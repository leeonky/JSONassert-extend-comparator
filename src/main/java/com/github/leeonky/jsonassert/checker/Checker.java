package com.github.leeonky.jsonassert.checker;

import org.skyscreamer.jsonassert.JSONCompareResult;

public interface Checker {

    void verify(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result);
}
