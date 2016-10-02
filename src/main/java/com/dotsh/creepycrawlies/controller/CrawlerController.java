package com.dotsh.creepycrawlies.controller;

import com.dotsh.creepycrawlies.crawler.CrawlInitialiser;
import com.dotsh.creepycrawlies.model.Page;

import java.io.IOException;
import java.util.List;

public class CrawlerController {
    public List<Page> crawl(String website) throws IOException {
        return getCrawlInitialiser().connect(website);
    }

    protected CrawlInitialiser getCrawlInitialiser() {
        return new CrawlInitialiser();
    }
}
