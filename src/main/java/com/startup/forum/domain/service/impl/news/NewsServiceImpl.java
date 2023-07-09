package com.startup.forum.domain.service.impl.news;

import com.startup.forum.infrastructure.storage.entity.News;
import com.startup.forum.domain.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService {

    @Autowired private List<ParserService> newsParsers;

    private List<News> currentNews = new ArrayList<>();

    public List<News> getThreeRandomNews() {
        collectNews();
        return threeRandomNews();
    }

    @Override
    public List<News> getAllNews() {
        collectNews();
        return currentNews;
    }

    @Override
    public List<News> refreshNews() {
        newsParsers.forEach(ParserService::refreshNews);
        return getAllNews();
    }

    private void collectNews() {
        currentNews = newsParsers.stream()
                .map(ParserService::getNewsList)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<News> threeRandomNews() {
            Random random = new Random();
            return random
                    .ints(0, currentNews.size())
                    .distinct().limit(3)
                    .mapToObj(rnd -> currentNews.get(rnd))
                    .collect(Collectors.toList());
        }
}
