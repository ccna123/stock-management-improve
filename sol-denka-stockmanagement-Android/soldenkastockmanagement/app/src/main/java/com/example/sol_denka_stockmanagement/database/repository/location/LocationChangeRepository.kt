package com.example.sol_denka_stockmanagement.database.repository.location

import android.os.Build
import androidx.room.withTransaction
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.database.AppDatabase
import com.example.sol_denka_stockmanagement.database.repository.tag.TagMasterRepository
import com.example.sol_denka_stockmanagement.model.location.LocationChangeEventModel
import com.example.sol_denka_stockmanagement.model.location.LocationChangeSessionModel
import com.example.sol_denka_stockmanagement.model.tag.TagMasterModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.forEach

@Singleton
class LocationChangeRepository @Inject constructor(
    private val db: AppDatabase,
    private val sessionRepo: LocationChangeSessionRepository,
    private val locationChangeEventRepository: LocationChangeEventRepository,
    private val tagMasterRepository: TagMasterRepository,
) {

    suspend fun createLocationChangeSession(): Int =
        sessionRepo.insert(
            LocationChangeSessionModel(
                deviceId = Build.ID,
                executedAt = generateIso8601JstTimestamp()
            )
        ).toInt()

    suspend fun insertLocationChangeEvent(
        sessionId: Int,
        memo: String,
        locationId: Int,
        rfidTagList: List<TagMasterModel>
    ) {
        rfidTagList.forEach { tag ->
            val ledgerId = tagMasterRepository.getLedgerIdByTagId(tag.tagId)
            locationChangeEventRepository.insert(
                LocationChangeEventModel(
                    locationChangeSessionId = sessionId,
                    ledgerItemId = ledgerId ?: 0,
                    locationId = locationId,
                    memo = memo,
                    scannedAt = generateIso8601JstTimestamp(),
                )
            )
        }
    }

    suspend fun saveLocationChangeTransaction(
        block: suspend () -> Unit
    ) = db.withTransaction { block() }
}
