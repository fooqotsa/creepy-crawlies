package com.dotsh.creepycrawlies.controller;

import com.dotsh.creepycrawlies.request.CrawlerControllerRequestHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class CrawlerController {

    public static final String URL_TO_PARSE = "http://wiprodigital.com";

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView crawl() {
        return handleRequest();
    }

    private ModelAndView handleRequest() {
        return initialiseRequestHandler().parsePages(URL_TO_PARSE);
    }

    protected CrawlerControllerRequestHandler initialiseRequestHandler() {
        return new CrawlerControllerRequestHandler();
    }

}
