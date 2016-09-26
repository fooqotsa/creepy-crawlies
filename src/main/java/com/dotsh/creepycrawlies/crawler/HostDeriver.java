package com.dotsh.creepycrawlies.crawler;

import com.google.common.net.InternetDomainName;

import java.net.MalformedURLException;
import java.net.URL;

public class HostDeriver {

    public String parse(String fullUrl) throws MalformedURLException {
        final URL url = new URL(fullUrl);
        final String host = url.getHost();
        final InternetDomainName name = InternetDomainName.from(host).topPrivateDomain();
        return name.toString();
    }

}
