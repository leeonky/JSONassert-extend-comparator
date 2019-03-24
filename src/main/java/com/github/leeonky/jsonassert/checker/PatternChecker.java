package com.github.leeonky.jsonassert.checker;

import java.util.regex.Pattern;

public class PatternChecker extends AbstractChecker implements Checker {

    private final String pattern;

    public PatternChecker(String pattern, Class<?>... types) {
        super(types);
        this.pattern = pattern;
    }

    @Override
    protected boolean isValueMatched(Object actualValue) {
        return Pattern.compile(pattern).matcher(String.valueOf(actualValue)).matches();
    }
}
