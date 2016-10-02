package com.dotsh.creepycrawlies.controller;

import com.dotsh.creepycrawlies.crawler.CrawlInitialiser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(path = "/{website}",method = RequestMethod.GET)
    public ModelAndView crawl(@PathVariable("website") String website) {
        final ModelAndView mav = new ModelAndView();
        try {
            mav.setViewName(RESULTS_PAGE);
            mav.addObject(PAGES_KEY, getCrawlInitialiser().connect(website));
            return mav;
        } catch (IOException e) {
            System.out.println("Crawler failed: " + e.getLocalizedMessage());
            System.out.println("returning error page");
            mav.setViewName(ERROR_PAGE);
        }
        return mav;
    }

    protected CrawlInitialiser getCrawlInitialiser() {
        return new CrawlInitialiser();
    }
}
