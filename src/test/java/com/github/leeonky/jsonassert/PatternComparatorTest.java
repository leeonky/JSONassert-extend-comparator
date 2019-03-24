package com.github.leeonky.jsonassert;

import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

class PatternComparatorTest {
    @Nested
    class VerifyNatureNumber {

        @Test
        void actual_type_should_be_integer() {
            AssertionError error = assertThrows(AssertionError.class, () ->
                    assertNaturalNumber("\"1\""));

            assertThat(error.getMessage()).contains("Type miss matched, expect Number but String");
        }

        private void assertNaturalNumber(final String actualValue) throws JSONException {
            assertEquals("{\"prop\":\"**NATURAL_NUMBER\"}", "{\"prop\":" + actualValue + "}",
                    PatternComparator.defaultPatternComparator());
        }

        @Test
        void actual_value_should_be_nature_number() {
            assertThrows(AssertionError.class, () -> assertNaturalNumber("0"));
            assertThrows(AssertionError.class, () -> assertNaturalNumber("-1"));
            assertThrows(AssertionError.class, () -> assertNaturalNumber("1.0"));
        }

        @Test
        void assert_ok() throws JSONException {
            assertNaturalNumber("1");
        }
    }

    @Nested
    class VerifyInstant {

        @Test
        void actual_type_should_be_string() {
            AssertionError error = assertThrows(AssertionError.class, () ->
                    assertUTCTimestamp("1"));

            assertThat(error.getMessage()).contains("Type miss matched, expect String but Integer");
        }

        private void assertUTCTimestamp(final String value) throws JSONException {
            assertEquals("{\"prop\":\"**UTC_IN_ISO_8601\"}", "{\"prop\":" + value + "}",
                    PatternComparator.defaultPatternComparator());
        }

        @Test
        void actual_value_should_be_instant_in_iso8601() {
            assertThrows(AssertionError.class, () -> assertUTCTimestamp("\"abcd\""));
        }

        @Test
        void assert_ok() throws JSONException {
            assertUTCTimestamp("\"2001-10-11T12:11:19Z\"");
            assertUTCTimestamp("\"2001-10-11T12:11:19.111Z\"");
        }
    }
}
