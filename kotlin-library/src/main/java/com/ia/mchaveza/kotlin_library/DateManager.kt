package com.ia.mchaveza.kotlin_library

import android.content.Context
import java.util.*

object DateManager {

    fun getCurrentYear(): String =
            Calendar.getInstance().get(Calendar.YEAR).toString()

    fun getCurrentMonth(context: Context): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.MONTH)) {
            Calendar.JANUARY -> {
                context.getString(R.string.january)
            }
            Calendar.FEBRUARY -> {
                context.getString(R.string.january)
            }
            Calendar.MARCH -> {
                context.getString(R.string.january)
            }
            Calendar.APRIL -> {
                context.getString(R.string.january)
            }
            Calendar.MAY -> {
                context.getString(R.string.january)
            }
            Calendar.JUNE -> {
                context.getString(R.string.january)
            }
            Calendar.JULY -> {
                context.getString(R.string.january)
            }
            Calendar.AUGUST -> {
                context.getString(R.string.january)
            }
            Calendar.SEPTEMBER -> {
                context.getString(R.string.january)
            }
            Calendar.OCTOBER -> {
                context.getString(R.string.january)
            }
            Calendar.NOVEMBER -> {
                context.getString(R.string.january)
            }
            Calendar.DECEMBER -> {
                context.getString(R.string.january)
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
                "Domingo"
            }
            Calendar.MONDAY -> {
                "Lunes"
            }
            Calendar.TUESDAY -> {
                "Martes"
            }
            Calendar.WEDNESDAY -> {
                "Miércoles"
            }
            Calendar.THURSDAY -> {
                "Jueves"
            }
            Calendar.FRIDAY -> {
                "Viernes"
            }
            Calendar.SATURDAY -> {
                "Sábado"
            }
            else -> {
                ""
            }
        }
    }

    fun getCurrentDateInMillis() = System.currentTimeMillis()

    fun getCurrentHour(): Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)



}