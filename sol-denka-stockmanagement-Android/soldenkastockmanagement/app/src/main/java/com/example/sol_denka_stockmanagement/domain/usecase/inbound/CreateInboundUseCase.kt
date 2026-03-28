package com.example.sol_denka_stockmanagement.domain.usecase.inbound

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundEventModel
import com.example.sol_denka_stockmanagement.domain.model.inbound.InboundSessionModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.inbound.IInboundEventRepository
import com.example.sol_denka_stockmanagement.domain.repository.inbound.IInboundSessionRepository
import java.math.BigDecimal
import javax.inject.Inject

class CreateInboundUseCase @Inject constructor(
    private val sessionRepo: IInboundSessionRepository,
    private val eventRepo: IInboundEventRepository,
    private val db: AppDatabase
) {
    suspend fun createSession(executedAt: String): Int =
        sessionRepo.insert(
            InboundSessionModel(
                deviceId = Build.ID,
                executedAt = executedAt
            )
        ).toInt()

    suspend fun insertEvent(
        sessionId: Int,
        locationId: Int,
        itemTypeId: Int,
        winderId: Int?,
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
            InboundEventModel(
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

    suspend fun withTransaction(block: suspend () -> Unit) =
        db.withTransaction { block() }
}