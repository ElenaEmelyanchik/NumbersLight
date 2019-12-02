package com.example.numberslight.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.numberslight.service.model.NumberDataDetails
import com.example.numberslight.service.repository.Repository
import com.example.numberslight.service.usecase.GetNumberDetailsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NumberDetailsViewModel(val repository: Repository) : ViewModel()  {
    private val compositeDisposable = CompositeDisposable()
    private val numberDetails = MutableLiveData<NumberDataDetails>()

    fun getDetails(name: String){
        compositeDisposable.add(
            GetNumberDetailsUseCase(repository, name).execute().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        numberDetails.value = it
                    },
                    {
                        Log.e(this.javaClass.name, it.message)
                    }
                ))

    }

    fun getNumberDetails(): LiveData<NumberDataDetails> = numberDetails

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}