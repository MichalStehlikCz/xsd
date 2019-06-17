package com.provys.common.xsd;

import javax.annotation.Nonnull;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

/**
 * Class supports parsing and formatting of time information compliant with XSD 1.1 xs:time datatype
 */
public class XsTimeFormatter {

    /**
     * Class only contains static methods and properties
     */
    private XsTimeFormatter() {}

    /**
     * String defining time part of format, accepted by STRICT formatter
     */
    static final String STRICT_TIME_REGEX = "(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))";

    /**
     * Append strict time parser and return formatter back
     */
    @Nonnull
    static final DateTimeFormatter STRICT_TIME = new DateTimeFormatterBuilder()
            .appendValue(HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .optionalStart()
            .appendFraction(NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .toFormatter();

    /**
     * String defining format, accepted by STRICT formatter
     */
    public static final String STRICT_REGEX = STRICT_TIME_REGEX + XsTimezoneFormatter.STRICT_REGEX + '?';

    /**
     * Pattern that corresponds to strings, accepted by STRICT formatter
     */
    public static final Pattern STRICT_PATTERN = Pattern.compile(STRICT_REGEX);

    /**
     * For parsing and formatting of {@code xs:date} values with validation strictly adhering to
     * <a href="https://www.w3.org/TR/xmlschema-2/#dateTime">XML Schema</a>.
     */
    public static final DateTimeFormatter STRICT = new DateTimeFormatterBuilder()
            .parseCaseSensitive()
            .append(STRICT_TIME)
            .optionalStart()
            .append(XsTimezoneFormatter.STRICT)
            .optionalEnd()
            .toFormatter(Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE);

    /**
     * Convert supplied xs:time string to {@code LocalTime} using strict validation
     *
     * @param xsTime is string value strictly compliant with xs:time format. Timezone is completely ignored
     * @return time corresponding to supplied value
     */
    @Nonnull
    public static LocalTime parse(String xsTime) {
        return LocalTime.from(STRICT.parse(Objects.requireNonNull(xsTime)));
    }

    /**
     * String defining time part of format, accepted by LENIENT formatter
     */
    static final String LENIENT_TIME_REGEX = "(([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9](\\.[0-9]+)?)?|(24:00(:00(\\.0+)?))?)";

    /**
     * Fromatter for lenient parsing of time part
     */
    static final DateTimeFormatter LENIENT_TIME = new DateTimeFormatterBuilder()
            .appendValue(HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .optionalStart()
            .appendFraction(NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .optionalEnd()
            .toFormatter();

    /**
     * String defining format, accepted by LENIENT formatter
     */
    public static final String LENIENT_REGEX = LENIENT_TIME_REGEX + XsTimezoneFormatter.LENIENT_REGEX + '?';

    /**
     * Pattern that corresponds to strings, accepted by LENIENT formatter
     */
    public static final Pattern LENIENT_PATTERN = Pattern.compile(LENIENT_REGEX);

    /**
     * For parsing and formatting of {@code xs:date} values with lenient validation
     */
    public static final DateTimeFormatter LENIENT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(LENIENT_TIME)
            .optionalStart()
            .append(XsTimezoneFormatter.LENIENT)
            .optionalEnd()
            .toFormatter(Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.STRICT)
            .withChronology(IsoChronology.INSTANCE);

    /**
     * Convert supplied xs:time string to {@code LocalTime} using lenient validation. Completely ignores timezone
     * information
     *
     * @param xsTime is string value roughly compliant with xs:time format. Timezone is completely ignored
     * @return time corresponding to supplied value
     */
    @Nonnull
    public static LocalTime parseLenient(String xsTime) {
        return LocalTime.from(LENIENT.parse(Objects.requireNonNull(xsTime)));
    }

    private static final Map<String, DateTimeFormatter> LENIENT_TZ_MAP = new ConcurrentHashMap<>(1);

    /**
     * Returns lenient parser that interprets missing timezone information as specified timezone
     */
    @Nonnull
    public static DateTimeFormatter getLenientTZ(String defOffset) {
        Objects.requireNonNull(defOffset);
        return LENIENT_TZ_MAP.computeIfAbsent(defOffset,
                defaultOffset -> new DateTimeFormatterBuilder()
                        .append(XsTimeFormatter.LENIENT_TIME)
                        .optionalStart()
                        .append(XsTimezoneFormatter.getLenient(defaultOffset))
                        .optionalEnd()
                        .toFormatter(Locale.ENGLISH)
                        .withChronology(IsoChronology.INSTANCE));
    }

    /**
     * Convert supplied xs:time string to {@code LocalTime} using lenient validation; value is converted to local time
     * as if local time was for default timezone. Missing timezone information is interpreted as belonging to this
     * default timezone
     *
     * @param xsTime is string value roughly compliant with xs:time format. Timezone is completely ignored
     * @return time corresponding to supplied value
     */
    @Nonnull
    public static LocalTime parseLenientTZ(String xsTime, String defOffset) {
        return OffsetTime.from(getLenientTZ(defOffset).parse(Objects.requireNonNull(xsTime)))
                .withOffsetSameInstant(ZoneOffset.of(defOffset))
                .toLocalTime();
    }
}
