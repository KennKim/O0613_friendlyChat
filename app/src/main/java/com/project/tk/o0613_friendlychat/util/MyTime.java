package com.project.tk.o0613_friendlychat.util;

import android.content.Context;

import com.project.tk.o0613_friendlychat.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static java.lang.System.currentTimeMillis;


/**
 * Created by conscious on 2017-07-06.
 */

public class MyTime {
    private static final String TAG_MYTIME = "MyTime";

    private static final long LOCALE_GAP = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());
    private static int MINUTE, HOUR, DAY, WEEK, MONTH, YEAR;
    private static String SECOND_STR, MINUTE_STR, HOUR_STR, DAY_STR, WEEK_STR, MONTH_STR, YEAR_STR;


    public MyTime(Context context) {

        MINUTE = 60;
        MINUTE = 60;
        HOUR = MINUTE * 60;
        DAY = HOUR * 24;
        WEEK = DAY * 7;
        MONTH = WEEK * 4;
        YEAR = MONTH * 12;

        SECOND_STR = context.getString(R.string.elapsed_seconds);
        MINUTE_STR = context.getString(R.string.elapsed_minutess);
        HOUR_STR = context.getString(R.string.elapsed_hours);
        DAY_STR = context.getString(R.string.elapsed_days);
        WEEK_STR = context.getString(R.string.elapsed_weeks);
        MONTH_STR = context.getString(R.string.elapsed_months);
        YEAR_STR = context.getString(R.string.elapsed_years);
    }

    public static String getLongGMT() {
        return currentTimeMillis() - LOCALE_GAP + "";
    }

    public static String getFormat(String dbTime) {
        long longTime = Long.parseLong(dbTime);
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy a HH:mm:ss");
//        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss (z Z)");
        return dateFormat.format(longTime + LOCALE_GAP);
    }

    public static String getElapsedTime(String dbTime) {
        long longTime = Long.parseLong(dbTime);
        long nowGMT = currentTimeMillis() - LOCALE_GAP;

        long elapsedTime = (nowGMT - longTime) / 1000;

        String result = null;

        if (elapsedTime >= MINUTE) {
            if (elapsedTime >= HOUR) {
                if (elapsedTime >= DAY) {
                    if (elapsedTime >= WEEK) {
                        if (elapsedTime >= MONTH) {
                            if (elapsedTime >= YEAR) {
//                                result = getFormat(dbTime);
                                result = (int) elapsedTime / YEAR + YEAR_STR;
                            } else {
                                result = (int) elapsedTime / MONTH + MONTH_STR;
                            }
                        } else {
                            result = (int) elapsedTime / WEEK + WEEK_STR;
                        }
                    } else {
                        result = (int) elapsedTime / DAY + DAY_STR;
                    }
                } else {
                    result = (int) elapsedTime / HOUR + HOUR_STR;
                }
            } else {
                result = (int) elapsedTime / MINUTE + MINUTE_STR;
            }
        } else {
            result = elapsedTime + SECOND_STR;
        }

        return result;
    }
}
