package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import com.dotsh.creepycrawlies.retriever.DocumentRetriever;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrawlerTest {

    @Test
    public void crawlerAddsInternalUrlFromInitialPageToQueueForProcessing() {
        class TestCrawler extends Crawler {
            public Queue<String> getInitialiseQueue(Page page) {
                return initialiseQueue(page);
            }
        }
        Page page = new Page();
        HashSet<String> internalUrls = new HashSet<>();
        internalUrls.add("internalUrl");

        page.setInternalUrls(internalUrls);
        TestCrawler crawler = new TestCrawler();
        Queue<String> queue = crawler.getInitialiseQueue(page);
        assertEquals(1, queue.size());
    }

    @Test
    public void crawlerAddsInternalUrlsFromInitialPageToQueueForProcessing() {
        class TestCrawler extends Crawler {
            public Queue<String> getInitialiseQueue(Page page) {
                return initialiseQueue(page);
            }
        }
        Page page = new Page();
        HashSet<String> internalUrls = new HashSet<>();
        internalUrls.add("internalUrl");
        internalUrls.add("internalUrl2");

        page.setInternalUrls(internalUrls);
        TestCrawler crawler = new TestCrawler();
        Queue<String> queue = crawler.getInitialiseQueue(page);
        assertEquals(2, queue.size());
    }

    @Test
    public void crawlerDoesNotAddInternalUrlsIfPageInternalUrlsIsEmpty() {
        class TestCrawler extends Crawler {
            public Queue<String> getInitialiseQueue(Page page) {
                return initialiseQueue(page);
            }
        }
        Page page = new Page();
        HashSet<String> internalUrls = new HashSet<>();
        page.setInternalUrls(internalUrls);
        TestCrawler crawler = new TestCrawler();
        Queue<String> queue = crawler.getInitialiseQueue(page);
        assertEquals(0, queue.size());
    }


    @Test
    public void crawlerLoopsThroughQueueAndBuildsPages() throws IOException {
        Crawler crawler = new Crawler();
        Page page = new Page();
        HashSet<String> internalUrls = new HashSet<>();
        internalUrls.add("wiprodigital.com");
        page.setInternalUrls(internalUrls);
        List<Page> pages = crawler.crawl(page);
        assertEquals(2, pages.size());
    }

}