package com.example.sol_denka_stockmanagement.database.repository.inbound

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.inbound.InboundEventModel
import com.example.sol_denka_stockmanagement.model.inbound.InboundSessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.let
import kotlin.takeIf
import kotlin.text.isNotBlank
import kotlin.text.toInt

@Singleton
class InboundRepository @Inject constructor(
    private val db: AppDatabase,
    private val sessionRepo: InboundSessionRepository,
    private val eventRepo: InboundEventRepository,
    private val tagMasterRepository: TagMasterRepository,
) {

    suspend fun saveInboundToDb(
        weight: String,
        grade: String,
        specificGravity: String,
        thickness: String,
        width: String,
        length: String,
        quantity: String,
        winderInfo: String,
        occurrenceReason: String,
        memo: String,
        rfidTag: TagMasterModel?
    ): Int = db.withTransaction {
        val sessionId = sessionRepo.insert(
            InboundSessionModel(
                deviceId = Build.ID,
                executedAt = generateIso8601JstTimestamp(),
            )
        )
        sessionId.let {
            val (itemTypeId, locationId) = tagMasterRepository.getItemTypeIdLocationIdByTagId(
                rfidTag?.tagId ?: 0
            )
            eventRepo.insert(
                InboundEventModel(
                    inboundSessionId = sessionId.toInt(),
                    itemTypeId = itemTypeId,
                    locationId = locationId,
                    tagId = rfidTag?.tagId ?: 0,
                    weight = weight.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                    grade = grade,
                    specificGravity = specificGravity,
                    thickness = thickness.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                    width =  width.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                    length = length.takeIf { it.isNotBlank() }?.toInt() ?: 0,
                    quantity = 0,
                    winderInfo = winderInfo,
                    occurrenceReason = occurrenceReason,
                    memo = memo,
                    occurredAt = generateIso8601JstTimestamp(),
                    registeredAt = generateIso8601JstTimestamp()
                )
            )
            sessionId.toInt()
        }
    }
}
