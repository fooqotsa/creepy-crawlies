package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrawlerTest {

    public static final String WIPRO_HOMEPAGE = "http://wiprodigital.com";

    @Test
    public void crawlerRetrievesAListOfPagesFromSpecifiedWebsite() throws IOException {
        Crawler crawler = new Crawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertNotNull(pages);
    }

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
    public void retrievesAListOfInternalUrlsOnCurrentPageAndAddsThemToPageObject() throws IOException {
        class TestCrawler extends Crawler {
            @Override
            protected Document retrieveDocument(String url) {
                Document document = mock(Document.class);
                Element topLevelElement = mock(Element.class);
                Element navElement = mock(Element.class);
                Elements elements = new Elements();
                Attributes attributes = mock(Attributes.class);
                elements.add(0, navElement);

                when(document.body()).thenReturn(topLevelElement);
                when(topLevelElement.select(anyString())).thenReturn(elements);
                when(attributes.get("href")).thenReturn("http://wiprodigital.com/who-we-are");
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertTrue(pages.get(0).getInternalUrls().contains("http://wiprodigital.com/who-we-are"));
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
