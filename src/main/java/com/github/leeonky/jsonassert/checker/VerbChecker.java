package com.github.leeonky.jsonassert.checker;

import org.skyscreamer.jsonassert.JSONCompareResult;

public interface VerbChecker {
    String CONTAINS = "CONTAINS";

    boolean isValueMatched(String prefix, Object actualValue, JSONCompareResult result);
}
