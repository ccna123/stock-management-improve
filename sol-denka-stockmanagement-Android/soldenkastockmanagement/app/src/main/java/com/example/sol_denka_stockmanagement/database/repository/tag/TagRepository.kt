package com.example.sol_denka_stockmanagement.database.repository.tag

import com.example.sol_denka_stockmanagement.constant.TagStatus
import com.example.sol_denka_stockmanagement.constant.generateTimeStamp
import com.example.sol_denka_stockmanagement.database.dao.tag.TagDao
import com.example.sol_denka_stockmanagement.database.entity.tag.TagMasterEntity
import com.example.sol_denka_stockmanagement.model.common.AdditionalFieldsModel
import com.example.sol_denka_stockmanagement.model.item.toEntity
import com.example.sol_denka_stockmanagement.model.item.toModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.model.tag.toEntity
import com.example.sol_denka_stockmanagement.model.tag.toModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepository @Inject constructor(
    private val dao: TagDao
) {

    val presetTags = listOf(
        TagMasterModel(
            1,
            1,
            "80000000",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
        TagMasterModel(
            2,
            2,
            "81111111",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
        TagMasterModel(
            3,
            3,
            "82222222",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
        TagMasterModel(
            4,
            4,
            "83333333",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
        TagMasterModel(
            5,
            5,
            "84444444",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
        TagMasterModel(
            6,
            6,
            "85555555",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
        TagMasterModel(
            7,
            7,
            "86666666",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
        TagMasterModel(
            8,
            8,
            "87777777",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
        TagMasterModel(
            9,
            9,
            "88888888",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
        TagMasterModel(
            10,
            10,
            "89999999",
            generateTimeStamp(),
            generateTimeStamp(),
            newFields = AdditionalFieldsModel(
                tagStatus = TagStatus.UNPROCESSED,
                rssi = -100f
            )
        ),
    )

    private val _mockData = MutableStateFlow(
        listOf(
            "80000000", "81111111", "82222222", "83333333",
            "84444444", "85555555", "86666666", "87777777",
            "88888888", "89999999",
        ).mapIndexed { index, epc ->
            TagMasterModel(
                tagId = index + 1,
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

    init {
        CoroutineScope(Dispatchers.IO).launch {
            ensurePresetInserted()
        }
    }

    private suspend fun ensurePresetInserted() {
        val existing = dao.get().firstOrNull() ?: emptyList()

        if (existing.isEmpty()) {
            presetTags.forEach { dao.insert(it.toEntity()) }
            println("📦 [ItemTypeRepository] Preset ItemTypes inserted")
        } else {
            println("📦 [ItemTypeRepository] Preset already exists → skip")
        }
    }


    fun get(): Flow<List<TagMasterModel>> = dao.get().map { list -> list.map { it.toModel() } }
    suspend fun insert(model: TagMasterModel) = dao.insert(model.toEntity())
    suspend fun update(model: TagMasterModel) = dao.update(model.toEntity())
    suspend fun delete(model: TagMasterModel) = dao.delete(model.toEntity())
}
