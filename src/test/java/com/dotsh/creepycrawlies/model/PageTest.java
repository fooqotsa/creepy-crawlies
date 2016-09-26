package com.dotsh.creepycrawlies.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PageTest {

    @Test
    public void pageHasATitle() {
        Page page = new Page();
        page.setTitle("title");
        assertEquals("title", page.getTitle());
    }

    @Test
    public void pageHasAUrl() {
        Page page = new Page();
        page.setUrl("url");
        assertEquals("url", page.getUrl());
    }
}