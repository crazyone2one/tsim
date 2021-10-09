package cn.master.tsim.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Created by 11's papa on 2021/10/08
 * @version 1.0.0
 */
public class DateUtils {

    public static LocalDate parse2LocalDate(String date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("");
        return LocalDate.parse(date, formatter);
    }

    public static LocalDate parse2LocalDate(Date date) {
        final Instant instant = date.toInstant();
        final ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    public static List<String> currentYearMonth() {
        final int year = LocalDate.now().getYear();
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            if (i > 9) {
                list.add(year + "-" + i);
            } else {
                list.add(year + "-0" + i);
            }
        }
        return list;
    }
}
