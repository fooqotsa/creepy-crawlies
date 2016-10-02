package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import com.dotsh.creepycrawlies.parser.PageParser;
import com.dotsh.creepycrawlies.retriever.DocumentRetriever;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class CrawlInitialiser {

    public List<Page> connect(String url) throws IOException {
        Document doc = retrieveDocument(url);
        return doc != null ? buildPages(doc, deriveHost(url)) : new ArrayList<>();
    }

    private List<Page> buildPages(Document doc, String host) throws IOException {
        List<Page> pages = new ArrayList<>();
        pages.addAll(initialiseCrawler().crawl(retrieveInitialPage(doc, host), host));
        return pages;
    }

    private Page retrieveInitialPage(Document doc, String host) {
        return new PageParser().buildFromDocument(doc, host);
    }

    private String deriveHost(String url) throws MalformedURLException {
        return new HostDeriver().parse(url);
    }

    protected Document retrieveDocument(String url) throws IOException {
        return new DocumentRetriever().retrieve(url);
    }

    protected Crawler initialiseCrawler() {
        return new Crawler();
    }
}
