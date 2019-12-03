package com.example.numberslight.view.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.numberslight.R


class NumberDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (resources.getBoolean(R.bool.isLandscapeTablet)) {
            finish()
            return
        }
        setContentView(R.layout.activity_number_details);
        if (savedInstanceState == null) {
            val details = NumberDetailsFragment1().apply {
                arguments = intent.extras
            }
            supportFragmentManager.beginTransaction().add(R.id.frameLayout, details).commit()
        }
    }
}