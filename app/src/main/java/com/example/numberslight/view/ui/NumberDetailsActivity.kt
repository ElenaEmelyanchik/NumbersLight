package com.example.numberslight.view.ui

import android.R
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.numberslight.viewmodel.NumberDetailsViewModelFactory
import com.example.numberslight.viewmodel.NumbersListViewModelFactory
import javax.inject.Inject


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
            val details = DetailsFragment()
            details.setArguments(intent.extras)
            supportFragmentManager.beginTransaction().add(R.id.content, details).commit()
        }
    }
}