package co.jp.wever.graphql.infrastructure.util;

import java.time.ZoneOffset;
import java.util.Date;

public class DateToStringConverter {
    public static String toDateString(Date date){
        return date.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime().format(
            DateStringFormat.getFormatter());
    }
}
