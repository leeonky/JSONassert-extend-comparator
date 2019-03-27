package com.github.leeonky.jsonassert.checker;

import java.net.MalformedURLException;
import java.net.URL;

public class URLChecker extends AbstractChecker {
    public URLChecker() {
        super(String.class);
    }

    @Override
    protected boolean isValueMatched(Object actualValue) {
        try {
            new URL(String.valueOf(actualValue));
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
