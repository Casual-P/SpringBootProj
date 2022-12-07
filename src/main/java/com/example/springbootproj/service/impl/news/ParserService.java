package com.example.springbootproj.service.impl.news;

import com.example.springbootproj.entity.News;
import java.util.List;

public abstract class ParserService {
    protected List<News> newsList;

    public List<News> getNewsList() {
        return newsList;
    }

    protected abstract void getNewsScheduled();

    public abstract void refreshNews();
}
