package com.example.numberslight.service.repository

import com.example.numberslight.service.model.NumberData
import com.example.numberslight.service.model.NumberDataDetails
import io.reactivex.Single
import java.util.*

interface Repository {

    fun getNumbers(): Single<ArrayList<NumberData>>

    fun getNumberDetails(name: String): Single<NumberDataDetails>
}