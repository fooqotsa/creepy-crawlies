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

    @Ignore
    public void test() throws IOException {
        new CrawlInitialiser().connect(WIPRO_HOMEPAGE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void crawlerThrowsAnIllegalArgumentExceptionIfUrlIsNull() throws IOException {
        new CrawlInitialiser().connect(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void crawlerThrowsAnIllegalArgumentExceptionIfUrlIsEmpty() throws IOException {
        new CrawlInitialiser().connect("");
    }

}
