package com.dotsh.creepycrawlies.crawler;

import com.dotsh.creepycrawlies.model.Page;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class CrawlerTest {

    @Test
    public void crawlerRetrievesAListOfPagesFromSpecifiedWebsite() throws IOException {
        Crawler crawler = new Crawler();
        List<Page> pages = crawler.connect("http://wiprodigital.com");
        assertNotNull(pages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void crawlerThrowsAnIllegalArgumentExceptionIfUrlIsNull() throws IOException {
        new Crawler().connect(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void crawlerThrowsAnIllegalArgumentExceptionIfUrlIsEmpty() throws IOException {
        new Crawler().connect("");
    }

}
