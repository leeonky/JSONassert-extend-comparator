package com.github.leeonky.jsonassert;

import com.github.leeonky.dal.AssertResult;
import com.github.leeonky.dal.DalException;
import com.github.leeonky.dal.DataAssert;
import com.github.leeonky.dal.util.ListAccessor;
import com.github.leeonky.dal.util.PropertyAccessor;
import com.github.leeonky.jsonassert.checker.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

import java.util.*;

public class PatternComparator extends DefaultComparator {
    public static final String PREFIX = "**";
    private final String prefix;
    private DataAssert dataAssert = new DataAssert();
    private Map<String, Checker> checkers = new HashMap<>();

    private PatternComparator(String prefix) {
        super(JSONCompareMode.STRICT);
        this.prefix = prefix;
        checkers.put(prefix + "ANY_URL", new URLChecker());
        checkers.put(prefix + "ANY_NATURAL_NUMBER", new NaturalNumberChecker());
        checkers.put(prefix + "ANY_OBJECT", new ObjectChecker());
        checkers.put(prefix + "ANY_UTC", new PatternChecker("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.?\\d*Z", String.class));

        dataAssert.getCompilingContextBuilder().registerPropertyAccessor(JSONObject.class, new PropertyAccessor<JSONObject>() {
            @Override
            public Object getValue(JSONObject instance, String name) throws Exception {
                return instance.get(name);
            }

            @Override
            public Set<String> getPropertyNames(JSONObject instance) {
                Set<String> set = new HashSet<>();
                Iterator iterator = instance.keys();
                while (iterator.hasNext())
                    set.add(iterator.next().toString());
                return set;
            }
        });

        dataAssert.getCompilingContextBuilder().registerListAccessor(JSONArray.class, new ListAccessor<JSONArray>() {
            @Override
            public Object get(JSONArray jsonArray, int index) {
                try {
                    return jsonArray.get(index);
                } catch (JSONException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            @Override
            public int size(JSONArray jsonArray) {
                return jsonArray.length();
            }
        });
    }

    public static PatternComparator defaultPatternComparator() {
        return new PatternComparator(PREFIX);
    }

    public DataAssert getDataAssert() {
        return dataAssert;
    }

    @Override
    public void compareValues(String prefix, Object expectedValue, Object actualValue, JSONCompareResult result) throws JSONException {
        if (expectedValue instanceof String) {
            String sourceCode = (String) expectedValue;
            Checker checker = checkers.get(sourceCode.split(" ")[0]);
            if (checker != null) {
                checker.verify(prefix, expectedValue, actualValue, result);
                return;
            }
            if (sourceCode.startsWith(this.prefix)) {
                try {
                    AssertResult assertResult = dataAssert.assertData(actualValue, sourceCode.substring(this.prefix.length()));
                    if (!assertResult.isPassed())
                        result.fail(prefix, expectedValue, actualValue);
                    return;
                } catch (DalException e) {
                    throw new RuntimeException(e.getClass().getSimpleName() + ": " + e.getMessage() + "\n" + expectedValue + "\n"
                            + String.join("", Collections.nCopies(e.getPosition() + this.prefix.length(), " ")) + "^");
                }
            }
        }
        super.compareValues(prefix, expectedValue, actualValue, result);
    }

}
