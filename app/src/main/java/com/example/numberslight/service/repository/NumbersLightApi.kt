package com.example.numberslight.service.repository

import com.example.numberslight.service.model.NumberData
import com.example.numberslight.service.model.NumberDataDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NumbersLightApi {

    @GET("/test/json.php")
    fun getNumbersData(): Single<ArrayList<NumberData>>

    @GET("/test/json.php")
    fun getNumberDataDetails(@Query("name") text: String): Single<NumberDataDetails>
}