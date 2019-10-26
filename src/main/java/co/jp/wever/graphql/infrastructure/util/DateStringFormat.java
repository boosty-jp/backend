package co.jp.wever.graphql.infrastructure.util;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class DateStringFormat {
    private final static DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("uuuu年MM月dd日").withLocale(Locale.JAPANESE).withResolverStyle(ResolverStyle.STRICT);

    public static DateTimeFormatter getFormatter() {
        return formatter;
    }
}
