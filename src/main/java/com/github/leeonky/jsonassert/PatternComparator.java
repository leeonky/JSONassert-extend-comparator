package com.github.leeonky.jsonassert;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

import java.math.BigInteger;

public class PatternComparator extends DefaultComparator {
    public PatternComparator() {
        super(JSONCompareMode.STRICT);
    }

    public static PatternComparator defaultPatternComparator() {
        return new PatternComparator();
    }

    @Override
    public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) throws JSONException {
        if (expectedValue instanceof String) {
            String pattern = expectedValue.toString();
            if (pattern.equals("**NATURAL_NUMBER")) {
                if (!(actualValue instanceof Number))
                    result.fail("Type miss matched, expect Number but " + actualValue.getClass().getSimpleName());
                else {
                    try {
                        if (new BigInteger(actualValue.toString()).compareTo(BigInteger.ZERO) <= 0)
                            result.fail(prefix, expectedValue, actualValue);
                    } catch (NumberFormatException e) {
                        result.fail(prefix, expectedValue, actualValue);
                    }
                }
                return;
            }
            super.compareValues(prefix, expectedValue, actualValue, result);
        }
    }
}
