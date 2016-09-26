package com.dotsh.creepycrawlies.crawler;

import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

public class HostDeriverTest {

    public static final String HOST_NAME = "wiprodigital.com";

    @Test
    public void derivesDomainNameFromUrl() throws MalformedURLException {
        String domainName = new HostDeriver().parse("http://wiprodigital.com");
        assertEquals(HOST_NAME, domainName);
    }

    @Test
    public void derivesDomainNameWithWwwAsPrefix() throws MalformedURLException {
        String domainName = new HostDeriver().parse("http://www.wiprodigital.com");
        assertEquals(HOST_NAME, domainName);
    }

    @Test
    public void derivesDomainNameWithSubDomain() throws MalformedURLException {
        String domainName = new HostDeriver().parse("http://sub.wiprodigital.com");
        assertEquals(HOST_NAME, domainName);
    }

    @Test
    public void derivesDomainNameWithSubDomainAndWwwPrefix() throws MalformedURLException {
        String domainName = new HostDeriver().parse("http://www.sub.wiprodigital.com");
        assertEquals(HOST_NAME, domainName);
    }

    @Test(expected = MalformedURLException.class)
    public void throwsMalformedURLExceptionIfUrlIsMalformed() throws MalformedURLException {
        new HostDeriver().parse("httrp://wiprodigital.com");
    }

    @Test(expected = MalformedURLException.class)
    public void throwsMalformedURLExceptionIfUrlIsNull() throws MalformedURLException {
        new HostDeriver().parse(null);
    }

    @Test(expected = MalformedURLException.class)
    public void throwsMalformedURLExceptionIfUrlIsEmpty() throws MalformedURLException {
        new HostDeriver().parse("");
    }
}