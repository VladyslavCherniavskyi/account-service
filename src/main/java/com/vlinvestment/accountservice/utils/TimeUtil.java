package com.vlinvestment.accountservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtil {

    private static final String FORMAT = "DD.MM.YYYY HH:mm:ss";
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static String formatter(Date date) {
        var dateFormat = new SimpleDateFormat(FORMAT);
        dateFormat.setTimeZone(UTC);
        return dateFormat.format(date);
    }

}
