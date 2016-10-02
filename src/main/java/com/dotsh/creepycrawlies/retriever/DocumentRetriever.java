package com.dotsh.creepycrawlies.retriever;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DocumentRetriever {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentRetriever.class);

    public Document retrieve(String url) throws IOException {
        try {
            return Jsoup.connect(url).get();
        } catch (IllegalArgumentException | HttpStatusException e) {
            handleException(url, e);
            return null;
        }
    }

    protected void handleException(String url, Exception e) {
        LOGGER.warn("dud url " + e.getLocalizedMessage());
        LOGGER.warn("dud url = " + url);
    }
}
