package com.provys.common.xsd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class XsDateFormatterTest {

    private static final Pattern strictDatePattern = Pattern.compile(XsDateFormatter.STRICT_DATE_REGEX);

    @Nonnull
    static Stream<Object[]> getStrictDateRegexTest() {
        return Stream.of(
                new Object[]{"2018-03-14", true}
                , new Object[]{"2015-12-31", true}
                , new Object[]{"-2015-12-31", true}
                , new Object[]{"2015-12-31Z", false}
                , new Object[]{"2015-12-31+01:00", false}
                , new Object[]{"15-12-31", false}
                , new Object[]{"2018", false}
                , new Object[]{"2018-01", false}
                , new Object[]{"2018-01-24T12:00:00", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStrictDateRegexTest(String value, boolean match) {
        assertThat(strictDatePattern.matcher(value).matches()).isEqualTo(match);
    }

    @Nonnull
    static Stream<Object[]> getStrictPatternTest() {
        return Stream.of(
                new Object[]{"2018-03-14", true}
                , new Object[]{"2015-12-31", true}
                , new Object[]{"-2015-12-31", true}
                , new Object[]{"2015-12-31Z", true}
                , new Object[]{"2015-12-31+01:00", true}
                , new Object[]{"2015-12-31z", false}
                , new Object[]{"2015-12-31+01:00:00", false}
                , new Object[]{"15-12-31", false}
                , new Object[]{"2018", false}
                , new Object[]{"2018-01", false}
                , new Object[]{"2018-01-24T12:00:00", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getStrictPatternTest(String value, boolean match) {
        assertThat(XsDateFormatter.STRICT_PATTERN.matcher(value).matches()).isEqualTo(match);
    }

    @Nonnull
    static Stream<Object[]> parseTest() {
        return Stream.of(
                new Object[]{"2018-03-14", LocalDate.of(2018, 3, 14)}
                , new Object[]{"2015-12-31", LocalDate.of(2015, 12, 31)}
                , new Object[]{"-2015-12-31", LocalDate.of(-2015, 12, 31)}
                , new Object[]{"2015-12-31Z", LocalDate.of(2015, 12, 31)}
                , new Object[]{"2015-12-31+01:00", LocalDate.of(2015, 12, 31)}
                , new Object[]{"2015-12-31z", null}
                , new Object[]{"2015-12-31+01:00:00", null}
                , new Object[]{"15-12-31", null}
                , new Object[]{"2018", null}
                , new Object[]{"2018-01", null}
                , new Object[]{"2018-01-24T12:00:00", null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseTest(String value, LocalDate result) {
        if (result != null) {
            assertThat(XsDateFormatter.parse(value)).isEqualTo(result);
        } else {
            assertThatThrownBy(() -> XsDateFormatter.parse(value));
        }
    }

    private static final Pattern lenientDatePattern = Pattern.compile(XsDateFormatter.LENIENT_DATE_REGEX);

    @Nonnull
    static Stream<Object[]> getLenientDateRegexTest() {
        return Stream.of(
                new Object[]{"2018-03-14", true}
                , new Object[]{"2015-12-31", true}
                , new Object[]{"-2015-12-31", true}
                , new Object[]{"2015-12-31Z", false}
                , new Object[]{"2015-12-31+01:00", false}
                , new Object[]{"15-12-31", false}
                , new Object[]{"2018", false}
                , new Object[]{"2018-01", false}
                , new Object[]{"2018-01-24T12:00:00", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getLenientDateRegexTest(String value, boolean match) {
        assertThat(lenientDatePattern.matcher(value).matches()).isEqualTo(match);
    }

    @Nonnull
    static Stream<Object[]> getLenientPatternTest() {
        return Stream.of(
                new Object[]{"2018-03-14", true}
                , new Object[]{"2015-12-31", true}
                , new Object[]{"-2015-12-31", true}
                , new Object[]{"2015-12-31Z", true}
                , new Object[]{"2015-12-31+01:00", true}
                , new Object[]{"2015-12-31z", true}
                , new Object[]{"2015-12-31+01:00:00", true}
                , new Object[]{"15-12-31", false}
                , new Object[]{"2018", false}
                , new Object[]{"2018-01", false}
                , new Object[]{"2018-01-24T12:00:00", false}
        );
    }

    @ParameterizedTest
    @MethodSource
    void getLenientPatternTest(String value, boolean match) {
        assertThat(XsDateFormatter.LENIENT_PATTERN.matcher(value).matches()).isEqualTo(match);
    }
    @Nonnull
    static Stream<Object[]> parseLenientTest() {
        return Stream.of(
                new Object[]{"2018-03-14", LocalDate.of(2018, 3, 14)}
                , new Object[]{"2015-12-31", LocalDate.of(2015, 12, 31)}
                , new Object[]{"-2015-12-31", LocalDate.of(-2015, 12, 31)}
                , new Object[]{"2015-12-31Z", LocalDate.of(2015, 12, 31)}
                , new Object[]{"2015-12-31+01:00", LocalDate.of(2015, 12, 31)}
                , new Object[]{"2015-12-31z", LocalDate.of(2015, 12, 31)}
                , new Object[]{"2015-12-31+01:00:00", LocalDate.of(2015, 12, 31)}
                , new Object[]{"15-12-31", null}
                , new Object[]{"2018", null}
                , new Object[]{"2018-01", null}
                , new Object[]{"2018-01-24T12:00:00", null}
        );
    }

    @ParameterizedTest
    @MethodSource
    void parseLenientTest(String value, @Nullable LocalDate result) {
        if (result != null) {
            assertThat(XsDateFormatter.parseLenient(value)).isEqualTo(result);
        } else {
            assertThatThrownBy(() -> XsDateFormatter.parseLenient(value));
        }
    }

}