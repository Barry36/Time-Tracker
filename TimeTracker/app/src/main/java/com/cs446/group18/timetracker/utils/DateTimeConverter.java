package com.cs446.group18.timetracker.utils;

import androidx.room.TypeConverter;

import com.cs446.group18.timetracker.exception.UServiceException;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter {
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public static Date fromTimestamp(final String source) {
        Date date = null;

        try {
            if (StringUtils.isNotEmpty(source)) {
                date = format.parse(source);
            }
        } catch (ParseException e) {
            throw new UServiceException("TXN_101", "", "Date parse error", e);
        }
        return date;
    }

    @TypeConverter
    public static String dateToTimestamp(final Date date) {
        String time = null;
        if (date != null) {
            time = format.format(date);
        }
        return time;
    }
}
