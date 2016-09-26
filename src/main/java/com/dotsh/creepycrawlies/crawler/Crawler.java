package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
        String host = deriveHost(url);
        pages.add(buildPageFromDocument(doc, host));
        return pages;
    }

    private String deriveHost(String url) throws MalformedURLException {
        return new HostDeriver().parse(url);
    }

    private Page buildPageFromDocument(Document doc, String url) {
        Page page = new Page();
        page.setTitle(doc.title());
        page.setUrl(doc.location());
        Elements hrefElements = getAllElementsWithHrefs(doc);
        page.setInternalUrls(parseInternalUrls(hrefElements, url));
        page.setExternalUrls(parseExternalUrls(hrefElements, url));
        return page;
    }

    private Set<String> parseInternalUrls(Elements hrefElements, String url) {
        Set<String> urls = new HashSet<>();
        retrieveInternalUrlsFromLinkElements(url, urls, hrefElements);
        return urls;
    }

    private Set<String> parseExternalUrls(Elements hrefElements, String url) {
        Set<String> urls = new HashSet<>();
        retrieveExternalUrlsFromLinkElements(url, urls, hrefElements);
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

    private void retrieveExternalUrlsFromLinkElements(String url, Set<String> urls, Elements linkElements) {
        for (Element element : linkElements) {
            Attributes attributes = element.attributes();
            if (attributes != null) {
                addHrefToSetIfExternalUrl(url, urls, attributes.get(HREF_ATTRIBUTE));
            }
        }
    }

    private void addHrefToSetIfExternalUrl(String url, Set<String> urls, String href) {
        if (href != null && !isOnSameDomain(url, href)) {
            urls.add(href);
        }
    }

    private void addHrefToSetIfInternalUrl(String url, Set<String> urls, String href) {
        if (href != null && isOnSameDomain(url, href)) {
            urls.add(href);
        }
    }

    private boolean isOnSameDomain(String url, String href) {
        return href.toLowerCase().contains(url.toLowerCase());
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
