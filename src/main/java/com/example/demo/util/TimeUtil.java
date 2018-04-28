package com.example.demo.util;

import java.sql.Timestamp;
import java.util.Calendar;

public class TimeUtil {
    public static Timestamp getTodayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp getTodayEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp getHourStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp getHourEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public static void main(String[] args) {
        System.err.println(getHourStartTime());
        System.err.println(getHourEndTime());
    }
}
