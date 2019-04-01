package com.github.leeonky.jsonassert;

import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

class PatternComparatorTest {

    private void assertExpect(final String expect, final String actualValue) throws JSONException {
        assertEquals("{\"prop\":\"" + expect + "\"}", "{\"prop\":" + actualValue + "}",
                PatternComparator.defaultPatternComparator());
    }

    @Nested
    class VerifyDefault {
        @Test
        void assert_by_default() throws JSONException {
            assertEquals("{\"prop\":1}", "{\"prop\":" + "1" + "}",
                    PatternComparator.defaultPatternComparator());
        }
    }

    @Nested
    class VerifyNatureNumber {

        @Test
        void actual_type_should_be_integer() {
            AssertionError error = assertThrows(AssertionError.class, () ->
                    assertExpect("**ANY_NATURAL_NUMBER", "\"1\""));

            assertThat(error.getMessage()).contains("Type miss matched, expect Number but String");
        }

        @Test
        void actual_value_should_be_nature_number() {
            assertThrows(AssertionError.class, () -> assertExpect("**ANY_NATURAL_NUMBER", "0"));
            assertThrows(AssertionError.class, () -> assertExpect("**ANY_NATURAL_NUMBER", "-1"));
            assertThrows(AssertionError.class, () -> assertExpect("**ANY_NATURAL_NUMBER", "1.0"));
        }

        @Test
        void assert_ok() throws JSONException {
            assertExpect("**ANY_NATURAL_NUMBER", "1");
        }
    }

    @Nested
    class VerifyInstant {

        @Test
        void actual_type_should_be_string() {
            AssertionError error = assertThrows(AssertionError.class, () -> assertExpect("**ANY_UTC", "1"));

            assertThat(error.getMessage()).contains("Type miss matched, expect String but Integer");
        }

        @Test
        void actual_value_should_be_instant_in_iso8601() {
            assertThrows(AssertionError.class, () -> assertExpect("**ANY_UTC", "\"abcd\""));
        }

        @Test
        void assert_ok() throws JSONException {
            assertExpect("**ANY_UTC", "\"2001-10-11T12:11:19Z\"");
            assertExpect("**ANY_UTC", "\"2001-10-11T12:11:19.111Z\"");
        }
    }


    @Nested
    class VerifyObject {

        @Test
        void actual_type_should_be_string() {
            AssertionError error = assertThrows(AssertionError.class, () -> assertExpect("**ANY_OBJECT", "1"));

            assertThat(error.getMessage()).contains("Type miss matched, expect JSONObject but Integer");
        }

        @Test
        void assert_ok() throws JSONException {
            assertExpect("**ANY_OBJECT", "{\"a\":1}");
        }
    }

    @Nested
    class VerifyURL {

        @Test
        void actual_type_should_be_string() {
            AssertionError error = assertThrows(AssertionError.class, () -> assertExpect("**ANY_URL", "1"));

            assertThat(error.getMessage()).contains("Type miss matched, expect String but Integer");
        }

        @Test
        void assert_failed_because_of_invalid_url() {
            assertThrows(AssertionError.class, () -> assertExpect("**ANY_URL", "www.baidu.com"));
        }

        @Test
        void assert_ok() throws JSONException {
            assertExpect("**ANY_URL", "\"http://www.baidu.com\"");
        }
    }
}
