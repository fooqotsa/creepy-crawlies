package com.dotsh.creepycrawlies.parser;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

import static com.dotsh.creepycrawlies.parser.PageParser.HREF_ATTRIBUTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PageParserTest {

    private static final String WIPRO_HOMEPAGE = "http://wiprodigital.com";
    public static final String HOST_URL = "wiprodigital.com";
    public static final String STATIC_CONTENT_SAMPLE = "http://17776-presscdn-0-6.pagely.netdna-cdn.com/wp-content/themes/wiprodigital/images/wdlogo.png";

    @Test
    public void retrievesAListOfInternalUrlsOnCurrentPageAndAddsThemToPageObject() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("http://wiprodigital.com/who-we-are");
        when(navElement.attributes()).thenReturn(attributes);

        PageParser parser = new PageParser();
        Page page = parser.buildFromDocument(document, HOST_URL);
        assertTrue(page.getInternalUrls().contains("http://wiprodigital.com/who-we-are"));
    }

    @Test
    public void addsInternalUrlFromSubDomain() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get(HREF_ATTRIBUTE)).thenReturn("http://www.subdomain.wiprodigital.com");
        when(navElement.attributes()).thenReturn(attributes);

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getInternalUrls().size());
    }

    @Test
    public void addsExternalUrlsToPage() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("http://google.com");
        when(navElement.attributes()).thenReturn(attributes);

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getExternalUrls().size());
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

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getStaticContent().size());
    }

    @Test
    public void doesNotAttemptToAddUrlsToPageIfAttributesIsNull() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        elements.add(0, navElement);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(navElement.attributes()).thenReturn(null);

        assertEquals(0, new PageParser().buildFromDocument(document, HOST_URL).getInternalUrls().size());
    }

    @Test
    public void onlyAddsOneOfTheSameUrl() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get(HREF_ATTRIBUTE)).thenReturn(WIPRO_HOMEPAGE);
        when(navElement.attributes()).thenReturn(attributes);

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getInternalUrls().size());
    }

    @Test
    public void doesNotAddQueryStringedUrlsWithQuestionMarkAndHashTwice() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Element navElement2 = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        Attributes attributes2 = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement2);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("http://wiprodigital.com/what-we-do?value#=work-three-circles-row");
        when(attributes2.get("href")).thenReturn("http://wiprodigital.com/what-we-do");
        when(navElement.attributes()).thenReturn(attributes);
        when(navElement2.attributes()).thenReturn(attributes2);

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getInternalUrls().size());
    }

    @Test
    public void doesNotAddQueryStringedUrlsWithQuestionMarkAndHashReversedTwice() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Element navElement2 = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        Attributes attributes2 = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement2);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("http://wiprodigital.com/what-we-do#value?=work-three-circles-row");
        when(attributes2.get("href")).thenReturn("http://wiprodigital.com/what-we-do");
        when(navElement.attributes()).thenReturn(attributes);
        when(navElement2.attributes()).thenReturn(attributes2);

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getInternalUrls().size());
    }

    @Test
    public void doesNotAddQueryStringedUrlsTwiceForExternalUrls() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Element navElement2 = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        Attributes attributes2 = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement2);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("http://google.com/what-we-do#work-three-circles-row");
        when(attributes2.get("href")).thenReturn("http://google.com/what-we-do");
        when(navElement.attributes()).thenReturn(attributes);
        when(navElement2.attributes()).thenReturn(attributes2);

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getExternalUrls().size());
    }

    @Test
    public void doesNotAddQueryStringedUrlsWithQuestionMarkTwiceExternalUrls() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Element navElement2 = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        Attributes attributes2 = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement2);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("http://google.com/what-we-do?value=work-three-circles-row");
        when(attributes2.get("href")).thenReturn("http://google.com/what-we-do");
        when(navElement.attributes()).thenReturn(attributes);
        when(navElement2.attributes()).thenReturn(attributes2);

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getExternalUrls().size());
    }

    @Test
    public void doesNotAddQueryStringedUrlsWithQuestionMarkAndHashTwiceExternalUrls() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Element navElement2 = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        Attributes attributes2 = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement2);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("http://google.com/what-we-do?value#=work-three-circles-row");
        when(attributes2.get("href")).thenReturn("http://google.com/what-we-do");
        when(navElement.attributes()).thenReturn(attributes);
        when(navElement2.attributes()).thenReturn(attributes2);

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getExternalUrls().size());
    }

    @Test
    public void doesNotAddQueryStringedUrlsWithQuestionMarkAndHashReversedTwiceExternalUrls() throws IOException {
        Document document = mock(Document.class);
        Element topLevelElement = mock(Element.class);
        Element navElement = mock(Element.class);
        Element navElement2 = mock(Element.class);
        Elements elements = new Elements();
        Attributes attributes = mock(Attributes.class);
        Attributes attributes2 = mock(Attributes.class);
        elements.add(0, navElement);
        elements.add(1, navElement2);

        when(document.body()).thenReturn(topLevelElement);
        when(topLevelElement.select(anyString())).thenReturn(elements);
        when(attributes.get("href")).thenReturn("http://google.com/what-we-do#value?=work-three-circles-row");
        when(attributes2.get("href")).thenReturn("http://google.com/what-we-do");
        when(navElement.attributes()).thenReturn(attributes);
        when(navElement2.attributes()).thenReturn(attributes2);

        assertEquals(1, new PageParser().buildFromDocument(document, HOST_URL).getExternalUrls().size());
    }

}
