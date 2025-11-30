package com.example.sol_denka_stockmanagement.constant

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class CommonsString(val displayName: String) {
    InvalidInput("Invalid Input")
}

enum class FileTransferMethod(val displayName: String) {
    SELECTION_TITLE("ファイル転送方法選択"),
    USB("USB"),
    WIFI("WIFI")
}

enum class HandlingMethod(val displayName: String) {
    USE("使用"),
    SALE("売却"),
    CRUSHING("粉砕")
}

enum class MaterialSelectionItem(val displayName: String) {
    MISS_ROLL("ミスロール"),
    PACKING_STYLE("荷姿"),
    LITER_CAN("一斗缶"),
    PELLET("ペレット"),
}
enum class PackingStyleItem(val displayName: String) {
    FLEXIBLE_CONTAINER_1T("1tフレコン"),
    PAPER_BAG_25KG("25Kg詰PPT防湿紙入り3層紙袋"),
}

enum class StockAreaItem(val displayName: String) {
    STOCK_AREA1("保管場所1"),
    STOCK_AREA2("保管場所2"),
    STOCK_AREA3("保管場所3"),
    STOCK_AREA4("保管場所4"),
    STOCK_AREA5("保管場所5"),
}
enum class Tab(val displayName: String) {
    Left("left"),
    Right("right")
}

enum class SelectTitle(val displayName: String) {
    SelectMissRoll("資材選択"),
    SelectLocation("保管場所選択"),
    SelectCsvType("CSVファイル種類選択"),
    SelectHandlingMethod("処理方法選択"),
    SelectPackingStyle("荷姿選択"),
    SelectMaterial("資材選択"),
}

enum class CsvType(val displayName: String) {
    MaterialMaster("資材マスタCSV"),
    StorageAreaMaster("保管場所マスタCSV"),
    LedgerMaster("台長アイテムマスタCSV"),
    InventoryResult("棚卸結果データCSV"),
    StockEvent("入出庫イベントデータCSV"),
    InboundResult("入庫結果CSV"),
    OutboundResult("出庫結果CSV"),
    LocationChange("保管場所変更CSV"),
}
enum class StockInOutEventCsvHeader {
    item_id,
    material_id,
    performed_location_id,
    from_location_id,
    to_location_id,
    epc,
    is_present,
    memo,
    occured_at,
    registered_at,
}

enum class InventoryResultCsvHeader {
    location_id,
    inventory_result_type_id,
    ledger_item_id,
    tag_id,
    device_id,
    memo,
    scanned_at,
    executed_at
}

enum class MaterialMasterCsvHeader{
    id,
    material_code,
    material_name,
    created_at,
    updated_at
}

enum class StorageAreaMasterCsvHeader{
    id,
    location_code,
    location_name,
    created_at,
    updated_at
}

enum class StockMasterCsvHeader{
    material_code,
    stock_quantity,
    scanned_at
}

enum class LedgerMasterCsvHeader{
    id,
    material_id,
    location_id,
    item_name,
    epc,
    is_present,
    status,
    created_at,
    updated_at
}

enum class InboundResultCsvHeader{
    tag_id,
    item_type_id,
    location_id,
    device_id,
    weight,
    grade,
    specific_gravity,
    thickness,
    width,
    length,
    quantity,
    winder_info,
    missroll_reason,
    occurred_at,
    registerd_at
}

enum class OutboundResultCsvHeader{
    ledger_item_id,
    tag_id,
    process_id,
    device_id,
    memo,
    occurred_at,
    registerd_at
}

enum class InventoryScanResult(val displayName: String) {
    OK("正常"),
    SHORTAGE("在庫不足"),
    OVERLOAD("在庫過多"),
    WRONG_LOCATION("保管場所不一致")
}

enum class EventTypeCode(val displayName: String){
    IN("IN"),
    OUT("OUT"),
    MOVE("MOVE"),
    ADJUST("ADJUST"),
    INVENTORY("INVENTORY"),
}

enum class CsvHistoryDirection(val displayName: String){
    EXPORT("EXPORT"),
    IMPORT("IMPORT")
}
enum class CsvHistoryTarget(val displayName: String){
    WINDOWS("Windows"),
    BACKUP("Backup")
}
enum class CsvHistoryResult(val displayName: String){
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE")
}
enum class InventoryResultCode(val displayName: String) {
    FOUND_OK("FOUND_OK"),
    FOUND_WRONG_LOCATION("FOUND_WRONG_LOCATION"),
    NOT_FOUND("NOT_FOUND"),
}
enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED
}

enum class StatusCode{
    OK,
    FAILED
}

enum class TagStatus{
    PROCESSED,
    UNPROCESSED
}

fun generateTimeStamp(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")
    return LocalDateTime.now(ZoneId.of("Asia/Tokyo")).format(formatter)
}
