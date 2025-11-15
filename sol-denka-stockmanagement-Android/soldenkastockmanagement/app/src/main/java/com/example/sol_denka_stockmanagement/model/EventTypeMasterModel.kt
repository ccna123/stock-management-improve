package com.example.sol_denka_stockmanagement.model

import com.example.sol_denka_stockmanagement.constant.EventTypeCode
import com.example.sol_denka_stockmanagement.database.entity.EventTypeMasterEntity

data class EventTypeMasterModel(
    val id: Int,
    val eventTypeCode: EventTypeCode,
    val eventTypeName: String
)

fun EventTypeMasterModel.toEventTypeMasterEntity(): EventTypeMasterEntity{
    return EventTypeMasterEntity(
        id = this.id,
        eventTypeCode = this.eventTypeCode,
        eventTypeName = this.eventTypeName
    )
}

fun EventTypeMasterEntity.toEventTypeMasterModel(): EventTypeMasterModel{
    return EventTypeMasterModel(
        id = this.id,
        eventTypeCode = this.eventTypeCode,
        eventTypeName = this.eventTypeName
    )
}

