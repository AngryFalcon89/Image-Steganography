package com.example.stagenography.domain.usecase

import com.example.stagenography.domain.model.SteganographyParams
import com.example.stagenography.domain.repository.SteganographyRepository

class EncodeImageUseCase(private val repository: SteganographyRepository) {

    suspend operator fun invoke(filePath: String, message: String): SteganographyParams {
        return repository.encodeImage(filePath, message)
    }
}