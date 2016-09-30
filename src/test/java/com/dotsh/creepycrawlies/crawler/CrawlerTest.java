package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.junit.Test;

import java.util.HashSet;
import java.util.Queue;

import static org.junit.Assert.*;

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


//    @Test
//    public void crawler
}