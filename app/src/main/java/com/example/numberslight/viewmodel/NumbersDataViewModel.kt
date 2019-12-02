package com.example.numberslight.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.numberslight.service.model.NumberData
import com.example.numberslight.service.repository.Repository
import com.example.numberslight.service.usecase.GetNumbersDataUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NumbersDataViewModel(val repository: Repository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val listNumbers = MutableLiveData<ArrayList<NumberData>>()

    init{
        getNumbers()
    }

    private fun getNumbers(){
        compositeDisposable.add(
            GetNumbersDataUseCase(repository).execute().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        listNumbers.value = it
                    },
                    {
                        Log.e(this.javaClass.name, it.message)
                    }
                ))
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    fun numbers() = listNumbers
}