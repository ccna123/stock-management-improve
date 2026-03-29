package com.example.sol_denka_stockmanagement.domain.repository.location

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeEventModel
import com.example.sol_denka_stockmanagement.domain.model.location.LocationChangeSessionModel
import com.example.sol_denka_stockmanagement.domain.model.tag.TagMasterModel
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ILocationChangeRepository @Inject constructor(
    private val db: AppDatabase,
    private val sessionRepo: ILocationChangeSessionRepository,
    private val locationChangeEventRepository: ILocationChangeEventRepository,
    private val tagMasterRepository: ITagMasterRepository,
) {

    suspend fun createLocationChangeSession(executedAt: String): Int =
        sessionRepo.insert(
            LocationChangeSessionModel(
                deviceId = Build.ID,
                executedAt = executedAt
            )
        ).toInt()

    suspend fun insertLocationChangeEvent(
        sessionId: Int,
        memo: String,
        locationId: Int,
        scannedAt: String,
        sourceEventIdByTagId: Map<Int, String>,
        rfidTagList: List<TagMasterModel>
    ) {
        rfidTagList.forEach { tag ->
            val ledgerId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
            locationChangeEventRepository.insert(
                LocationChangeEventModel(
                    locationChangeSessionId = sessionId,
                    ledgerItemId = ledgerId,
                    locationId = locationId,
                    sourceEventId = sourceEventIdByTagId[tag.tagId]!!,
                    memo = memo,
                    scannedAt = scannedAt,
                )
            )
        }
    }

    suspend fun saveLocationChangeTransaction(
        block: suspend () -> Unit
    ) = db.withTransaction { block() }
}
