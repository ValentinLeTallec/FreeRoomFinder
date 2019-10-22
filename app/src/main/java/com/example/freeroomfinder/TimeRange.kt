package com.example.freeroomfinder

import java.util.TimeZone.getTimeZone
import java.util.Calendar.getInstance
import java.util.Calendar.YEAR
import java.util.Calendar.MONTH
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE

class TimeRange(timeRangeString: String? = "") {

    /**Normailistion d'un entier
     * @param number le nombre
     * @return for example "09" if number = 9 and "10" si number = 10
     */
    //@ requires number <100
    private fun normaliser(number: Int): String =
        if (number < 10) "0$number" else "$number"

    private val date = getInstance()
    var year = date.get(YEAR)
    var month = date.get(MONTH)
    var day = date.get(DAY_OF_MONTH)
    var hourStart = date.get(HOUR_OF_DAY)
    var minuteStart = date.get(MINUTE)
    var hourEnd = date.get(HOUR_OF_DAY) + 1
    var minuteEnd = date.get(MINUTE)

    fun getDateToPrint(): String = normaliser(day) + "/" + normaliser(month + 1) +"/$year"
    fun getDateForQuery() : String = "$year-" + normaliser(month + 1) + "-" + normaliser(day)
    fun getStartTime() = normaliser(hourStart) + " h " + normaliser(minuteStart)
    fun getEndTime()   = normaliser(hourEnd)   + " h " + normaliser(minuteEnd)

    private fun utcPlus0(hour: Int): String {
        val currentTimezoneOffset = getTimeZone("Europe/Paris").getOffset(1, year, month, day, 0, 0)/3600000
        return normaliser((hour - currentTimezoneOffset).rem(24))
    }
    /**
     * This function is used to transmit a TimeRange from an activity to an other,
     * for practical reasons the time transmitted is standardised to UTC+0
      */
    override fun toString() : String =
        "$year" + normaliser(month) + normaliser(day) + utcPlus0(hourStart) +
        normaliser(minuteStart) + utcPlus0(hourEnd) + normaliser(minuteEnd)

    init {
        if (timeRangeString != null && timeRangeString.length == 16) {
            this.year   = Integer.valueOf(timeRangeString.substring(0,4))
            this.month  = Integer.valueOf(timeRangeString.substring(4,6))
            this.day    = Integer.valueOf(timeRangeString.substring(6,8))
            this.hourStart   = Integer.valueOf(timeRangeString.substring(8,10))
            this.minuteStart = Integer.valueOf(timeRangeString.substring(10,12))
            this.hourEnd     = Integer.valueOf(timeRangeString.substring(12,14))
            this.minuteEnd   = Integer.valueOf(timeRangeString.substring(14,16))
        }
    }

    fun setDate(year: Int, month: Int, day: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    fun isChronological() = hourStart*60 + minuteStart < hourEnd*60 + minuteEnd

    fun setStartTime(hour: Int, minute: Int) {
        this.hourStart = hour
        this.minuteStart = minute
    }

    fun setEndTime(hour: Int, minute: Int) {
        this.hourEnd = hour
        this.minuteEnd = minute
    }
}

