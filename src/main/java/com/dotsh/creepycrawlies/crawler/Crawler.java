package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {

    public List<Page> connect(String url) throws IOException {
        throwIfUrlIsNullOrEmpty(url);
        List<Page> pages = new ArrayList<>();
        Document doc = retrieveDocument(url);
        Page page = new Page();
        page.setTitle(doc.title());
        page.setUrl(doc.location());
        pages.add(page);
        return pages;
    }

    protected Document retrieveDocument (String url) {
        return new Document("");
    }

    private void throwIfUrlIsNullOrEmpty(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("Url must not be null");
        }
    }
}
