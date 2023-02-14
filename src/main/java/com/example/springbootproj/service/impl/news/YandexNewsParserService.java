package com.example.springbootproj.service.impl.news;

import com.example.springbootproj.entity.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class YandexNewsParserService extends ParserService{

    public final static String URL = "https://dzen.ru/news?issue_tld=ru";

    @Override
    @Scheduled(fixedRate = 600000)
    protected void getNewsScheduled() {
        Document yandexDoc = getYandexDoc(URL);
        if (yandexDoc != null) {
            newsList = getAllNews(yandexDoc);
        }
    }

    @Override
    public void refreshNews() {
        getNewsScheduled();
    }

    private Document getYandexDoc(String currentUrl) {
        Document yandexDoc;
        try {
            yandexDoc = Jsoup.connect(currentUrl)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                    .referrer("https://yandex.ru/")
                    .followRedirects(true)
                    .cookie("_yasc", "boB4B+csyuJOBsexSFi6Ltr79e/mA04olKMYU1kDBhFy3bIzs/+KDJiCSuc=")
                    .cookie("zen_sso_checked", "1")
                    .cookie("sso_status", "sso.passport.yandex.ru:synchronized")
                    .cookie("domain", ".dzen.ru")
                    .cookie("path", "/")
                    .cookie("expires", "Fri, 05-Nov-2032 16:25:12 GMT")
                    .cookie("yandex_login", "")
                    .cookie("yandexuid", "1127325071674582777")
                    .cookie("vid", "01a05cca497c2ba4")
                    .cookie("tmr_lvid", "41ec10fecf2c97a199458e11faa7322a")
                    .cookie("tmr_lvidTS", "1667909477998")
                    .cookie("_ym_uid", "1667909478377551768")
                    .cookie("_ym_d", "1667909478")
                    .cookie("_ym_isad", "2")
                    .cookie("Session_id", "noauth:1674582777")
                    .cookie("ys", "c_chck.2827640846")
                    .cookie("mda2_beacon", "1674582777597")
                    .cookie("tmr_detect", "0%7C1667917571139")
                    .cookie("tmr_reqNum", "12")
                    .get();
        } catch (IOException e) {
            log.warn("Exception when connect to dzen.ru : {}, URL: {}", e.getMessage(), currentUrl);
            return null;
        }
        return yandexDoc;
    }

    private String getTargetNewsUrl(Document yandexDoc) {
        Optional<Element> targetElement = Optional.ofNullable(yandexDoc.getElementsByClass("card-big _topnews _news").first());
        return targetElement.orElseThrow().attributes().get("href");
    }

    private List<News> getAllNews(Document yandexDoc) {
        Element storiesDiv = yandexDoc.getElementsByClass("news-top-flexible-stories").first();
        Elements elementsByClass = Optional.ofNullable(storiesDiv).orElseThrow().getElementsByClass("mg-grid__col");
        return elementsByClass.stream().map(e -> {
            News news = new News();
            Elements titleDiv = e.getElementsByClass("mg-card__title");
            String title = titleDiv.text();
            String href = Optional.ofNullable(titleDiv.first()).orElseThrow().child(0).attributes().get("href");
            news.setTitle(title);
            String annotation = e.getElementsByClass("mg-card__annotation").text();
            news.setText(annotation);
            String origin = e.getElementsByClass("mg-card-source__source").text();
            news.setOrigin(origin);
            String time = e.getElementsByClass("mg-card-source__time").text();
            news.setPublishDate(time.concat(" " + LocalDate.now()));
            Element imageDiv = e.getElementsByClass("neo-image").first();
            String imageUrl;
            if (imageDiv != null)
                imageUrl = imageDiv.attributes().get("src");
            else {
                Element small = e.getElementsByClass("mg-card__media-block_type_image").first();
                String styleBackground = Optional.ofNullable(small).orElseThrow().attributes().get("style");
                imageUrl = styleBackground.substring(21, styleBackground.length() - 1);
            }
            news.setImageUrl(imageUrl);
            Document originYandexDoc = getYandexDoc(href);
            Element yandexOriginElement = originYandexDoc != null ? originYandexDoc.getElementsByClass("mg-story__title").first() : null;
            String originHref = Optional.ofNullable(yandexOriginElement).orElseThrow().child(0).attributes().get("href");
            news.setOriginalUrl(originHref);
            return news;
        }).collect(Collectors.toList());
    }
}
