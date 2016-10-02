package com.dotsh.creepycrawlies.controller;

import com.dotsh.creepycrawlies.request.CrawlerControllerRequestHandler;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CrawlerControllerTest {

    @Test
    public void makesCallToCrawlerControllerRequestHandlerToDealWithIncomingRequests() {
        CrawlerControllerRequestHandler mockHandler = mock(CrawlerControllerRequestHandler.class);
        class TestController extends CrawlerController {
            @Override
            protected CrawlerControllerRequestHandler initialiseRequestHandler() {
                return mockHandler;
            }
        }
        when(mockHandler.parsePages(any())).thenReturn(new ModelAndView());
        assertNotNull(new TestController().crawl());
        verify(mockHandler, times(1)).parsePages(any());
    }

}
