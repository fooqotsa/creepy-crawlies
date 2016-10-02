package com.dotsh.creepycrawlies.request;

import com.dotsh.creepycrawlies.crawler.CrawlInitialiser;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class CrawlerControllerRequestHandlerTest {

    public static final String URL = "http://wiprodigital.com";

    @Test
    public void controllerReturnsModelAndView() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerControllerRequestHandler extends CrawlerControllerRequestHandler {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }
        when(mockInitialiser.connect(URL)).thenReturn(new ArrayList<>());
        TestCrawlerControllerRequestHandler controller = new TestCrawlerControllerRequestHandler();
        ModelAndView mav = controller.parsePages(URL);
        assertNotNull(mav);
    }

    @Test
    public void controllerReturnsViewOfResultsIfSuccess() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerControllerRequestHandler extends CrawlerControllerRequestHandler {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }
        when(mockInitialiser.connect(URL)).thenReturn(new ArrayList<>());
        TestCrawlerControllerRequestHandler controller = new TestCrawlerControllerRequestHandler();
        ModelAndView mav = controller.parsePages(URL);
        assertEquals("results", mav.getViewName());
    }

    @Test
    public void controllerReturnsListOfPagesInsideModel() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerControllerRequestHandler extends CrawlerControllerRequestHandler {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }
        when(mockInitialiser.connect(URL)).thenReturn(new ArrayList<>());
        TestCrawlerControllerRequestHandler controller = new TestCrawlerControllerRequestHandler();
        ModelAndView mav = controller.parsePages(URL);
        assertNotNull(mav.getModel().get("pages"));
    }

    @Test
    public void controllerCallsToInitialiseCrawler() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerControllerRequestHandler extends CrawlerControllerRequestHandler {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }

        TestCrawlerControllerRequestHandler crawlerController = new TestCrawlerControllerRequestHandler();
        crawlerController.parsePages(URL);
        verify(mockInitialiser, times(1)).connect(URL);
    }

    @Test
    public void controllerReturnsErrorPageIfUnexpectecExceptionIsThrown() throws IOException {
        CrawlInitialiser mockInitialiser = mock(CrawlInitialiser.class);
        class TestCrawlerControllerRequestHandler extends CrawlerControllerRequestHandler {
            @Override
            protected CrawlInitialiser getCrawlInitialiser() {
                return mockInitialiser;
            }
        }
        when(mockInitialiser.connect(anyString())).thenThrow(new IOException());
        TestCrawlerControllerRequestHandler crawlerController = new TestCrawlerControllerRequestHandler();
        ModelAndView mav = crawlerController.parsePages(URL);
        assertEquals("error", mav.getViewName());
    }

}