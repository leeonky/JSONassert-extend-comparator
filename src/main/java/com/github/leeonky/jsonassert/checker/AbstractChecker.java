package com.github.leeonky.jsonassert.checker;

import com.github.leeonky.jsonassert.checker.verb.VerbContain;
import org.skyscreamer.jsonassert.JSONCompareResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public abstract class AbstractChecker implements Checker {
    private final List<Class<?>> acceptTypes;
    protected Set<String> allowVerbs = new HashSet<>();

    protected AbstractChecker(Class<?>... acceptTypes) {
        this.acceptTypes = asList(acceptTypes);
    }

    @Override
    public void verify(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) {
        if (isValidType(actualValue)) {
            VerbChecker verbChecker = buildVerbChecker(expectedValue, result);
            if (!(verbChecker != null && isValueMatched(actualValue) && verbChecker.isValueMatched(prefix, actualValue, result)))
                result.fail(prefix, expectedValue, actualValue);
        } else
            result.fail("Type miss matched, expect "
                    + acceptTypes.stream().map(Class::getSimpleName).collect(Collectors.joining(", "))
                    + " but " + actualValue.getClass().getSimpleName());
    }

    protected boolean isValueMatched(Object actualValue) {
        return true;
    }

    private boolean isValidType(Object actualValue) {
        return acceptTypes.stream().anyMatch(c -> c.isInstance(actualValue));
    }

    protected VerbChecker buildVerbChecker(Object expectedValue, JSONCompareResult result) {
        String[] strings = expectedValue.toString().split(" ", 3);
        if (strings.length > 1) {
            String verb = strings[1];
            if (allowVerbs.contains(verb))
                return createVerbChecker(verb, strings.length > 2 ? strings[2] : "");
            result.fail("Verb " + verb + " not allowed for " + strings[0]);
            return null;
        }
        return (x, y, z) -> true;
    }

    protected VerbContain createVerbChecker(String verb, String args) {
        switch (verb) {
            case VerbChecker.CONTAINS:
                return new VerbContain(args);
            default:
                throw new IllegalArgumentException("Unsupport verb " + verb);
        }
    }

}

