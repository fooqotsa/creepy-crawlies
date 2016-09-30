package com.dotsh.creepycrawlies.model;

import java.util.Set;

public class Page {
    private String title;
    private String url;
    private Set<String> internalUrls;
    private Set<String> externalUrls;
    private Set<String> staticContent;

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

    public Set<String> getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(Set<String> externalUrls) {
        this.externalUrls = externalUrls;
    }

    public Set<String> getStaticContent() {
        return staticContent;
    }

    public void setStaticContent(Set<String> staticContent) {
        this.staticContent = staticContent;
    }
}
