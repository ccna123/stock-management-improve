package com.example.sol_denka_stockmanagement.database.dao.field

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.sol_denka_stockmanagement.app_interface.IDao
import com.example.sol_denka_stockmanagement.database.entity.field.ItemTypeFieldSettingMasterEntity
import com.example.sol_denka_stockmanagement.model.inbound.InboundInputFormModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemTypeFieldSettingMasterDao: IDao<ItemTypeFieldSettingMasterEntity> {

    @Query("SELECT * FROM ItemTypeFieldSettingMaster")
    override fun get(): Flow<List<ItemTypeFieldSettingMasterEntity>>

    @Query("""
        SELECT 
        f.field_name AS fieldName, 
        f.field_code AS fieldCode,
        f.control_type AS controlType,  
        f.data_type AS dataType, 
        s.is_required AS isRequired, 
        s.is_visible AS isVisible
        FROM itemtypefieldsettingmaster s
        INNER JOIN itemtypemaster it ON it.item_type_id = s.item_type_id
        INNER JOIN fieldmaster f ON f.field_id = s.field_id
        WHERE s.item_type_id = :id
    """)
    suspend fun getFieldForItemTypeByItemTypeId(id: Int): List<InboundInputFormModel>


    @Insert(onConflict = REPLACE)
    override suspend fun insert(e: ItemTypeFieldSettingMasterEntity): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(e: List<ItemTypeFieldSettingMasterEntity>)

    @Update
    override suspend fun update(e: ItemTypeFieldSettingMasterEntity)

    @Delete
    override suspend fun delete(e: ItemTypeFieldSettingMasterEntity)
}
