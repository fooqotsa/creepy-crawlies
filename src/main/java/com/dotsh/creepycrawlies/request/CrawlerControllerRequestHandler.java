package com.dotsh.creepycrawlies.request;

import com.dotsh.creepycrawlies.controller.CrawlerController;
import com.dotsh.creepycrawlies.crawler.CrawlInitialiser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

public class CrawlerControllerRequestHandler {

    public static final String PAGES_KEY = "pages";
    public static final String RESULTS_PAGE = "results";
    public static final String ERROR_PAGE = "error";
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerController.class);

    public ModelAndView parsePages(String href) {
        try {
            return buildModelAndView(href);
        } catch (IOException e) {
            return handleException(e);
        }
    }

    private ModelAndView buildModelAndView(String href) throws IOException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(RESULTS_PAGE);
        mav.addObject(PAGES_KEY, getCrawlInitialiser().connect(href));
        return mav;
    }

    private ModelAndView handleException(IOException e) {
        ModelAndView mav = new ModelAndView();
        LOGGER.error("Crawler failed: " + e.getLocalizedMessage());
        LOGGER.error("returning error page");
        mav.setViewName(ERROR_PAGE);
        return mav;
    }

    protected CrawlInitialiser getCrawlInitialiser() {
        return new CrawlInitialiser();
    }

}
