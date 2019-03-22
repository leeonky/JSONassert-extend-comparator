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
}
