package com.example.numberslight.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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
    private val state = MutableLiveData<State>()

    init {
        getNumbers()
    }

    fun getNumbers() {
        compositeDisposable.add(
            GetNumbersDataUseCase(repository).execute().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        listNumbers.value = it
                        state.value = State.Success
                    },
                    {
                        state.value = State.Error(it)
                    }
                ))
    }

    override fun onCleared() {
        super.onCleared()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    fun numbers(): LiveData<ArrayList<NumberData>> = listNumbers
    fun state(): LiveData<State> = state

    sealed class State {
        object Success : State()
        class Error(val error: Throwable) : State()
    }
}