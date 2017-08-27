package com.johnpetitto.orachat

import android.content.Context
import android.support.annotation.PluralsRes
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXX", Locale.ENGLISH)

private val secs = TimeUnit.SECONDS.toMillis(1)
private val mins = TimeUnit.MINUTES.toMillis(1)
private val hours = TimeUnit.HOURS.toMillis(1)
private val days = TimeUnit.DAYS.toMillis(1)
private val months = TimeUnit.DAYS.toMillis(30)
private val years = TimeUnit.DAYS.toMillis(365)

fun getTimeAgo(context: Context, createdAt: String): String {
    val currentTime = Date().time
    val createdAtTime = dateFormatter.parse(createdAt).time
    val diff = currentTime - createdAtTime

    return when {
        diff < secs -> context.getString(R.string.now)
        diff < mins -> getTimeSuffix(context, R.plurals.seconds_suffix, diff / secs)
        diff < hours -> getTimeSuffix(context, R.plurals.minutes_suffix, diff / mins)
        diff < days -> getTimeSuffix(context, R.plurals.hours_suffix, diff / hours)
        diff < months -> getTimeSuffix(context, R.plurals.days_suffix, diff / days)
        diff < years -> getTimeSuffix(context, R.plurals.months_suffix, diff / months)
        else -> getTimeSuffix(context, R.plurals.years_suffix, diff / years)
    }
}

private fun getTimeSuffix(context: Context, @PluralsRes resId: Int, quantity: Long) =
        context.resources.getQuantityString(resId, quantity.toInt(), quantity)
