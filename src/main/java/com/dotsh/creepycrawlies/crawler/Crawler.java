package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Crawler {

    public static final String HREF_SELECTOR = "a[href]";
    public static final String HREF_ATTRIBUTE = "href";

    public List<Page> connect(String url) throws IOException {
        throwIfUrlIsNullOrEmpty(url);
        List<Page> pages = new ArrayList<>();
        Document doc = retrieveDocument(url);
        pages.add(buildPageFromDocument(doc, url));
        return pages;
    }

    private Page buildPageFromDocument(Document doc, String url) {
        Page page = new Page();
        page.setTitle(doc.title());
        page.setUrl(doc.location());
        page.setInternalUrls(parseInternalUrls(doc, url));
        return page;
    }

    private Set<String> parseInternalUrls(Document doc, String url) {
        Set<String> urls = new HashSet<>();
        retrieveInternalUrlsFromLinkElements(url, urls, getAllElementsWithHrefs(doc));
        return urls;
    }

    private Elements getAllElementsWithHrefs(Document doc) {
        if (doc.body() != null) {
            return doc.body().select(HREF_SELECTOR);
        }
        return new Elements();
    }

    private void retrieveInternalUrlsFromLinkElements(String url, Set<String> urls, Elements linkElements) {
        for (Element element : linkElements) {
            Attributes attributes = element.attributes();
            if (attributes != null) {
                addHrefToSetIfInternalUrl(url, urls, attributes.get(HREF_ATTRIBUTE));
            }
        }
    }

    private void addHrefToSetIfInternalUrl(String url, Set<String> urls, String href) {
        if (href != null && href.toLowerCase().startsWith(url.toLowerCase())) {
            urls.add(href);
        }
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
