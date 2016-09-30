package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import com.dotsh.creepycrawlies.retriever.DocumentRetriever;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Crawler {

    public List<Page> crawl(Page initialPage) {
        Queue<String> queue = initialiseQueue(initialPage);
        List<Page> pages = new ArrayList<>();
        pages.add(initialPage);
        while (!queue.isEmpty()) {
            queue.remove();
            pages.add(parsePage());
        }
        return pages;
    }

    protected Page parsePage() {
        return new Page();
    }

    protected Queue<String> initialiseQueue(Page page) {
        Queue<String> queue = new LinkedList<>();
        queue.addAll(page.getInternalUrls());
        return queue;
    }

}
