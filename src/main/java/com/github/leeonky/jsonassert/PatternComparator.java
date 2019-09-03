package com.github.leeonky.jsonassert;

import com.github.leeonky.dal.AssertResult;
import com.github.leeonky.dal.DalException;
import com.github.leeonky.dal.DataAssert;
import com.github.leeonky.dal.util.ListAccessor;
import com.github.leeonky.dal.util.PropertyAccessor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PatternComparator extends DefaultComparator {
    public static final String PREFIX = "**";
    private final String prefix;
    private DataAssert dataAssert = new DataAssert();

    private PatternComparator(String prefix) {
        super(JSONCompareMode.STRICT);
        this.prefix = prefix;

        dataAssert.getRuntimeContextBuilder().registerPropertyAccessor(JSONObject.class, new PropertyAccessor<JSONObject>() {
            @Override
            public Object getValue(JSONObject instance, String name) {
                try {
                    return instance.has(name) ? instance.get(name) : JSONObject.NULL;
                } catch (JSONException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            @Override
            public Set<String> getPropertyNames(JSONObject instance) {
                Set<String> set = new HashSet<>();
                Iterator iterator = instance.keys();
                while (iterator.hasNext())
                    set.add(iterator.next().toString());
                return set;
            }

            @Override
            public boolean isNull(JSONObject instance) {
                return instance == null || instance.equals(JSONObject.NULL);
            }
        });

        dataAssert.getRuntimeContextBuilder().registerListAccessor(JSONArray.class, new ListAccessor<JSONArray>() {
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
            String trim = sourceCode.trim();
            if (trim.startsWith(this.prefix)) {
                try {
                    AssertResult assertResult = dataAssert.assertData(actualValue, trim.substring(this.prefix.length()));
                    if (!assertResult.isPassed())
                        result.fail(prefix, expectedValue, String.valueOf(actualValue));
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
