package com.example.freeroomfinder


class Time(hour: Int, minute: Int) {
    /**
     * minutes since midnight
     */
    var value: Int = hour*60 + minute
}

class Event(val startTime: Time, val endTime: Time, val locations: List<String>)

/**
 * @param stringCal contenu d'un fichier ics
 */
class Calendar(stringCal: String = "") {
    private var events = parseIcs(stringCal)

    private fun parseIcs(stringCal: String): List<Event> {
        if (stringCal == "") {
            return emptyList()
        }

        val events = mutableListOf<Event>()

        val calAsList = stringCal.replaceBefore("DTSTAMP:","","")
                               .removeSuffix("END:VEVENT\r\nEND:VCALENDAR\r\n")
                               .split("END:VEVENT\r\nBEGIN:VEVENT\r\n")

        if (calAsList != listOf("")) {
            for (item in calAsList) {
                lateinit var startTime: Time
                lateinit var endTime: Time
                lateinit var location: List<String>

                for (line in item.lines()) {
                    when (line.substringBefore(':')) {
                        "DTSTART"  -> startTime = dtToTime(line.substringAfter(':'))
                        "DTEND"    -> endTime   = dtToTime(line.substringAfter(':'))
                        "LOCATION" -> location  = line.substringAfter(':').split("\\,")
                    }
                }
                events += Event(startTime, endTime, location)
            }
        }
        return events
    }

    private fun dtToTime (DT: String): Time {
        val hour = Integer.valueOf(DT.substring(9,11))
        val minute = Integer.valueOf(DT.substring(11,13))
        return Time(hour, minute)
    }

    fun freeRoomsBetween(roomNames: Array<String>, hourStart: Int, minuteStart: Int, hourEnd: Int, minuteEnd: Int): String {
        val startTime: Int = Time(hourStart, minuteStart).value
        val endTime: Int = Time(hourEnd, minuteEnd).value
        val freeRooms = roomNames.toMutableList()

        for (e in events) {
            if (e.endTime.value   in (startTime + 1) until endTime ||
                e.startTime.value in (startTime + 1) until endTime ||
                e.startTime.value < startTime && endTime < e.endTime.value) {
                for (location in e.locations) {
                    freeRooms.remove(location.substring(0, 4))
//                    freeRooms.remove(location.substring(0, 4) + "a")
//                    freeRooms.remove(location.substring(0, 4) + "b")
                }
            }
        }
        var freeRoomString = ""
        if (freeRooms.isEmpty()) {
            freeRoomString = "Aucune salle libre"
        } else {
            for (room in freeRooms) {
                freeRoomString += "$room\n"
            }
        }
        return freeRoomString
    }
}

