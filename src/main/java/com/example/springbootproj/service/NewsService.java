package com.example.springbootproj.service;

import com.example.springbootproj.entity.News;

import java.util.List;

public interface NewsService {
    List<News> getThreeRandomNews();

    List<News> getAllNews();

    List<News> refreshNews();
}
