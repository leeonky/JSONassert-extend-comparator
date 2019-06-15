package com.github.leeonky.jsonassert;

import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.leeonky.jsonassert.PatternComparatorTest.assertExpect;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Nested
    class VerifyVerb {
        @Test
        void check_type() {
            AssertionError error = assertThrows(AssertionError.class, () -> assertExpect("**ANY_OBJECT CONTAINS name", "1"));

            assertThat(error.getMessage()).contains("Type miss matched, expect JSONObject but Integer");
        }

        @Test
        void not_allowed_verb() {
            AssertionError error = assertThrows(AssertionError.class, () -> assertExpect("**ANY_OBJECT HELLO", "{}"));

            assertThat(error.getMessage()).contains("Verb HELLO not allowed for **ANY_OBJECT");
        }

        @Nested
        class ContainsProperties {
            @Test
            void verify_contains_properties() {
                AssertionError error = assertThrows(AssertionError.class, () -> assertExpect("**ANY_OBJECT CONTAINS name", "{}"));

                assertThat(error.getMessage()).contains("Expect prop contains property name");
            }
        }
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
