package com.example.numberslight.service.usecase

import com.example.numberslight.service.model.NumberDataDetails
import com.example.numberslight.service.repository.Repository
import io.reactivex.Single

class GetNumberDetailsUseCase(override var repository: Repository, val name: String): BaseSingleUsecase<NumberDataDetails>() {

    override fun executeResponse(): Single<NumberDataDetails> {
        return repository.getNumberDetails(name)
    }
}