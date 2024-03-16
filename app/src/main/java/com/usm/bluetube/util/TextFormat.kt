package com.usm.bluetube.util

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val THOUSAND = 1_000
const val MILLION = 1_000_000
const val MILLIARD = 1_000_000_000
const val TRILLION = 1_000_000_000

const val SECONDS_PER_MINUTE = 60
const val SECONDS_PER_HOUR = 3600
const val HOURS_PER_DAY = 24
const val DAYS_PER_MONTH = 31
const val DAYS_PER_YEAR = 365
const val MONTHS_PER_YEAR = 12

fun formatViews(views: Long): String {
    return when(true) {
        (views < THOUSAND) -> views.toString()
        (views < MILLION) -> "${(views / THOUSAND).toInt()}K views"
        (views < MILLIARD) -> "${(views / MILLION).toInt()}M views"
        else -> "${(views / TRILLION).toInt()}MLR views"
    }
}

fun formatDate(date: String): String {
    val postedDate = LocalDateTime.parse(
        date.substring(0, 19),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME
    )
    val currentDate = LocalDateTime.now()
    val postedAgo = Duration.between(postedDate, currentDate)
    val seconds = postedAgo.seconds
    val hours = postedAgo.toHours()
    val days = postedAgo.toDays()

    return when(true) {
        (seconds < SECONDS_PER_HOUR) -> "${seconds / SECONDS_PER_MINUTE} minutes ago"
        (hours < HOURS_PER_DAY) -> "$hours hours ago"
        (days < DAYS_PER_MONTH) -> "$days days ago"
        (days < DAYS_PER_YEAR) -> "${days / MONTHS_PER_YEAR} months ago"
        else -> "${days / DAYS_PER_YEAR} years ago"
    }
}