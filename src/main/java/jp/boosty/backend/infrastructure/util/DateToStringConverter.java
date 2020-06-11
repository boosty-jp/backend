package jp.boosty.backend.infrastructure.util;

import java.time.ZoneOffset;
import java.util.Date;

public class DateToStringConverter {
    public static String toDateString(Date date){
        //JSTにしている
        return date.toInstant().atOffset(ZoneOffset.ofHours(9)).toLocalDateTime().format(
            DateStringFormat.getFormatter());
    }
}
