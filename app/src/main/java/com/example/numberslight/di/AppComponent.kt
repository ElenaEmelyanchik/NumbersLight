package com.example.numberslight.di

import com.example.numberslight.service.repository.Repository
import com.example.numberslight.view.ui.NumberDetailsFragment
import com.example.numberslight.view.ui.MainActivity
import com.example.numberslight.view.ui.NumbersFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, NetworkModule::class])
interface AppComponent {

    fun repository(): Repository
    fun inject(activity: MainActivity)
    fun inject(fragment: NumbersFragment)
    fun inject(fragment: NumberDetailsFragment)
}