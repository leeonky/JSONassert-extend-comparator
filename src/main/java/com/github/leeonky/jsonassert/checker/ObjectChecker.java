package com.github.leeonky.jsonassert.checker;

import org.json.JSONObject;

public class ObjectChecker extends AbstractChecker {
    public ObjectChecker() {
        super(JSONObject.class);
    }

    @Override
    protected boolean isValueMatched(Object actualValue) {
        return true;
    }
}
