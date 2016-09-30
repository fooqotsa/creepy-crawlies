package com.dotsh.creepycrawlies.parser;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static com.dotsh.creepycrawlies.parser.PageParser.HREF_ATTRIBUTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UrlParserTest {

    public static final String HOST_URL = "wiprodigital.com";
    public static final String WIPRO_HOMEPAGE = "http://wiprodigital.com";
    public static final String STATIC_CONTENT_SAMPLE = "http://17776-presscdn-0-6.pagely.netdna-cdn.com/wp-content/themes/wiprodigital/images/wdlogo.png";

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

    @Test
    public void addsInternalUrlFromSubDomain() throws IOException {
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement);

        when(attributes.get(HREF_ATTRIBUTE)).thenReturn("http://www.subdomain.wiprodigital.com");
        when(navElement.attributes()).thenReturn(attributes);

        Set<String> urls = parser.retrieveInternalUrlsFromLinkElements(HOST_URL, elements);
        assertEquals(1, urls.size());
    }

    @Test
    public void doesNotAddToListOfInternalUrlsIfUrlIsNotAtSameDomain() throws IOException {
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(attributes.get("href")).thenReturn("http://externalSite.com");
        when(navElement.attributes()).thenReturn(attributes);

        Set<String> urls = parser.retrieveInternalUrlsFromLinkElements(HOST_URL, elements);
        assertEquals(0, urls.size());
    }

    @Test
    public void onlyAddsMultipleDifferentUrls() throws IOException {
        Element navElement = mock(Element.class);
        Element navElement2 = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        Attributes attributes2 = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement2);

        when(attributes.get(HREF_ATTRIBUTE)).thenReturn(WIPRO_HOMEPAGE);
        when(attributes2.get(HREF_ATTRIBUTE)).thenReturn(WIPRO_HOMEPAGE + "/other");
        when(navElement.attributes()).thenReturn(attributes);
        when(navElement2.attributes()).thenReturn(attributes2);

        Set<String> urls = parser.retrieveInternalUrlsFromLinkElements(HOST_URL, elements);
        assertEquals(2, urls.size());
    }

    @Test
    public void addsExternalUrlsToPage() throws IOException {
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("http://google.com");
        when(navElement.attributes()).thenReturn(attributes);
        Set<String> urls = parser.retrieveExternalUrlsFromLinkElements(HOST_URL, elements);
        assertEquals(1, urls.size());
    }

    @Test
    public void doesNotAddToListOfExternalUrlsIfUrlIsNull() throws IOException {
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(attributes.get("href")).thenReturn(null);
        when(navElement.attributes()).thenReturn(attributes);
        Set<String> urls = parser.retrieveExternalUrlsFromLinkElements(HOST_URL, elements);
        assertTrue(urls.isEmpty());
    }

    @Test
    public void onlyAddsMultipleDifferentExternalUrls() throws IOException {
        Element navElement = mock(Element.class);
        Element navElement2 = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        Attributes attributes2 = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement2);

        when(attributes.get(HREF_ATTRIBUTE)).thenReturn("www.google.com");
        when(attributes2.get(HREF_ATTRIBUTE)).thenReturn("www.google.com/other");
        when(navElement.attributes()).thenReturn(attributes);
        when(navElement2.attributes()).thenReturn(attributes2);

        Set<String> urls = parser.retrieveExternalUrlsFromLinkElements(HOST_URL, elements);
        assertEquals(2, urls.size());
    }

    @Test
    public void doesNotAddHashLinksToExternalUrls() throws IOException {
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("#pageFocus");
        when(navElement.attributes()).thenReturn(attributes);
        Set<String> urls = parser.retrieveExternalUrlsFromLinkElements(HOST_URL, elements);
        assertEquals(0, urls.size());
    }

    @Test
    public void doesNotAddStaticContentIfNull() throws IOException {
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("src")).thenReturn(null);
        when(navElement.attributes()).thenReturn(attributes);
        Set<String> urls = parser.retrieveStaticContentFromElements(elements);
        assertEquals(0, urls.size());
    }

    @Test
    public void doesNotAddStaticContentIfEmpty() throws IOException {
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("src")).thenReturn("");
        when(navElement.attributes()).thenReturn(attributes);
        Set<String> urls = parser.retrieveStaticContentFromElements(elements);
        assertEquals(0, urls.size());
    }

    @Test
    public void addsStaticContentToPage() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("src")).thenReturn(STATIC_CONTENT_SAMPLE);
        when(navElement.attributes()).thenReturn(attributes);
        Set<String> urls = parser.retrieveStaticContentFromElements(elements);
        assertEquals(1, urls.size());
    }

    @Test
    public void doesNotAddToListOfInternalUrlsIfUrlIsEmpty() throws IOException {
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("");
        when(navElement.attributes()).thenReturn(attributes);
        Set<String> urls = parser.retrieveInternalUrlsFromLinkElements(HOST_URL, elements);
        assertEquals(0, urls.size());
    }


}
