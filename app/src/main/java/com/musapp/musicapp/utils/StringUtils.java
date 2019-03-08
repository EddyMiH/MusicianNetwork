package com.musapp.musicapp.utils;

import java.util.Calendar;

public final class StringUtils {
    private StringUtils(){}

    public static String CalendarToString(Calendar calendar){
        return calendar.get(Calendar.DAY_OF_MONTH) + "//" + calendar.get(Calendar.MONTH) + "//" +
        calendar.get(Calendar.YEAR);
    }
}
