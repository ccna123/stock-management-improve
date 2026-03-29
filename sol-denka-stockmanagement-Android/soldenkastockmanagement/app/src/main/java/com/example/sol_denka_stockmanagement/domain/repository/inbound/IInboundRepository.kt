package com.example.sol_denka_stockmanagement.domain.repository.inbound

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IInboundRepository @Inject constructor(
    private val db: AppDatabase,
    private val sessionRepo: IInboundSessionRepository,
    private val eventRepo: IInboundEventRepository,
) {

    suspend fun createInboundSession(executedAt: String): Int =
        sessionRepo.insert(
            _root_ide_package_.com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel(
                deviceId = Build.ID,
                executedAt = executedAt
            )
        ).toInt()

    suspend fun insertInboundEvent(
        sessionId: Int,
        winderId: Int?,
        locationId: Int,
        itemTypeId: Int,
        weight: Int?,
        width: Int?,
        length: Int?,
        thickness: BigDecimal?,
        lotNo: String?,
        occurrenceReason: String?,
        quantity: Int?,
        memo: String?,
        sourceEventId: String,
        occurredAt: String?,
        processedAt: String?,
        registeredAt: String,
        rfidTag: TagMasterModel?
    ) {
        eventRepo.insert(
            _root_ide_package_.com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel(
                inboundSessionId = sessionId,
                itemTypeId = itemTypeId,
                locationId = locationId,
                winderId = winderId,
                tagId = rfidTag?.tagId ?: 0,
                weight = weight,
                width = width,
                length = length,
                thickness = thickness,
                lotNo = lotNo,
                occurrenceReason = occurrenceReason,
                quantity = quantity,
                memo = memo,
                sourceEventId = sourceEventId,
                occurredAt = occurredAt,
                processedAt = processedAt,
                registeredAt = registeredAt
            )
        )
    }
    suspend fun saveInboundTransaction(
        block: suspend () -> Unit
    ) = db.withTransaction { block() }
}
