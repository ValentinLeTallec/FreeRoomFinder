package com.n7.freeroomfinder


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

        val filters = intent.getStringExtra("filters")

        for ((index, filterName) in resources.getStringArray(R.array.filtersName).withIndex()) {
            if (filters != null && (filters.matches("0+".toRegex()) || filters[index] == '1')) lookForFreeRoomsIn(filterName)
        }
    }

    private fun lookForFreeRoomsIn(filterName: String) {
        val rooms = resources.getStringArray(resources.getIdentifier(filterName,"array", packageName))

        var roomsId = ""
        var roomNames =  emptyArray<String>()
        for ((index, value ) in rooms.withIndex()) {

            if (index.rem(2) == 0) {
                if (value.last() != 'b')
                    roomNames += value.substring(0,4)

            } else {
                roomsId += value
                if (index < rooms.size - 2) {
                    roomsId += ","
                }

            }
        }

        val urlString = resources.getString(R.string.urlCalendar, roomsId, date.getDateForQuery())

        doAsync {
            val calendar = Calendar(URL(urlString).readText())
            val freeRooms = calendar.freeRoomsBetween(roomNames, date.hourStart, date.minuteStart, date.hourEnd, date.minuteEnd)
            uiThread {
                FreeRooms.addView(ResultCard(this@Results, filterName, freeRooms))
            }
        }
    }
}
