package com.startup.forum.domain.service;

import com.startup.forum.infrastructure.storage.entity.News;

import java.util.List;

public interface NewsService {
    List<News> getThreeRandomNews();

    List<News> getAllNews();

    List<News> refreshNews();
}
