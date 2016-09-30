package com.dotsh.creepycrawlies.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

import static com.dotsh.creepycrawlies.parser.PageParser.HREF_ATTRIBUTE;

public class UrlParser {

    public static final String SRC_ATTRIBUTE = "src";
    public static final String SAME_PAGE_ANCHOR_PREFIX = "#";
    private QueryParamParser paramParser = new QueryParamParser();

    public Set<String> retrieveInternalUrlsFromLinkElements(String url, Elements linkElements) {
        Set<String> urls = new HashSet<>();
        for (Element element : linkElements) {
            buildInternalUrl(url, urls, element);
        }
        return urls;
    }


    public Set<String> retrieveExternalUrlsFromLinkElements(String url, Elements linkElements) {
        Set<String> urls = new HashSet<>();
        for (Element element : linkElements) {
            buildExternalUrl(url, urls, element);
        }
        return urls;
    }

    public Set<String> retrieveStaticContentFromElements(Elements staticContentElements) {
        Set<String> urls = new HashSet<>();
        for (Element staticContentElement : staticContentElements) {
            final Attributes attributes = staticContentElement.attributes();
            if (attributes != null) {
                addStaticContentToSet(urls, attributes.get(SRC_ATTRIBUTE));
            }
        }
        return urls;
    }

    private void buildInternalUrl(String url, Set<String> urls, Element element) {
        final Attributes attributes = element.attributes();
        if (attributes != null) {
            addHrefToSetIfInternalUrl(url, urls, paramParser.stripUrlIfQueryParametersArePresent(attributes.get(HREF_ATTRIBUTE)));
        }
    }

    private void buildExternalUrl(String url, Set<String> urls, Element element) {
        final Attributes attributes = element.attributes();
        if (attributes != null) {
            addHrefToSetIfExternalUrl(url, urls, paramParser.stripUrlIfQueryParametersArePresent(attributes.get(HREF_ATTRIBUTE)));
        }
    }

    private void addStaticContentToSet(Set<String> urls, String src) {
        if (src != null && !src.isEmpty()) {
            urls.add(src);
        }
    }

    private void addHrefToSetIfExternalUrl(String url, Set<String> urls, String href) {
        if (href != null && !href.isEmpty() && !isOnSameDomain(url, href) && !isASamePageAnchor(href)) {
            urls.add(href.trim());
        }
    }

    private void addHrefToSetIfInternalUrl(String url, Set<String> urls, String href) {
        if (href != null && !href.isEmpty() && isOnSameDomain(url, href)) {
            urls.add(href.trim());
        }
    }

    private boolean isASamePageAnchor(String href) {
        return href.startsWith(SAME_PAGE_ANCHOR_PREFIX);
    }

    private boolean isOnSameDomain(String url, String href) {
        return href.toLowerCase().contains(url.toLowerCase());
    }

}
