package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Document;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CrawlInitialiserTest {

    public static final String WIPRO_HOMEPAGE = "http://wiprodigital.com";

    @Test
    public void retrievesTitleFromDocumentAndAddsItToPage() throws IOException {
        class TestCrawlInitialiser extends CrawlInitialiser {
            @Override
            protected Document retrieveDocument (String url) {
                Document document = mock(Document.class);
                when(document.title()).thenReturn("title");
                return document;
            }
        }

        TestCrawlInitialiser crawler = new TestCrawlInitialiser();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals("title", pages.get(0).getTitle());
    }

    @Test
    public void retrievesUrlOfCurrentPageAndAddsItToPageObject() throws IOException {
        class TestCrawlInitialiser extends CrawlInitialiser {
            @Override
            protected Document retrieveDocument (String url) {
                Document document = mock(Document.class);
                when(document.location()).thenReturn(WIPRO_HOMEPAGE);
                return document;
            }
        }

        TestCrawlInitialiser crawler = new TestCrawlInitialiser();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(WIPRO_HOMEPAGE, pages.get(0).getUrl());
    }

    @Test
    public void returnsEmptyListIfInitialDocumentIsNull() throws IOException {
        assertTrue(new CrawlInitialiser().connect("url").isEmpty());
    }

    @Test
    public void afterInitialPageIsParsedCrawlerIsCalledToRecursivelyCrawlThroughInternalUrls() throws IOException {
        Crawler mockCrawler = mock(Crawler.class);
        class TestCrawlerInitialiser extends CrawlInitialiser {
            @Override
            protected Crawler initialiseCrawler() {
                return mockCrawler;
            }

            @Override
            protected Document retrieveDocument (String url) {
                Document document = mock(Document.class);
                when(document.location()).thenReturn(WIPRO_HOMEPAGE);
                return document;
            }
        }
        TestCrawlerInitialiser crawlerInitialiser = new TestCrawlerInitialiser();
        crawlerInitialiser.connect(WIPRO_HOMEPAGE);
        verify(mockCrawler, times(1)).crawl(any(), any());
    }

}
