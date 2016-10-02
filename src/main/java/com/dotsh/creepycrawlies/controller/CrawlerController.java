package com.dotsh.creepycrawlies.controller;

import com.dotsh.creepycrawlies.crawler.CrawlInitialiser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class CrawlerController {

    public static final String PAGES_KEY = "pages";
    public static final String RESULTS_PAGE = "results";
    public static final String ERROR_PAGE = "error";
    public static final String URL = "http://wiprodigital.com";
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView crawl() {
        final ModelAndView mav = new ModelAndView();
        try {
            mav.setViewName(RESULTS_PAGE);
            mav.addObject(PAGES_KEY, getCrawlInitialiser().connect(URL));
            return mav;
        } catch (IOException e) {
            LOGGER.error("Crawler failed: " + e.getLocalizedMessage());
            LOGGER.error("returning error page");
            mav.setViewName(ERROR_PAGE);
        }
        return mav;
    }

    protected CrawlInitialiser getCrawlInitialiser() {
        return new CrawlInitialiser();
    }
}
