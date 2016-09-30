package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrawlerTest {

    public static final String WIPRO_HOMEPAGE = "http://wiprodigital.com";

    @Test
    public void retrievesTitleFromDocumentAndAddsItToPage() throws IOException {
        class TestCrawler extends Crawler {
            @Override
            protected Document retrieveDocument (String url) {
                Document document = mock(Document.class);
                when(document.title()).thenReturn("title");
                return document;
            }
        }

        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals("title", pages.get(0).getTitle());
    }

    @Test
    public void retrievesUrlOfCurrentPageAndAddsItToPageObject() throws IOException {
        class TestCrawler extends Crawler {
            @Override
            protected Document retrieveDocument (String url) {
                Document document = mock(Document.class);
                when(document.location()).thenReturn(WIPRO_HOMEPAGE);
                return document;
            }
        }

        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(WIPRO_HOMEPAGE, pages.get(0).getUrl());
    }

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
    public void crawlerDoesNotAddsInternalUrlsIfPageInternalUrlsIsEmpty() {
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


    @Ignore
    public void test() throws IOException {
        new Crawler().connect(WIPRO_HOMEPAGE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void crawlerThrowsAnIllegalArgumentExceptionIfUrlIsNull() throws IOException {
        new Crawler().connect(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void crawlerThrowsAnIllegalArgumentExceptionIfUrlIsEmpty() throws IOException {
        new Crawler().connect("");
    }

}
