package com.example.sol_denka_stockmanagement.domain.usecase.location

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeEventModel
import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeSessionModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeEventRepository
import com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeSessionRepository
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import javax.inject.Inject

class SaveLocationChangeUseCase @Inject constructor(
    private val sessionRepo: ILocationChangeSessionRepository,
    private val eventRepo: ILocationChangeEventRepository,
    private val tagRepo: ITagMasterRepository,
    private val db: AppDatabase
) {
    suspend fun createSession(executedAt: String): Int =
        sessionRepo.insert(
            LocationChangeSessionModel(
                deviceId = Build.ID,
                executedAt = executedAt
            )
        ).toInt()

    suspend fun insertEvents(
        sessionId: Int,
        memo: String,
        locationId: Int,
        scannedAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ) {
        rfidTagList.forEach { tag ->
            val ledgerId = tagRepo.getLedgerIdByTagId(tag.tagId)
            eventRepo.insert(
                LocationChangeEventModel(
                    locationChangeSessionId = sessionId,
                    ledgerItemId = ledgerId,
                    locationId = locationId,
                    sourceEventId = sourceEventIdByTagId[tag.tagId]!!,
                    memo = memo,
                    scannedAt = scannedAt
                )
            )
        }
    }

    suspend fun withTransaction(block: suspend () -> Unit) =
        db.withTransaction { block() }
}