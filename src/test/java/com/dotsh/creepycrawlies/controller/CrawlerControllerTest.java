package com.dotsh.creepycrawlies.controller;

import com.dotsh.creepycrawlies.crawler.CrawlInitialiser;
import com.dotsh.creepycrawlies.model.Page;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CrawlerControllerTest {

    @Test
    public void controllerReturnsListOfPagesFound() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerController extends CrawlerController {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }
        when(mockInitialiser.connect("http://wiprodigital.com")).thenReturn(new ArrayList<>());
        TestCrawlerController controller = new TestCrawlerController();
        List<Page> pages = controller.crawl("http://wiprodigital.com");
        assertNotNull(pages);
    }

    @Test
    public void controllerCallsToInitialiseCrawler() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerController extends CrawlerController {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }

        TestCrawlerController crawlerController = new TestCrawlerController();
        crawlerController.crawl("");
        verify(mockInitialiser, times(1)).connect("");
    }
}
