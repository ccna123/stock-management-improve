package com.example.sol_denka_stockmanagement.database.repository.tag

import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.database.dao.tag.TagDao
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.model.common.AdditionalFieldsModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.model.tag.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepository @Inject constructor(
    private val dao: TagDao
) {
    private val _mockData = MutableStateFlow(
        listOf(
            "80000000", "81111111", "82222222", "83333333",
            "84444444", "85555555", "86666666", "87777777",
            "88888888", "89999999",
        ).mapIndexed { index, epc ->
            TagMasterModel(
                tagId = index+1,
                ledgerItemId = null,
                epc = epc,
                createdAt = "2025-11-12T10:00:00+09:00",
                updatedAt = "2025-11-12T12:00:00+09:00",
                newFields = AdditionalFieldsModel(
                    tagStatus = TagStatus.UNPROCESSED,
                    rssi = -100f
                )
            )
        }
    ).asStateFlow()

    fun get(): Flow<List<TagMasterModel>> = _mockData
    suspend fun insert(model: TagMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: TagMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: TagMasterModel) = dao.delete(model.toEntity())
}
