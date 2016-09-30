package com.dotsh.creepycrawlies.parser;

import com.dotsh.creepycrawlies.model.Page;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PageParserTest {

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
        Page page = parser.buildFromDocument(document, "wiprodigital.com");
        assertTrue(page.getInternalUrls().contains("http://wiprodigital.com/who-we-are"));
    }
}
