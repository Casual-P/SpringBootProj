package com.startup.forum.domain.service.impl.news;

import com.startup.forum.infrastructure.storage.entity.News;
import java.util.List;

public abstract class ParserService {
    protected List<News> newsList;

    public List<News> getNewsList() {
        return newsList;
    }

    protected abstract void getNewsScheduled();

    public abstract void refreshNews();
}
