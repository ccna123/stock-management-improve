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
    private val locationMasterRepository: LocationMasterRepository,
    private val locationChangeEventRepository: LocationChangeEventRepository,
    private val tagMasterRepository: TagMasterRepository,
) {

    suspend fun saveLocationChangeToDb(
        memo: String,
        newLocation: String,
        rfidTagList: List<TagMasterModel>
    ): Int = db.withTransaction {
        val sessionId = sessionRepo.insert(
            LocationChangeSessionModel(
                deviceId = Build.ID,
                executedAt = generateIso8601JstTimestamp()
            )
        )
        val newLocationId = locationMasterRepository.getLocationIdByName(newLocation)
        rfidTagList.forEach { row ->
            val ledgerId = tagMasterRepository.getLedgerIdByEpc(row.epc)
            locationChangeEventRepository.insert(
                LocationChangeEventModel(
                    locationChangeSessionId = sessionId.toInt(),
                    ledgerItemId = ledgerId ?: 0,
                    locationId = newLocationId ?: 0,
                    memo = memo,
                    scannedAt = generateIso8601JstTimestamp(),
                )
            )
        }
        sessionId.toInt()
    }
}
