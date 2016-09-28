package com.dotsh.creepycrawlies.parser;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

public class PageParser {

    public static final String HREF_SELECTOR = "a[href]";
    public static final String HREF_ATTRIBUTE = "href";
    public static final String STATIC_CONTENT_SELECTOR = "[src]";
    public static final String SRC_ATTRIBUTE = "src";
    public static final String SAME_PAGE_ANCHOR_PREFIX = "#";
    public static final String HASH_QUERY_STRING = "#";
    public static final int URL_FIRST_SECTION = 0;
    public static final String QUESTION_MARK_SPLITTER = "\\?";
    public static final String QUESTION_MARK_QUERY_STRING = "?";

    public Page buildFromDocument(Document doc, String url) {
        Page page = new Page();
        page.setTitle(doc.title());
        page.setUrl(doc.location());
        Elements hrefElements = getAllElementsWithHrefs(doc);
        Elements staticContentElements = getAllElementsWithStaticContent(doc);
        page.setInternalUrls(parseInternalUrls(hrefElements, url));
        page.setExternalUrls(parseExternalUrls(hrefElements, url));
        page.setStaticContent(parseStaticContent(staticContentElements));
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

    private Set<String> parseStaticContent(Elements staticContentElements) {
        Set<String> urls = new HashSet<>();
        retrieveStaticContentFromElements(staticContentElements, urls);
        return urls;
    }

    private Elements getAllElementsWithHrefs(Document doc) {
        return queryDocument(doc, HREF_SELECTOR);
    }

    private Elements getAllElementsWithStaticContent(Document doc) {
        return queryDocument(doc, STATIC_CONTENT_SELECTOR);
    }

    private Elements queryDocument(Document doc, String selector) {
        if (doc.body() != null) {
            return doc.body().select(selector);
        }
        return new Elements();
    }


    private void retrieveInternalUrlsFromLinkElements(String url, Set<String> urls, Elements linkElements) {
        for (Element element : linkElements) {
            final Attributes attributes = element.attributes();
            if (attributes != null) {
                addHrefToSetIfInternalUrl(url, urls, stripUrlIfQueryParametersArePresent(attributes.get(HREF_ATTRIBUTE)));
            }
        }
    }

    private void retrieveExternalUrlsFromLinkElements(String url, Set<String> urls, Elements linkElements) {
        for (Element element : linkElements) {
            final Attributes attributes = element.attributes();
            if (attributes != null) {
                addHrefToSetIfExternalUrl(url, urls, stripUrlIfQueryParametersArePresent(attributes.get(HREF_ATTRIBUTE)));
            }
        }
    }

    private String stripUrlIfQueryParametersArePresent(String href) {
        if (href != null) {
            if (href.contains(HASH_QUERY_STRING)) {
                href = href.split(HASH_QUERY_STRING)[URL_FIRST_SECTION];
            }
            if (href.contains(QUESTION_MARK_QUERY_STRING)) {
                href = href.split(QUESTION_MARK_SPLITTER)[URL_FIRST_SECTION];
            }
        }
        return href;
    }

    private void retrieveStaticContentFromElements(Elements staticContentElements, Set<String> urls) {
        for (Element staticContentElement : staticContentElements) {
            final Attributes attributes = staticContentElement.attributes();
            if (attributes != null) {
                addStaticContentToSet(urls, attributes.get(SRC_ATTRIBUTE));
            }
        }
    }

    private void addStaticContentToSet(Set<String> urls, String src) {
        if (src != null && !src.isEmpty()) {
            urls.add(src);
        }
    }

    private void addHrefToSetIfExternalUrl(String url, Set<String> urls, String href) {
        if (href != null && !href.isEmpty() && !isOnSameDomain(url, href) && !isASamePageAnchor(href)) {
            urls.add(href);
        }
    }

    private void addHrefToSetIfInternalUrl(String url, Set<String> urls, String href) {
        if (href != null && !href.isEmpty() && isOnSameDomain(url, href)) {
            urls.add(href);
        }
    }

    private boolean isASamePageAnchor(String href) {
        return href.startsWith(SAME_PAGE_ANCHOR_PREFIX);
    }

    private boolean isOnSameDomain(String url, String href) {
        return href.toLowerCase().contains(url.toLowerCase());
    }

}
