package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {

    public List<Page> connect(String url) throws IOException {
        throwIfUrlIsNullOrEmpty(url);
        List<Page> pages = new ArrayList<>();
        return pages;
    }

    private void throwIfUrlIsNullOrEmpty(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("Url must not be null");
        }
    }
}
