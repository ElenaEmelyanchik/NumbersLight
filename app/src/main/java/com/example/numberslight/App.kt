package com.example.numberslight

import android.app.Application
import com.example.numberslight.di.AppComponent
import com.example.numberslight.di.ContextModule
import com.example.numberslight.di.DaggerAppComponent
import com.example.numberslight.di.NetworkModule

class App : Application() {
    private val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .contextModule(ContextModule(this))
            .networkModule(NetworkModule())
            .build()
    }

    fun component() = component
}