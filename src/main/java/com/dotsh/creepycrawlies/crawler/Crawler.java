package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Crawler {

    public List<Page> connect(String url) throws IOException {
        throwIfUrlIsNullOrEmpty(url);
        List<Page> pages = new ArrayList<>();
        Document doc = retrieveDocument(url);
        pages.add(buildPageFromDocument(doc));
        return pages;
    }

    private Page buildPageFromDocument(Document doc) {
        Page page = new Page();
        page.setTitle(doc.title());
        page.setUrl(doc.location());
        page.setInternalUrls(parseInternalUrls(doc));
        return page;
    }

    private Set<String> parseInternalUrls(Document doc) {
        Set<String> urls = new HashSet<>();
        Elements linkElements = doc.body().select("a[href]");
        for (Element element : linkElements) {
            urls.add(element.attributes().get("href"));
        }
        return urls;
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
