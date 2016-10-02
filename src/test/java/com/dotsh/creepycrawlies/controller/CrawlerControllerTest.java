package com.dotsh.creepycrawlies.controller;

import com.dotsh.creepycrawlies.model.Page;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class CrawlerControllerTest {

    @Test
    public void controllerReturnsListOfPagesFound() {
        CrawlerController controller = new CrawlerController();
        List<Page> pages = controller.crawl("http://wiprodigital.com");
        assertNotNull(pages);
    }

}
