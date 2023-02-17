package com.example.springbootproj.controller;

import com.example.springbootproj.entity.News;
import com.example.springbootproj.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/all")
    public List<News> getAll() {
        return newsService.getAllNews();
    }

    @GetMapping("/refresh")
    public List<News> refreshAll() {
        return newsService.refreshNews();
    }
}
