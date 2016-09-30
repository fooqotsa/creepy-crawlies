package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import com.dotsh.creepycrawlies.parser.PageParser;
import com.dotsh.creepycrawlies.retriever.DocumentRetriever;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class Crawler {

    private DocumentRetriever documentRetriever = new DocumentRetriever();

    public List<Page> crawl(Page initialPage, String hostUrl) throws IOException {
        Queue<String> queue = initialiseQueue(initialPage);
        Set<String> alreadyVisited = new HashSet<>();
        alreadyVisited.add(initialPage.getUrl());
        List<Page> pages = new ArrayList<>();
        pages.add(initialPage);
        while (!queue.isEmpty()) {
            String href = queue.remove();
            if (!alreadyVisited.contains(href)) {
                Document document = documentRetriever.retrieve(href);
                if (document != null) {
                    pages.add(parsePage(document, hostUrl));
                    alreadyVisited.add(href);
                }
            }
        }
        return pages;
    }

    protected Page parsePage(Document document, String hostUrl) {
        return new PageParser().buildFromDocument(document, hostUrl);
    }

    protected Queue<String> initialiseQueue(Page page) {
        Queue<String> queue = new LinkedList<>();
        queue.addAll(page.getInternalUrls());
        return queue;
    }

    public void setDocumentRetriever(DocumentRetriever documentRetriever) {
        this.documentRetriever = documentRetriever;
    }
}
