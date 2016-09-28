package com.dotsh.creepycrawlies.parser;

public class QueryParamParser {

    public static final String HASH_QUERY_STRING = "#";
    public static final int URL_FIRST_SECTION = 0;
    public static final String QUESTION_MARK_SPLITTER = "\\?";
    public static final String QUESTION_MARK_QUERY_STRING = "?";

    public String stripUrlIfQueryParametersArePresent(String href) {
        if (href != null) {
            href = stripOutHash(href);
            href = stripOutQuestionMark(href);
        }
        return href;
    }

    private String stripOutHash(String href) {
        if (href.contains(HASH_QUERY_STRING)) {
            href = href.split(HASH_QUERY_STRING)[URL_FIRST_SECTION];
        }
        return href;
    }

    private String stripOutQuestionMark(String href) {
        if (href.contains(QUESTION_MARK_QUERY_STRING)) {
            href = href.split(QUESTION_MARK_SPLITTER)[URL_FIRST_SECTION];
        }
        return href;
    }

}
