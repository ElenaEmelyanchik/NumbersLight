package com.example.numberslight.service.usecase

import com.example.numberslight.service.model.NumberData
import com.example.numberslight.service.repository.Repository
import io.reactivex.Single

class GetNumbersDataUseCase(override var repository: Repository): BaseSingleUsecase<ArrayList<NumberData>>() {

    override fun executeResponse(): Single<ArrayList<NumberData>> {
        return repository.getNumbers()
    }
}