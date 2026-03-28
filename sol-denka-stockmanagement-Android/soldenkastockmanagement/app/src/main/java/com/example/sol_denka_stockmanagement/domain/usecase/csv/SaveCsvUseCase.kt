package com.example.sol_denka_stockmanagement.domain.usecase.csv

import android.content.Context
import com.example.sol_denka_stockmanagement.app_interface.ICsvExport
import com.example.sol_denka_stockmanagement.constant.CsvHistoryDirection
import com.example.sol_denka_stockmanagement.constant.CsvHistoryResult
import com.example.sol_denka_stockmanagement.constant.CsvTaskType
import com.example.sol_denka_stockmanagement.constant.generateIso8601JstTimestamp
import com.example.sol_denka_stockmanagement.domain.model.csv.CsvHistoryModel
import com.example.sol_denka_stockmanagement.domain.repository.csv.ICsvHistoryRepository
import com.example.sol_denka_stockmanagement.domain.repository.csv.ICsvTaskTypeRepository
import com.example.sol_denka_stockmanagement.exception.AppException
import com.example.sol_denka_stockmanagement.exception.EmptyDataException
import com.example.sol_denka_stockmanagement.helper.csv.CsvHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.collections.first

class SaveCsvUseCase @Inject constructor(
    private val csvHelper: CsvHelper,
    private val csvTaskTypeRepo: ICsvTaskTypeRepository,
    private val csvHistoryRepo: ICsvHistoryRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(
        taskCode: CsvTaskType,
        direction: CsvHistoryDirection,
        data: List<ICsvExport>,
        onProgress: (Float) -> Unit
    ): Result<Unit> {
        if (data.isEmpty()) return Result.failure(EmptyDataException())

        val first = data.first()
        var result = CsvHistoryResult.SUCCESS
        var errorMessage = ""
        var csvTaskTypeId: Int? = null

        return try {
            csvTaskTypeId = csvTaskTypeRepo.getIdByTaskCode(taskCode.name)
            csvHelper.saveCsv(
                context = context,
                csvType = first.toCsvType(),
                fileName = first.toCsvName(),
                rows = data,
                onProgress = onProgress
            )
            Result.success(Unit)
        } catch (e: AppException) {
            result = CsvHistoryResult.FAILURE
            errorMessage = e.message ?: ""
            Result.failure(e)
        } catch (e: Exception) {
            result = CsvHistoryResult.FAILURE
            errorMessage = e.message ?: ""
            Result.failure(e)
        } finally {
            csvTaskTypeId?.let { id ->
                runCatching {
                    csvHistoryRepo.insert(
                        CsvHistoryModel(
                            csvTaskTypeId = id,
                            fileName = first.toCsvName(),
                            direction = direction,
                            result = result,
                            recordNum = data.size,
                            errorMessage = errorMessage,
                            executedAt = generateIso8601JstTimestamp()
                        )
                    )
                }
            }
        }
    }
}