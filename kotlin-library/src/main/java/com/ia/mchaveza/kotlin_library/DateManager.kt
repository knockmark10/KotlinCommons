package com.ia.mchaveza.kotlin_library

import java.util.*

object DateManager {

    fun getCurrentYear(): String =
            Calendar.getInstance().get(Calendar.YEAR).toString()

    fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.MONTH)) {
            Calendar.JANUARY -> {
                "Enero"
            }
            Calendar.FEBRUARY -> {
                "Febrero"
            }
            Calendar.MARCH -> {
                "Marzo"
            }
            Calendar.APRIL -> {
                "Abril"
            }
            Calendar.MAY -> {
                "Mayo"
            }
            Calendar.JUNE -> {
                "Junio"
            }
            Calendar.JULY -> {
                "Julio"
            }
            Calendar.AUGUST -> {
                "Agosto"
            }
            Calendar.SEPTEMBER -> {
                "Septiembre"
            }
            Calendar.OCTOBER -> {
                "Octubre"
            }
            Calendar.NOVEMBER -> {
                "Noviembre"
            }
            Calendar.DECEMBER -> {
                "Diciembre"
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