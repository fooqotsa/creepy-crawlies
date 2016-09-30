package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import com.dotsh.creepycrawlies.parser.PageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {

    public List<Page> connect(String url) throws IOException {
        Document doc = retrieveDocument(url);
        String host = deriveHost(url);
        return buildPages(doc, host);
    }

    private List<Page> buildPages(Document doc, String host) throws IOException {
        List<Page> pages = new ArrayList<>();
        pages.add(new PageParser().buildFromDocument(doc, host));
        return pages;
    }

    private String deriveHost(String url) throws MalformedURLException {
        return new HostDeriver().parse(url);
    }

    protected Document retrieveDocument (String url) throws IOException {
        return Jsoup.connect(url).get();
    }

}
