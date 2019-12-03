package com.example.numberslight.viewmodel

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
    private val state = MutableLiveData<State>()

    fun getDetails(name: String){
        compositeDisposable.add(
            GetNumberDetailsUseCase(repository, name).execute().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        numberDetails.value = it
                        state.value = State.Success
                    },
                    {
                        state.value = State.Error(it)
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

    fun state(): LiveData<State> = state

    sealed class State {
        object Success : State()
        class Error(val error: Throwable) : State()
    }
}