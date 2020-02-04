package com.n7.freeroomfinder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_accueil.*

class Accueil : AppCompatActivity() {

    private var date = TimeRange()
    private var chipTable: Array<Chip> = emptyArray()
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        /* Initialisation du texte présent sur les différents boutton */
        bDate.text = date.getDateToPrint()
        bStartTime.text = date.getStartTime()
        bEndTime.text = date.getEndTime()

        sharedPref = this.getPreferences(Context.MODE_PRIVATE)

        /* Declare and restore filter to previous state */
        for (filterName in resources.getStringArray(R.array.filtersName)) {
            val chip = Chip(this)
            chip.text = filterName
            chip.isCheckable = true
            chip.isChecked = sharedPref.getBoolean(filterName, filterName == "Linux")
            filters.addView(chip)
            chipTable += chip
        }

    }

    override fun onResume() {
        super.onResume()
        date = TimeRange()

        /* Réinitialisation du texte présent sur les différents boutton */
        bDate.text = date.getDateToPrint()
        bStartTime.text = date.getStartTime()
        bEndTime.text = date.getEndTime()
    }

    override fun onStop() {
        super.onStop()

        val editor = sharedPref.edit()
        for (chip in chipTable) {
            editor.putBoolean(chip.text.toString(), chip.isChecked)
        }
        editor.apply()
    }

    /** @return true if connected to Internet and false otherwise*/
    private fun haveInternetConnection(): Boolean {
        val network =
            (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return network != null && network.isConnected
    }

    private fun filtersToString(): String {
        var str = ""
        for (chip in chipTable){
            str += if (chip.isChecked) "1" else "0"
        }
        return str
    }

    fun lookForFreeRoom(view: View) {

        if (haveInternetConnection()) {

            if (date.isChronological()) {
                val toResults = Intent(this, Results::class.java)
                toResults.putExtra("timeRange", date.toString())
                toResults.putExtra("filters", filtersToString())
                startActivity(toResults)
            } else {
                AlertDialog.Builder(this)
                    .setMessage("Veuillez entrer des horaires consécutifs.")
                    .setPositiveButton("Ok", null)
                    .show()
            }

        } else {
            AlertDialog.Builder(this)
                .setMessage("Veuillez vous connecter à Internet.")
                .setPositiveButton("Ok", null)
                .show()
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
            date.setEndTime(hour + 1, minute)
            bEndTime.text = date.getEndTime()
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
