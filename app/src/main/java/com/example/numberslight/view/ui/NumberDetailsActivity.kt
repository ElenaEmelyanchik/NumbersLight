package com.example.numberslight.view.ui

import android.R
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class NumberDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.configuration.orientation
            == Configuration.ORIENTATION_LANDSCAPE
        ) {
            finish()
            return
        }
        if (savedInstanceState == null) {
            val details = NumberDetailsFragment().apply {
                arguments = intent.extras
            }
            supportFragmentManager.beginTransaction().add(R.id.content, details).commit()
        }
    }
}