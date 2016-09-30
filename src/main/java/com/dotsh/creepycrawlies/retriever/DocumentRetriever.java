package com.dotsh.creepycrawlies.retriever;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocumentRetriever {

    public Document retrieve(String url) throws IOException {
        try {
            return Jsoup.connect(url).get();
        } catch (IllegalArgumentException | HttpStatusException e) {
            handleException(url, e);
            return null;
        }
    }

    protected void handleException(String url, Exception e) {
        System.out.println("dud url " + e.getLocalizedMessage());
        System.out.println("dud url = " + url);
    }
}
