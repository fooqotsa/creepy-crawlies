package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import com.dotsh.creepycrawlies.parser.PageParser;
import com.dotsh.creepycrawlies.retriever.DocumentRetriever;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class Crawler {

    private DocumentRetriever documentRetriever = new DocumentRetriever();
    private static final Logger LOGGER = LoggerFactory.getLogger(Crawler.class);

    public List<Page> crawl(Page initialPage, String hostUrl) throws IOException {
        Queue<String> queue = initialiseQueue(initialPage);
        Set<String> alreadyVisited = initialiseAlreadyVisitedSet(initialPage);
        List<Page> pages = new ArrayList<>();
        pages.add(initialPage);
        retrievePagesUntilAllInteralPagesParsed(hostUrl, queue, alreadyVisited, pages);
        return pages;
    }

    protected void retrievePagesUntilAllInteralPagesParsed(String hostUrl, Queue<String> queue, Set<String> alreadyVisited, List<Page> pages) throws IOException {
        while (!queue.isEmpty()) {
            retrieveAndParsePages(hostUrl, queue, alreadyVisited, pages);
        }
    }

    private void retrieveAndParsePages(String hostUrl, Queue<String> queue, Set<String> alreadyVisited, List<Page> pages) throws IOException {
        String href = queue.remove();
        if (!alreadyVisited.contains(href)) {
            LOGGER.debug("retrieving " + href + " for parsing");
            Document document = documentRetriever.retrieve(href);
            parseDocument(hostUrl, queue, alreadyVisited, pages, href, document);
        }
    }

    private void parseDocument(String hostUrl, Queue<String> queue, Set<String> alreadyVisited, List<Page> pages, String href, Document document) {
        if (document != null) {
            final Page page = parsePage(document, hostUrl);
            pages.add(page);
            alreadyVisited.add(href);
            addInternalLinksToQueue(queue, alreadyVisited, page.getInternalUrls());
        }
    }

    protected Set<String> initialiseAlreadyVisitedSet(Page initialPage) {
        Set<String> alreadyVisited = new HashSet<>();
        alreadyVisited.add(initialPage.getUrl());
        return alreadyVisited;
    }

    protected Page parsePage(Document document, String hostUrl) {
        return new PageParser().buildFromDocument(document, hostUrl);
    }

    protected Queue<String> initialiseQueue(Page page) {
        Queue<String> queue = new LinkedList<>();
        queue.addAll(page.getInternalUrls());
        return queue;
    }

    protected void addInternalLinksToQueue(Queue<String> queue, Set<String> alreadyVisited, Set<String> internalUrls) {
        internalUrls.stream().filter(url -> hasNotBeenVisitedAndIsNotInQueue(queue, alreadyVisited, url)).forEach(queue::add);
    }

    private boolean hasNotBeenVisitedAndIsNotInQueue(Queue<String> queue, Set<String> alreadyVisited, String url) {
        return !alreadyVisited.contains(url) && !queue.contains(url);
    }

    public void setDocumentRetriever(DocumentRetriever documentRetriever) {
        this.documentRetriever = documentRetriever;
    }
}
