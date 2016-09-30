package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;

import java.util.LinkedList;
import java.util.Queue;

public class Crawler {

    protected Queue<String> initialiseQueue(Page page) {
        Queue<String> queue = new LinkedList<>();
        queue.addAll(page.getInternalUrls());
        return queue;
    }

}
