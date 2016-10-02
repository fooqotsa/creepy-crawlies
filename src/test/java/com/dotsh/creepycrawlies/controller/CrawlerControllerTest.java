package com.dotsh.creepycrawlies.controller;

import com.dotsh.creepycrawlies.crawler.CrawlInitialiser;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CrawlerControllerTest {

    @Test
    public void controllerReturnsModelAndView() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerController extends CrawlerController {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }
        when(mockInitialiser.connect("http://wiprodigital.com")).thenReturn(new ArrayList<>());
        TestCrawlerController controller = new TestCrawlerController();
        ModelAndView mav = controller.crawl();
        assertNotNull(mav);
    }

    @Test
    public void controllerReturnsViewOfResultsIfSuccess() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerController extends CrawlerController {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }
        when(mockInitialiser.connect("http://wiprodigital.com")).thenReturn(new ArrayList<>());
        TestCrawlerController controller = new TestCrawlerController();
        ModelAndView mav = controller.crawl();
        assertEquals("results", mav.getViewName());
    }

    @Test
    public void controllerReturnsListOfPagesInsideModel() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerController extends CrawlerController {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }
        when(mockInitialiser.connect("http://wiprodigital.com")).thenReturn(new ArrayList<>());
        TestCrawlerController controller = new TestCrawlerController();
        ModelAndView mav = controller.crawl();
        assertNotNull(mav.getModel().get("pages"));
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
        crawlerController.crawl();
        verify(mockInitialiser, times(1)).connect("http://wiprodigital.com");
    }

    @Test
    public void controllerReturnsErrorPageIfUnexpectecExceptionIsThrown() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerController extends CrawlerController {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }
        when(mockInitialiser.connect(anyString())).thenThrow(new IOException());
        TestCrawlerController crawlerController = new TestCrawlerController();
        ModelAndView mav = crawlerController.crawl();
        assertEquals("error", mav.getViewName());
    }
}
