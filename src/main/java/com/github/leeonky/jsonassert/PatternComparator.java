package com.github.leeonky.jsonassert;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

import java.math.BigInteger;
import java.util.regex.Pattern;

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
            if (pattern.equals("**UTC_IN_ISO_8601")) {
                if (!(actualValue instanceof String))
                    result.fail("Type miss matched, expect String but " + actualValue.getClass().getSimpleName());
                else {
                    if (!Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.?\\d*Z").matcher(String.valueOf(actualValue)).matches())
                        result.fail(prefix, expectedValue, actualValue);
                }
                return;
            }
            super.compareValues(prefix, expectedValue, actualValue, result);
        }
    }
}
