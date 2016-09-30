package com.dotsh.creepycrawlies.parser;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class QueryParamParserTest {

    @Test
    public void removesHashFromUrl() throws IOException {
        String urlWithHash = "http://wiprodigital.com/what-we-do#work-three-circles-row";
        String expectedResult = "http://wiprodigital.com/what-we-do";
        QueryParamParser paramParser = new QueryParamParser();
        assertEquals(expectedResult, paramParser.stripUrlIfQueryParametersArePresent(urlWithHash));
    }


}