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


    public Page buildFromDocument(Document doc, String url) {
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

}
