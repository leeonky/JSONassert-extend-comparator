package com.github.leeonky.jsonassert;

import com.github.leeonky.jsonassert.checker.*;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

import java.util.HashMap;
import java.util.Map;

public class PatternComparator extends DefaultComparator {
    public static final String PREFIX = "**";

    private Map<String, Checker> checkers = new HashMap<>();

    public PatternComparator() {
        this(PREFIX);
    }

    public PatternComparator(String prefix) {
        super(JSONCompareMode.STRICT);
        checkers.put(prefix + "ANY_URL", new URLChecker());
        checkers.put(prefix + "ANY_NATURAL_NUMBER", new NaturalNumberChecker());
        checkers.put(prefix + "ANY_OBJECT", new ObjectChecker());
        checkers.put(prefix + "ANY_UTC", new PatternChecker("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.?\\d*Z", String.class));
    }

    public static PatternComparator defaultPatternComparator() {
        return new PatternComparator();
    }

    @Override
    public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) throws JSONException {
        if (expectedValue instanceof String) {
            Checker checker = checkers.get(((String) expectedValue).split(" ")[0]);
            if (checker != null) {
                checker.verify(prefix, expectedValue, actualValue, result);
                return;
            }
        }
        super.compareValues(prefix, expectedValue, actualValue, result);
    }

}
