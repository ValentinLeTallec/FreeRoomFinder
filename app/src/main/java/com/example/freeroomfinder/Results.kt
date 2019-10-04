package com.example.freeroomfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_results.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class Results : AppCompatActivity() {

    private lateinit var date: TimeRange

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        date = TimeRange(intent.getStringExtra("timeRange"))

        val rooms = resources.getStringArray(R.array.sallesInfo)

        /**
         * urlCalendarPart1 + room_id1 + "," + room_id2 + ... + urlCalendarPart2 + date.getDateForQuery + urlCalendarPart3 + date.getDateForQuery
         * Example : urlCalendarPart1 + "304,305,306,307,308,309" + urlCalendarPart2 + "2020-02-29" + urlCalendarPart2 + "2020-02-29"
          */
        val urlCalendarPart1 = resources.getString(R.string.urlCalendarPart1)
        val urlCalendarPart2 = resources.getString(R.string.urlCalendarPart2)
        val urlCalendarPart3 = resources.getString(R.string.urlCalendarPart3)

        var urlString = urlCalendarPart1
        var roomNames =  emptyArray<String>()
        for ((index, value ) in rooms.withIndex()) {

            if (index.rem(2) == 0) {
                if (value.last() != 'b')
                roomNames += value.substring(0,4)

            } else {
                urlString += value
                if (index < rooms.size - 2) {
                    urlString += ","
                }

            }
        }

        urlString += urlCalendarPart2 + date.getDateForQuery() + urlCalendarPart3 + date.getDateForQuery()


        doAsync {
            val calendar = Calendar(URL(urlString).readText())
            uiThread {
                sallesLibres.text =
                    calendar.freeRoomsBetween(roomNames, date.hourStart, date.minuteStart, date.hourEnd, date.minuteEnd)
            }
        }
    }
}
