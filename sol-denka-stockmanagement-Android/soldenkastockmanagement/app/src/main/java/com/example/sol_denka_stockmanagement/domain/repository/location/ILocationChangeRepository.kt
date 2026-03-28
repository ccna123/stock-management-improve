package com.example.sol_denka_stockmanagement.domain.repository.location

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.domain.repository.tag.ITagMasterRepository
import com.example.sol_denka_stockmanagement.model.location.LocationChangeEventModel
import com.example.sol_denka_stockmanagement.model.location.LocationChangeSessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ILocationChangeRepository @Inject constructor(
    private val db: AppDatabase,
    private val sessionRepo: com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeSessionRepository,
    private val ILocationChangeEventRepository: com.example.sol_denka_stockmanagement.domain.repository.location.ILocationChangeEventRepository,
    private val ITagMasterRepository: ITagMasterRepository,
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
            val ledgerId = ITagMasterRepository.getLedgerIdByTagId(tag.tagId)
            ILocationChangeEventRepository.insert(
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
