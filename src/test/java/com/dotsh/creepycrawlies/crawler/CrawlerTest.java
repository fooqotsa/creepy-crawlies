package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
