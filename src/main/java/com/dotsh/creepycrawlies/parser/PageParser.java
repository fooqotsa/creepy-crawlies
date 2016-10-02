package com.dotsh.creepycrawlies.parser;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Set;

public class PageParser {

    public static final String HREF_SELECTOR = "a[href]";
    public static final String HREF_ATTRIBUTE = "href";
    public static final String STATIC_CONTENT_SELECTOR = "[src]";
    private UrlParser urlParser = new UrlParser();

    public Page buildFromDocument(Document doc, String url) {
        Page page = initialisePage(doc);
        final Elements hrefElements = getAllElementsWithHrefs(doc);
        final Elements staticContentElements = getAllElementsWithStaticContent(doc);
        parsePageContent(url, page, hrefElements, staticContentElements);
        return page;
    }

    private Page initialisePage(Document doc) {
        Page page = new Page();
        page.setTitle(doc.title());
        page.setUrl(doc.location());
        return page;
    }

    private void parsePageContent(String url, Page page, Elements hrefElements, Elements staticContentElements) {
        page.setInternalUrls(parseInternalUrls(hrefElements, url));
        page.setExternalUrls(parseExternalUrls(hrefElements, url));
        page.setStaticContent(parseStaticContent(staticContentElements));
    }

    private Set<String> parseInternalUrls(Elements hrefElements, String url) {
        return urlParser.retrieveInternalUrlsFromLinkElements(url, hrefElements);
    }

    private Set<String> parseExternalUrls(Elements hrefElements, String url) {
        return urlParser.retrieveExternalUrlsFromLinkElements(url, hrefElements);
    }

    private Set<String> parseStaticContent(Elements staticContentElements) {
        return urlParser.retrieveStaticContentFromElements(staticContentElements);
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

}
