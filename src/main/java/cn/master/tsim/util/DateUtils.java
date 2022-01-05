package cn.master.tsim.util;

import java.text.SimpleDateFormat;
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
    private static final String DATEFORMAT_DAY = "yyyy-MM-dd";

    public static LocalDate parse2LocalDate(String date) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATEFORMAT_DAY);
        return LocalDate.parse(date, formatter);
    }

    public static LocalDate parse2LocalDate(String date, String dateFormat) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDate.parse(date, formatter);
    }

    public static String parse2String(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static LocalDate parse2LocalDate(Date date) {
        final Instant instant = date.toInstant();
        final ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

    public static List<String> currentYearMonth() {
        List<String> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
//        当前月份前3个月
        for (long i = 1L; i <= 3L; i++) {
            LocalDate localDate = today.minusMonths(4 - i);
            String ss = localDate.toString().substring(0, 7);
            list.add(ss);
        }
        for (long i = 0L; i <= 3L; i++) {
            LocalDate localDate = today.minusMonths(-i);
            String ss = localDate.toString().substring(0, 7);
            list.add(ss);
        }
        return list;
    }
}
