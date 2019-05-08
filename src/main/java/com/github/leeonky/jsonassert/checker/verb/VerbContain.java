package com.github.leeonky.jsonassert.checker.verb;

import com.github.leeonky.jsonassert.checker.VerbChecker;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareResult;

import static java.util.Arrays.asList;

public class VerbContain implements VerbChecker {
    private final String args;

    public VerbContain(String args) {
        this.args = args;
    }

    @Override
    public boolean isValueMatched(String prefix, Object actualValue, JSONCompareResult result) {
        if (actualValue instanceof JSONObject) {
            return isValueMatched(prefix, (JSONObject) actualValue, result, args);
        }
        return false;
    }

    private boolean isValueMatched(String prefix, JSONObject jsonObject, JSONCompareResult result, String args) {
        return asList(args.split(",")).stream().map(PropertyChecker::new)
                .filter(p -> !p.matched(jsonObject))
                .peek(p -> p.outputFail(prefix, result))
                .count() == 0;
    }

    static class PropertyChecker {
        private final String property;

        PropertyChecker(String exp) {
            property = exp.trim();
        }

        boolean matched(JSONObject object) {
            return object.has(property);
        }

        void outputFail(String prefix, JSONCompareResult result) {
            result.fail("Expect " + prefix + " contains property " + property);
        }
    }
}
