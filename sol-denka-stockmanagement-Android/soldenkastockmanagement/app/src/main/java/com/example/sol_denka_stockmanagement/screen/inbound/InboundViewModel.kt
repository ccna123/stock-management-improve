package com.example.sol_denka_stockmanagement.screen.inbound

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.repository.inbound.InboundRepository
import com.example.sol_denka_stockmanagement.database.repository.item.ItemTypeRepository
import com.example.sol_denka_stockmanagement.database.repository.location.LocationMasterRepository
import com.example.sol_denka_stockmanagement.database.repository.winder.WinderInfoRepository
import com.example.sol_denka_stockmanagement.model.csv.InboundResultCsvModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltViewModel
class InboundViewModel @Inject constructor(
    private val inboundRepository: InboundRepository,
    private val winderInfoRepository: WinderInfoRepository,
    private val itemTypeRepository: ItemTypeRepository,
    private val locationMasterRepository: LocationMasterRepository
) : ViewModel() {

    private val csvModels = mutableListOf<InboundResultCsvModel>()

    suspend fun generateCsvData(
        itemInCategory: String,
        location: String,
        winder: String?,
        weight: String,
        width: String,
        length: String,
        thickness: String,
        lotNo: String,
        occurrenceReason: String,
        quantity: String,
        memo: String,
        occurredAt: String,
        processedAt: String,
        rfidTag: TagMasterModel?
    ): List<InboundResultCsvModel> =
        withContext(Dispatchers.IO) {
            csvModels.clear()
            val itemTypeId = itemTypeRepository.getItemTypeIdByItemName(itemName = itemInCategory)
            val locationId = locationMasterRepository.getLocationIdByName(locationName = location)
            val winderId = winderInfoRepository.getIdByName(winderName = winder ?: "")
            val model = InboundResultCsvModel(
                tagId = rfidTag?.tagId ?: 0,
                itemTypeId = itemTypeId,
                locationId = locationId,
                winderId = winderId,
                deviceId = Build.ID,
                weight = weight,
                width = width,
                length = length,
                thickness = thickness,
                lotNo = lotNo,
                occurrenceReason = occurrenceReason,
                quantity = quantity,
                memo = memo,
                occurredAt = occurredAt,
                processedAt = processedAt,
                registeredAt = generateIso8601JstTimestamp()
            )
            csvModels.add(model)
            csvModels.toList()
        }

    suspend fun saveInboundToDb(
        itemInCategory: String,
        location: String,
        winder: String?,
        weight: String?,
        thickness: String?,
        width: String?,
        length: String?,
        quantity: String?,
        lotNo: String?,
        occurrenceReason: String?,
        memo: String?,
        occurredAt: String?,
        processedAt: String?,
        registeredAt: String,
        rfidTag: TagMasterModel?
    ): Result<Int> {
        return try {
            var sessionId = 0

            inboundRepository.saveInboundTransaction {

                // 1️⃣ create session
                sessionId = inboundRepository.createInboundSession()
                val winderId = winderInfoRepository.getIdByName(winderName = winder ?: "")
                val itemTypeId = itemTypeRepository.getItemTypeIdByItemName(itemName = itemInCategory)
                val locationId = locationMasterRepository.getLocationIdByName(locationName = location)

                // 2️⃣ insert event (only if session OK)
                inboundRepository.insertInboundEvent(
                    sessionId = sessionId,
                    winderId = winderId,
                    itemTypeId = itemTypeId,
                    locationId = locationId,
                    weight = weight?.takeIf { it.isNotBlank() }?.toInt(),
                    width = width?.takeIf { it.isNotBlank() }?.toInt(),
                    length = length?.takeIf { it.isNotBlank() }?.toInt(),
                    thickness = thickness?.takeIf { it.isNotBlank() }?.toInt(),
                    lotNo = lotNo?.takeIf { it.isNotBlank() },
                    occurrenceReason = occurrenceReason?.takeIf { it.isNotBlank() },
                    quantity = quantity?.takeIf { it.isNotBlank() }?.toInt(),
                    memo = memo?.takeIf { it.isNotBlank() },
                    occurredAt = occurredAt?.takeIf { it.isNotBlank() },
                    processedAt = processedAt?.takeIf { it.isNotBlank() },
                    registeredAt = registeredAt,
                    rfidTag = rfidTag
                )
            }
            Result.success(sessionId)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}