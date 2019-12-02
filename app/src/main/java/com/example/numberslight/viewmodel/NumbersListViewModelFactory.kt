package com.example.numberslight.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.numberslight.service.repository.Repository
import javax.inject.Inject

class NumbersListViewModelFactory @Inject constructor(val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NumbersDataViewModel(repository) as T
    }
}