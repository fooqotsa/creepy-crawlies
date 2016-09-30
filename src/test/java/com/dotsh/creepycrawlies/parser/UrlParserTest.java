package com.dotsh.creepycrawlies.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlParserTest {

    public static final String HOST_URL = "wiprodigital.com";

    private UrlParser parser = new UrlParser();

    @Test
    public void doesNotAddToListOfInternalUrlsIfUrlIsNull() throws IOException {
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(attributes.get("href")).thenReturn(null);
        when(navElement.attributes()).thenReturn(attributes);
        Set<String> urls = parser.retrieveInternalUrlsFromLinkElements(HOST_URL, elements);
        assertTrue(urls.isEmpty());
    }

}
