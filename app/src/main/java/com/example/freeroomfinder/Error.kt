package com.example.freeroomfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class Error : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        findViewById<TextView>(R.id.errorTextView).text = intent.getStringExtra("errorMessage")
    }

    /**Retourne à l'acceuil */
    fun retry(view: View) {
        val toAcceuil = Intent(this, Accueil::class.java)
        startActivity(toAcceuil)
    }
}
