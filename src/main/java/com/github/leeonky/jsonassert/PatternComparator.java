package com.github.leeonky.jsonassert;

import com.github.leeonky.jsonassert.checker.Checker;
import com.github.leeonky.jsonassert.checker.NaturalNumberChecker;
import com.github.leeonky.jsonassert.checker.PatternChecker;
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
        checkers.put(prefix + "NATURAL_NUMBER", new NaturalNumberChecker());
        checkers.put(prefix + "UTC_IN_ISO_8601", new PatternChecker("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.?\\d*Z", String.class));
    }

    public static PatternComparator defaultPatternComparator() {
        return new PatternComparator();
    }

    @Override
    public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) throws JSONException {
        if (expectedValue instanceof String) {
            Checker checker = checkers.get(expectedValue);
            if (checker != null)
                checker.verify(prefix, expectedValue, actualValue, result);
            else
                super.compareValues(prefix, expectedValue, actualValue, result);
        }
    }

}
