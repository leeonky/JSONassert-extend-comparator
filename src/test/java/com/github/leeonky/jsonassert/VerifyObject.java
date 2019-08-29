package com.github.leeonky.jsonassert;

import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@Nested
class VerifyObject {
    static void assertExpect(final String expect, final String actualValue) throws JSONException {
        assertEquals("{\"prop\":\"" + expect + "\"}", "{\"prop\":" + actualValue + "}",
                PatternComparator.defaultPatternComparator());
    }

    @Nested
    class DALVerify {

        @Test
        void verify_pass() throws JSONException {
            assertExpect("**is URL which .protocol='http'", "\"http://www.baidu.com\"");
        }

        @Test
        void get_syntax_error() {
            RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> assertExpect("** = '", "\"a\""));

            assertThat(runtimeException.getMessage()).contains("SyntaxException: string should end with '''\n** = '\n      ^");
        }

        @Test
        void verify_json_data() throws JSONException {
            assertExpect("** .name='tom'", "{\"name\": \"tom\"}");
        }

        @Test
        void verify_json_array() throws JSONException {
            assertExpect("** is List which .size=2 and [0]=1", "[1, 2]");
        }
    }
}
