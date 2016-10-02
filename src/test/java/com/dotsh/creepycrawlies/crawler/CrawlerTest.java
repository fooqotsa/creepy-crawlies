package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import com.dotsh.creepycrawlies.retriever.DocumentRetriever;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CrawlerTest {

    DocumentRetriever retriever = mock(DocumentRetriever.class);

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
        when(retriever.retrieve(any())).thenReturn(new Document(""));
        crawler.setDocumentRetriever(retriever);
        Page page = new Page();
        HashSet<String> internalUrls = new HashSet<>();
        internalUrls.add("wiprodigital.com");
        page.setInternalUrls(internalUrls);
        List<Page> pages = crawler.crawl(page, "");
        assertEquals(2, pages.size());
    }

    @Test
    public void retrievesDocumentForPageParsingForEachUrl() throws IOException {
        Crawler crawler = new Crawler();
        Document document = mock(Document.class);
        crawler.setDocumentRetriever(retriever);
        when(retriever.retrieve(any())).thenReturn(document);
        Page initialPage = new Page();
        HashSet<String> internalUrls = new HashSet<>();
        internalUrls.add("wiprodigital.com/stuff");
        initialPage.setInternalUrls(internalUrls);
        crawler.crawl(initialPage, "");
        verify(retriever, times(1)).retrieve("wiprodigital.com/stuff");
    }

    @Test
    public void crawlerDoesNotAddPageIfDocumentIsNull() throws IOException {
        Crawler crawler = new Crawler();
        crawler.setDocumentRetriever(retriever);
        when(retriever.retrieve(any())).thenReturn(null);
        Page initialPage = new Page();
        HashSet<String> internalUrls = new HashSet<>();
        internalUrls.add("wiprodigital.com/stuff");
        initialPage.setInternalUrls(internalUrls);
        List<Page> pages = crawler.crawl(initialPage, "");
        assertEquals(1, pages.size());
    }

    @Test
    public void crawlerPassesDocumentToPageParse() throws IOException {
        class TestCrawler extends Crawler {
            public Document doc = null;

            @Override
            protected Page parsePage(Document document, String hostUrl) {
                doc = document;
                return new Page();
            }
        }
        TestCrawler crawler = new TestCrawler();
        crawler.setDocumentRetriever(retriever);
        Document document = new Document("");
        when(retriever.retrieve(any())).thenReturn(document);
        Page initialPage = new Page();
        HashSet<String> internalUrls = new HashSet<>();
        internalUrls.add("");
        initialPage.setInternalUrls(internalUrls);
        crawler.crawl(initialPage, "");
        assertSame(document, crawler.doc);
    }

    @Test
    public void crawlerPassesInHostUrlToParsePage() throws IOException {
        class TestCrawler extends Crawler {
            public String host = null;

            @Override
            protected Page parsePage(Document document, String hostUrl) {
                host = hostUrl;
                return new Page();
            }
        }
        TestCrawler crawler = new TestCrawler();
        crawler.setDocumentRetriever(retriever);
        Document document = new Document("");
        when(retriever.retrieve(any())).thenReturn(document);
        Page initialPage = new Page();
        HashSet<String> internalUrls = new HashSet<>();
        internalUrls.add("");
        initialPage.setInternalUrls(internalUrls);
        crawler.crawl(initialPage, "hostUrl");
        assertSame("hostUrl", crawler.host);
    }

    @Test
    public void crawlerOnlyVisitsAPageOnce() throws IOException {
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

        Crawler crawler = new Crawler();
        crawler.setDocumentRetriever(retriever);

        when(retriever.retrieve(any())).thenReturn(document);
        Page page = new Page();
        page.setUrl("http://wiprodigital.com/who-we-are");
        HashSet<String> internalUrls = new HashSet<>();
        internalUrls.add("http://wiprodigital.com/who-we-are");
        page.setInternalUrls(internalUrls);
        List<Page> pages = crawler.crawl(page, "wiprodigital.com");
        assertEquals(1, pages.size());
    }

    @Test
    public void ifParsedPageHasInternalRefThatHasAlreadyBeenVisitedThenItDoesNotJoinTheQueue() {
        class TestCrawler extends Crawler {
            public void parseQueue(Queue<String> queue, Set<String> alreadyVisited, Page page) {
                addInternalLinksToQueue(queue, alreadyVisited, page.getInternalUrls());
            }
        }

        TestCrawler crawler = new TestCrawler();
        Queue<String> queue = new LinkedList<>();
        Page page = new Page();
        Set<String> internalUrls = new HashSet<>();
        Set<String> alreadyVisited = new HashSet<>();
        alreadyVisited.add("http://wiprodigital.com/1");
        internalUrls.add("http://wiprodigital.com/1");
        internalUrls.add("http://wiprodigital.com/2");
        page.setInternalUrls(internalUrls);
        crawler.parseQueue(queue, alreadyVisited, page);

        assertTrue(queue.contains("http://wiprodigital.com/2"));
    }

}