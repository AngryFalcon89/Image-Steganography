package com.example.stagenography.domain.usecase

import com.example.stagenography.domain.model.SteganographyParams
import com.example.stagenography.domain.repository.SteganographyRepository


class DecodeImageUseCase(private val repository: SteganographyRepository) {

    suspend operator fun invoke(filePath: String): SteganographyParams {
        return repository.decodeImage(filePath)
    }
}