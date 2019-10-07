package com.example.freeroomfinder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_accueil.*

class Accueil : AppCompatActivity() {

    private val date = TimeRange()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        /* Initialisation du texte présent sur les différents boutton */
        bDate.text = date.getDateToPrint()
        bStartTime.text = date.getStartTime()
        bEndTime.text = date.getEndTime()
    }

    /** @return true if connected to Internet and false otherwise*/
    private fun haveInternetConnection(): Boolean {
        val network =
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return network != null && network.isConnected
    }

    fun lookForFreeRoom(view: View) {
        if (haveInternetConnection()) {

            if (date.isChronological()) {
                val toResults = Intent(this, Results::class.java)
                toResults.putExtra("timeRange", date.toString())
                startActivity(toResults)
            } else {
                val toError = Intent(this, Error::class.java)
                toError.putExtra("errorMessage", "Veuillez entrer des horaires consécutifs.")
                startActivity(toError)
            }

        } else {
            val toError = Intent(this, Error::class.java)
            toError.putExtra("errorMessage", "Veuillez vous connecter à internet.")
            startActivity(toError)
        }
    }

    fun showTimePicker(view: View) {
        val hour: Int
        val minute: Int
        val button: Int

        if (view == startTimeCard) {
            hour = date.hourStart
            minute = date.minuteStart
            button = 1
        } else {
            hour = date.hourEnd
            minute = date.minuteEnd
            button = 2
        }

        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener(function = { _, h, m -> onTimeSet(h,m,button)}),
            hour,minute, true)

        timePickerDialog.show()
    }

    private fun onTimeSet(hour: Int, minute: Int, button: Int) {
        if (button == 1) {
            date.setStartTime(hour, minute)
            bStartTime.text = date.getStartTime()
        } else {
            date.setEndTime(hour, minute)
            bEndTime.text = date.getEndTime()
        }
    }

    fun showDatePicker(view: View) {
        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener(function =
                { _, y, m, d -> date.setDate(y,m,d)
                                bDate.text = date.getDateToPrint()
                }),
            date.year, date.month, date.day)

        datePickerDialog.show()
    }
}
