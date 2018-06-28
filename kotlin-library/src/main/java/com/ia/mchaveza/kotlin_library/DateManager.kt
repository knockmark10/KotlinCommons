package com.ia.mchaveza.kotlin_library

import java.util.*

object DateManager {

    fun getCurrentYear(): String =
            Calendar.getInstance().get(Calendar.YEAR).toString()

    fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.MONTH)) {
            Calendar.JANUARY -> {
                Constants.JANUARY
            }
            Calendar.FEBRUARY -> {
                Constants.FEBRUARY
            }
            Calendar.MARCH -> {
                Constants.MARCH
            }
            Calendar.APRIL -> {
                Constants.APRIL
            }
            Calendar.MAY -> {
                Constants.MAY
            }
            Calendar.JUNE -> {
                Constants.JUNE
            }
            Calendar.JULY -> {
                Constants.JULY
            }
            Calendar.AUGUST -> {
                Constants.AUGUST
            }
            Calendar.SEPTEMBER -> {
                Constants.SEPTEMBER
            }
            Calendar.OCTOBER -> {
                Constants.OCTOBER
            }
            Calendar.NOVEMBER -> {
                Constants.NOVEMBER
            }
            Calendar.DECEMBER -> {
                Constants.DECEMBER
            }
            else -> {
                ""
            }
        }
    }

    fun getCurrentDayOfMonth(): String {
        val calendar = Calendar.getInstance()
        var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        if (day.length == 1) {
            day = "0$day"
        }
        return day
    }

    fun getCurrentDayOfWeek(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        return when (day) {
            Calendar.SUNDAY -> {
                Constants.SUNDAY
            }
            Calendar.MONDAY -> {
                Constants.MONDAY
            }
            Calendar.TUESDAY -> {
                Constants.TUESDAY
            }
            Calendar.WEDNESDAY -> {
                Constants.WEDNESDAY
            }
            Calendar.THURSDAY -> {
                Constants.THURSDAY
            }
            Calendar.FRIDAY -> {
                Constants.FRIDAY
            }
            Calendar.SATURDAY -> {
                Constants.SATURDAY
            }
            else -> {
                ""
            }
        }
    }

    fun getCurrentDateInMillis() = System.currentTimeMillis()

    fun getCurrentHour(): Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)


}