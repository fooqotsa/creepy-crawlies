package com.dotsh.creepycrawlies.parser;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class QueryParamParserTest {

    private QueryParamParser paramParser = new QueryParamParser();

    @Test
    public void returnsNullIfHrefIsNull() {
        assertNull(paramParser.stripUrlIfQueryParametersArePresent(null));
    }

    @Test
    public void removesHashFromUrl() throws IOException {
        String urlWithHash = "http://wiprodigital.com/what-we-do#work-three-circles-row";
        String expectedResult = "http://wiprodigital.com/what-we-do";

        assertEquals(expectedResult, paramParser.stripUrlIfQueryParametersArePresent(urlWithHash));
    }

    @Test
    public void doesNotAddQueryStringedUrlsWithQuestionMarkTwice() throws IOException {
        String urlWithQuestionMark = "http://wiprodigital.com/what-we-do?value=work-three-circles-row";
        String expectedResult = "http://wiprodigital.com/what-we-do";

        assertEquals(expectedResult, paramParser.stripUrlIfQueryParametersArePresent(urlWithQuestionMark));
    }

    @Test
    public void doesNotAddQueryStringedUrlsWithQuestionMarkAndHashTwice() throws IOException {
        String urlWithQuestionMarkAndHash = "http://wiprodigital.com/what-we-do?value#=work-three-circles-row";
        String expectedResult = "http://wiprodigital.com/what-we-do";
        assertEquals(expectedResult, paramParser.stripUrlIfQueryParametersArePresent(urlWithQuestionMarkAndHash));
    }

    @Test
    public void doesNotAddQueryStringedUrlsWithQuestionMarkAndHashReversedTwice() throws IOException {
        String urlWithHashAndQuestionMark = "http://wiprodigital.com/what-we-do#value?=work-three-circles-row";
        String expectedResult = "http://wiprodigital.com/what-we-do";
        assertEquals(expectedResult, paramParser.stripUrlIfQueryParametersArePresent(urlWithHashAndQuestionMark));
    }

    @Test
    public void removesLeadingSlashIfUrlContainsSlashAtFrontOfUrl() {
        String urlWithLeadingSlash = "http://google.com/";
        String expectedResult = "http://google.com";

        assertEquals(expectedResult, paramParser.stripUrlIfQueryParametersArePresent(urlWithLeadingSlash));
    }

    @Test
    public void maintainsUrlIfNoLeadingSlash() {
        String expectedResult = "http://google.com";

        assertEquals(expectedResult, paramParser.stripUrlIfQueryParametersArePresent(expectedResult));
    }
}