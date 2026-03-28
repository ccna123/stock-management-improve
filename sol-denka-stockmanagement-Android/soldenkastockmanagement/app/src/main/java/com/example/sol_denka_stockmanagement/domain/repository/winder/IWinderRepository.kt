package com.example.sol_denka_stockmanagement.domain.repository.winder

import com.example.sol_denka_stockmanagement.domain.model.winder.WinderModel
import kotlinx.coroutines.flow.Flow

interface IWinderRepository {
    fun get(): Flow<List<WinderModel>>
    suspend fun countRecord(): Int
    suspend fun getIdByName(winderName: String): Int?
    suspend fun insert(model: WinderModel): Long
    suspend fun update(model: WinderModel)
    suspend fun delete(model: WinderModel)
    suspend fun upsertAll(models: List<WinderModel>)
}