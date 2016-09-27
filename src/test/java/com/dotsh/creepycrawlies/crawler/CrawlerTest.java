package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.dotsh.creepycrawlies.parser.PageParser.HREF_ATTRIBUTE;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrawlerTest {

    public static final String WIPRO_HOMEPAGE = "http://wiprodigital.com";
    public static final String STATIC_CONTENT_SAMPLE = "http://17776-presscdn-0-6.pagely.netdna-cdn.com/wp-content/themes/wiprodigital/images/wdlogo.png";

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

    @Test
    public void doesNotAddToListOfInternalUrlsIfUrlIsNotAtSameDomain() throws IOException {
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
                when(attributes.get("href")).thenReturn("http://externalSite.com");
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertTrue(pages.get(0).getInternalUrls().isEmpty());
    }

    @Test
    public void doesNotAddToListOfInternalUrlsIfUrlIsNull() throws IOException {
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
                when(attributes.get("href")).thenReturn(null);
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertTrue(pages.get(0).getInternalUrls().isEmpty());
    }

    @Test
    public void doesNotAddToListOfInternalUrlsIfUrlIsEmpty() throws IOException {
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
                when(attributes.get("href")).thenReturn("");
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertTrue(pages.get(0).getInternalUrls().isEmpty());
    }

    @Test
    public void onlyAddsOneOfTheSameUrl() throws IOException {
        class TestCrawler extends Crawler {
            @Override
            protected Document retrieveDocument(String url) {
                Document document = mock(Document.class);
                Element topLevelElement = mock(Element.class);
                Element navElement = mock(Element.class);
                Elements elements = new Elements();
                Attributes attributes = mock(Attributes.class);
                elements.add(0, navElement);
                elements.add(1, navElement);

                when(document.body()).thenReturn(topLevelElement);
                when(topLevelElement.select(anyString())).thenReturn(elements);
                when(attributes.get(HREF_ATTRIBUTE)).thenReturn(WIPRO_HOMEPAGE);
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(1, pages.get(0).getInternalUrls().size());
    }

    @Test
    public void addsOneFromSubDomain() throws IOException {
        class TestCrawler extends Crawler {
            @Override
            protected Document retrieveDocument(String url) {
                Document document = mock(Document.class);
                Element topLevelElement = mock(Element.class);
                Element navElement = mock(Element.class);
                Elements elements = new Elements();
                Attributes attributes = mock(Attributes.class);
                elements.add(0, navElement);
                elements.add(1, navElement);

                when(document.body()).thenReturn(topLevelElement);
                when(topLevelElement.select(anyString())).thenReturn(elements);
                when(attributes.get(HREF_ATTRIBUTE)).thenReturn("http://www.subdomain.wiprodigital.com");
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(1, pages.get(0).getInternalUrls().size());
    }

    @Test
    public void onlyAddsMultipleDifferentUrls() throws IOException {
        class TestCrawler extends Crawler {
            @Override
            protected Document retrieveDocument(String url) {
                Document document = mock(Document.class);
                Element topLevelElement = mock(Element.class);
                Element navElement = mock(Element.class);
                Element navElement2 = mock(Element.class);
                Elements elements = new Elements();
                Attributes attributes = mock(Attributes.class);
                Attributes attributes2 = mock(Attributes.class);
                elements.add(0, navElement);
                elements.add(1, navElement2);

                when(document.body()).thenReturn(topLevelElement);
                when(topLevelElement.select(anyString())).thenReturn(elements);
                when(attributes.get(HREF_ATTRIBUTE)).thenReturn(WIPRO_HOMEPAGE);
                when(attributes2.get(HREF_ATTRIBUTE)).thenReturn(WIPRO_HOMEPAGE + "/other");
                when(navElement.attributes()).thenReturn(attributes);
                when(navElement2.attributes()).thenReturn(attributes2);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(2, pages.get(0).getInternalUrls().size());
    }

    @Test
    public void addsExternalUrlsToPage() throws IOException {
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
                when(attributes.get("href")).thenReturn("http://google.com");
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(1, pages.get(0).getExternalUrls().size());
    }

    @Test
    public void doesNotAddHashLinksToExternalUrls() throws IOException {
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
                when(attributes.get("href")).thenReturn("#pageFocus");
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(0, pages.get(0).getExternalUrls().size());
    }

    @Test
    public void addsStaticContentToPage() throws IOException {
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
                when(attributes.get("src")).thenReturn(STATIC_CONTENT_SAMPLE);
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(1, pages.get(0).getStaticContent().size());
    }

    @Test
    public void doesNotAddStaticContentIfNull() throws IOException {
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
                when(attributes.get("src")).thenReturn(null);
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(0, pages.get(0).getStaticContent().size());
    }

    @Test
    public void doesNotAddStaticContentIfEmpty() throws IOException {
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
                when(attributes.get("src")).thenReturn("");
                when(navElement.attributes()).thenReturn(attributes);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(0, pages.get(0).getStaticContent().size());
    }

    @Test
    public void doesNotAttemptToAddUrlsToPageIfAttributesIsNull() throws IOException {
        class TestCrawler extends Crawler {
            @Override
            protected Document retrieveDocument(String url) {
                Document document = mock(Document.class);
                Element topLevelElement = mock(Element.class);
                Element navElement = mock(Element.class);
                Elements elements = new Elements();
                elements.add(0, navElement);

                when(document.body()).thenReturn(topLevelElement);
                when(topLevelElement.select(anyString())).thenReturn(elements);
                when(navElement.attributes()).thenReturn(null);

                return document;
            }
        }
        TestCrawler crawler = new TestCrawler();
        List<Page> pages = crawler.connect(WIPRO_HOMEPAGE);
        assertEquals(0, pages.get(0).getInternalUrls().size());
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
