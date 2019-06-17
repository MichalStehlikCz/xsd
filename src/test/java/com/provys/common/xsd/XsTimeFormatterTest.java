package com.provys.common.xsd;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalTime;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class XsTimeFormatterTest {

    private static final Pattern strictTimePattern = Pattern.compile(XsTimeFormatter.STRICT_TIME_REGEX);

    @Nonnull
    static Stream<Object[]> getStrictTimeRegexTest() {
        return Stream.of(
                new Object[]{"00:00:00", true}
                , new Object[]{"12:17:15", true}
                , new Object[]{"15:07:56.2354", true}
                , new Object[]{"24:00:00", true}
                , new Object[]{"15:10", false}
                , new Object[]{"8:17:15", false}
                , new Object[]{"24:01:15", false}
                , new Object[]{"31:02:15", false}
                , new Object[]{"08:60:48", false}
                , new Object[]{"07:06:84", false}
                , new Object[]{"16", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStrictTimeRegexTest(String value, boolean match) {
        assertThat(strictTimePattern.matcher(value).matches()).isEqualTo(match);
    }

    @Nonnull
    static Stream<Object[]> getStrictPatternTest() {
        return Stream.of(
                new Object[]{"00:00:00", true}
                , new Object[]{"12:17:15", true}
                , new Object[]{"15:07:56.2354+01:00", true}
                , new Object[]{"24:00:00Z", true}
                , new Object[]{"15:10", false}
                , new Object[]{"8:17:15", false}
                , new Object[]{"12:17:15z", false}
                , new Object[]{"12:17:15+01:00:00", false}
                , new Object[]{"24:01:15", false}
                , new Object[]{"31:02:15", false}
                , new Object[]{"08:60:48", false}
                , new Object[]{"07:06:84", false}
                , new Object[]{"16", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStrictPatternTest(String value, boolean match) {
        assertThat(XsTimeFormatter.STRICT_PATTERN.matcher(value).matches()).isEqualTo(match);
    }

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"00:00:00", LocalTime.of(0, 0, 0)}
                , new Object[]{"12:17:15", LocalTime.of(12, 17, 15)}
                , new Object[]{"15:07:56.2354+01:00", LocalTime.of(15, 7, 56, 235400000)}
                , new Object[]{"24:00:00Z", LocalTime.of(0, 0, 0)}
                , new Object[]{"15:10", null}
                , new Object[]{"8:17:15", null}
                , new Object[]{"12:17:15z", null}
                , new Object[]{"12:17:15+01:00:00", null}
                , new Object[]{"24:01:15", null}
                , new Object[]{"31:02:15", null}
                , new Object[]{"08:60:48", null}
                , new Object[]{"07:06:84", null}
                , new Object[]{"16", null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String value, @Nullable LocalTime expected) {
        if (expected != null) {
            assertThat(XsTimeFormatter.parse(value)).isEqualTo(expected);
        } else {
            assertThatThrownBy(() -> XsTimeFormatter.parse(value));
        }
    }

    private static final Pattern lenientTimePattern = Pattern.compile(XsTimeFormatter.LENIENT_TIME_REGEX);

    @Nonnull
    static Stream<Object[]> getLenientTimeRegexTest() {
        return Stream.of(
                new Object[]{"00:00:00", true}
                , new Object[]{"12:17:15", true}
                , new Object[]{"15:07:56.2354", true}
                , new Object[]{"24:00:00", true}
                , new Object[]{"15:10", true}
                , new Object[]{"8:17:15", true}
                , new Object[]{"24:01:15", false}
                , new Object[]{"31:02:15", false}
                , new Object[]{"08:60:48", false}
                , new Object[]{"07:06:84", false}
                , new Object[]{"16", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getLenientTimeRegexTest(String value, boolean match) {
        assertThat(lenientTimePattern.matcher(value).matches()).isEqualTo(match);
    }

    @Nonnull
    static Stream<Object[]> getLenientPatternTest() {
        return Stream.of(
                new Object[]{"00:00:00", true}
                , new Object[]{"12:17:15", true}
                , new Object[]{"15:07:56.2354+01:00", true}
                , new Object[]{"24:00:00Z", true}
                , new Object[]{"15:10", true}
                , new Object[]{"8:17:15", true}
                , new Object[]{"12:17:15z", true}
                , new Object[]{"12:17:15+01:00:00", true}
                , new Object[]{"24:01:15", false}
                , new Object[]{"31:02:15", false}
                , new Object[]{"08:60:48", false}
                , new Object[]{"07:06:84", false}
                , new Object[]{"16", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getLenientPatternTest(String value, boolean match) {
        assertThat(XsTimeFormatter.LENIENT_PATTERN.matcher(value).matches()).isEqualTo(match);
    }
}