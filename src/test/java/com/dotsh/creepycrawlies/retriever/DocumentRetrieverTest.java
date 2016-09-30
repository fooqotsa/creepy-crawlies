package com.dotsh.creepycrawlies.retriever;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DocumentRetrieverTest {

    @Test
    public void retrieverThrowsAnIllegalArgumentExceptionIfUrlIsNull() throws IOException {
        class TestRetriever extends DocumentRetriever {
            public Exception exception = null;
            @Override
            protected void handleException(String url, Exception e) {
                exception = e;
            }
        }
        TestRetriever retriever = new TestRetriever();
        retriever.retrieve(null);
        assertTrue(retriever.exception instanceof IllegalArgumentException);
    }

    @Test
    public void retrieverThrowsAnIllegalArgumentExceptionIfUrlIsEmpty() throws IOException {
        class TestRetriever extends DocumentRetriever {
            public Exception exception = null;
            @Override
            protected void handleException(String url, Exception e) {
                exception = e;
            }
        }
        TestRetriever retriever = new TestRetriever();
        retriever.retrieve("");
        assertTrue(retriever.exception instanceof IllegalArgumentException);
    }

    @Test
    public void retrieverReturnsDocumentForParsing() {
        class TestRetriever extends DocumentRetriever {
            @Override
            public Document retrieve(String url) {
                Document document = mock(Document.class);
                when(document.title()).thenReturn("title");
                return document;
            }
        }

        Document doc = new TestRetriever().retrieve("");
        assertEquals("title", doc.title());
    }
}