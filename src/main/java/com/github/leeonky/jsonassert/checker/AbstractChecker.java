package com.github.leeonky.jsonassert.checker;

import org.skyscreamer.jsonassert.JSONCompareResult;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public abstract class AbstractChecker implements Checker {
    private final List<Class<?>> acceptTypes;

    protected AbstractChecker(Class<?>... acceptTypes) {
        this.acceptTypes = asList(acceptTypes);
    }

    @Override
    public void verify(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) {
        if (isValidType(actualValue)) {
            if (!isValueMatched(actualValue))
                result.fail(prefix, expectedValue, actualValue);
        } else
            result.fail("Type miss matched, expect "
                    + acceptTypes.stream().map(Class::getSimpleName).collect(Collectors.joining(", "))
                    + " but " + actualValue.getClass().getSimpleName());
    }

    protected abstract boolean isValueMatched(Object actualValue);

    private boolean isValidType(Object actualValue) {
        return acceptTypes.stream().anyMatch(c -> c.isInstance(actualValue));
    }
}
