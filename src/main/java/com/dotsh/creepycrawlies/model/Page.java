package com.dotsh.creepycrawlies.model;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class Page {
    private String title;
    private String url;
    private Set<String> internalUrls;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Set<String> getInternalUrls() {
        return internalUrls;
    }

    public void setInternalUrls(Set<String> internalUrls) {
        this.internalUrls = internalUrls;
    }
}
