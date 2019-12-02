package com.example.numberslight.service.repository

import com.example.numberslight.service.model.NumberData
import com.example.numberslight.service.model.NumberDataDetails
import io.reactivex.Single
import java.util.*

class ApiRestRepository(val api: NumbersLightApi) : Repository {
    override fun getNumbers(): Single<ArrayList<NumberData>> {
        return api.getNumbersData()
    }

    override fun getNumberDetails(name: String): Single<NumberDataDetails> {
        return api.getNumberDataDetails(name)
    }
}