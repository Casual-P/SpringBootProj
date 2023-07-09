package com.startup.forum.domain.service.impl.news;

import com.startup.forum.infrastructure.storage.entity.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BloombergParserService extends ParserService {

    public final static String URL = "https://www.bloomberg.com";

    @Override
    @Scheduled(fixedRate = 600000)
    protected void getNewsScheduled() {
        Document bloombergDoc = getBloombergDoc();
        if (bloombergDoc != null) {
            newsList = getAllNews(bloombergDoc);
            log.debug("Bloomberg news: {}", newsList.toString());
        }
    }

    @Override
    public void refreshNews() {
        getNewsScheduled();
    }

    private Document getBloombergDoc() {
        Document bloombergDoc;
        try {
            bloombergDoc = Jsoup.connect(BloombergParserService.URL)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                    .referrer("https://google.com/")
                    .followRedirects(true)
                    .cookie("seen_uk", "1")
                    .cookie("domain", "bloomberg.com")
                    .cookie("path", "/")
                    .cookie("exp_pref", "EUR")
                    .cookie("_pxhd", "GG4hCFjE2CmQYniFQh535tOiz6j9ys2Fse8ByH8JXhNGAoPttkESXUAAnOP5/GgUtJ39xDkbaDuH5Gl4UtlV2A==:BOMNqIgNk7hNfBZXJoj1LKiZrHtXUQRVCelGsKDG-k82N1uMkbTVduPxZytJn-RXomFPrceT9UAd0gxZP4Z-F8X6IL3hqkbRZ0h5-COODjQ=")
                    .cookie("Expires", "Fri, 01 Jan 2021 00:00:00 GMT")
                    .get();
        } catch (IOException e) {
            log.warn("Exception when connect to bloomberg.com : {}, URL: {}", e.getMessage(), BloombergParserService.URL);
            return null;
        }
        return bloombergDoc;
    }

    private String getTargetNewsUrl(Document bloombergDoc) {
        Optional<Element> targetElement = Optional.ofNullable(bloombergDoc.getElementsByClass("single-story-module__info").first());
        return targetElement.orElseThrow().child(1).attributes().get("href");
    }

    private List<News> getAllNews(Document bloombergDoc) {
        Elements elementsByClass = bloombergDoc.getElementsByClass("single-story-module__story");
        log.debug("bloomberg doc: {}", bloombergDoc);
        return elementsByClass.stream().map(e -> {
            log.debug("1: {}", e.toString());
            News news = new News();
            Element titleDiv = e.getElementsByClass("single-story-module__info").first();
            String title = Optional.ofNullable(titleDiv).orElseThrow().child(1).text();
            String href = titleDiv.child(1).attributes().get("href");
            news.setTitle(title);
            news.setOriginalUrl(URL.concat(href));
            news.setOrigin("Bloomberg");
            String time = e.getElementsByClass("hub-timestamp").text();
            news.setPublishDate(time);
            Element imageDiv = e.getElementsByClass("bb-lazy-img__image").first();
            String imageUrl;
            if (imageDiv == null) {
                imageDiv = Optional.ofNullable(e.getElementsByClass("single-story-module__video-player-img").first()).orElseThrow();
                imageUrl = imageDiv.attributes().get("src");
            }
            else {
                imageUrl = imageDiv.attributes().get("data-hi-res-src");
            }
            news.setImageUrl(imageUrl);
            log.debug("2: {}", news);
            return news;
        }).collect(Collectors.toList());
    }
}
