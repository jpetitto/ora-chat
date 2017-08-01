package com.johnpetitto.orachat;

import android.content.Context;
import android.support.annotation.PluralsRes;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class TimeUtils {
    private TimeUtils() {}

    public static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX", Locale.ENGLISH);

    private static final long SECONDS = TimeUnit.SECONDS.toMillis(1);
    private static final long MINUTES = TimeUnit.MINUTES.toMillis(1);
    private static final long HOURS = TimeUnit.HOURS.toMillis(1);
    private static final long DAYS = TimeUnit.DAYS.toMillis(1);
    private static final long YEARS = TimeUnit.DAYS.toMillis(365);

    public static String getTimeAgo(Context context, String createdAt) {
        try {
            long currentTime = new Date().getTime();
            long createdAtTime = dateFormatter.parse(createdAt).getTime();
            long diff = currentTime - createdAtTime;

            if (diff < MINUTES) {
                return getTimeSuffix(context, R.plurals.seconds_suffix, diff / SECONDS);
            } else if (diff < HOURS) {
                return getTimeSuffix(context, R.plurals.minutes_suffix, diff / MINUTES);
            } else if (diff < DAYS) {
                return getTimeSuffix(context, R.plurals.hours_suffix, diff / HOURS);
            } else if (diff < YEARS) {
                return getTimeSuffix(context, R.plurals.days_suffix, diff / DAYS);
            } else {
                return getTimeSuffix(context, R.plurals.years_suffix, diff / YEARS);
            }
        } catch (ParseException e) {
            Log.e("TimeUtils", e.getMessage());
        }

        return null;
    }

    private static String getTimeSuffix(Context context, @PluralsRes int resId, long quantity) {
        return context.getResources().getQuantityString(resId, (int) quantity, quantity);
    }
}
