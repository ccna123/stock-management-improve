package com.example.sol_denka_stockmanagement.constant

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class ProcessMethod(val displayName: String) {
    USE("使用"),
    SALE("売却"),
    CRUSH("粉砕")
}

enum class MaterialSelectionItem(val displayName: String) {
    MISS_ROLL("ミスロール"),
    PAPER_CORE("紙管"),
    PACKING_STYLE("荷姿"),
    LITER_CAN("一斗缶"),
    PELLET("ペレット"),
}

enum class PackingStyleItem(val displayName: String) {
    SELECTION_TITLE("荷姿選択"),
    FLEXIBLE_CONTAINER_1T("1tフレコン"),
    PAPER_BAG_25KG("25Kg詰PPT防湿紙入り3層紙袋"),
}

enum class Tab(val displayName: String) {
    Left("left"),
    Right("right")
}

enum class SelectTitle(val displayName: String) {
    SelectMissRoll("資材選択"),
    SelectLocation("保管場所選択"),
    SelectCsvType("CSVファイル種類選択"),
    SelectProcessMethod("処理方法選択"),
    SelectPackingStyle("荷姿選択"),
    SelectMaterial("資材選択"),
}

enum class CsvType(val displayName: String) {
    LocationMaster("保管場所マスタCSV"),
    LedgerMaster("台帳アイテムマスタCSV"),
    InventoryResult("棚卸結果データCSV"),
    StockEvent("入出庫イベントデータCSV"),
    InboundResult("入庫結果CSV"),
    OutboundResult("出庫結果CSV"),
    LocationChange("保管場所変更CSV"),
    ItemTypeMaster("品目マスタCSV"),
    TagMaster("タグマスタCSV"),
}

enum class InventoryResultTypeCode(val displayName: String) {
    FOUND_OK("FOUND_OK"),
    FOUND_WRONG_LOCATION("FOUND_WRONG_LOCATION"),
    NOT_FOUND("NOT_FOUND"),
}

enum class CsvTaskType(val displayName: String) {
    IN_OUT("入庫"),
    INVENTORY("棚卸"),
    LOCATION_CHANGE("保管場所変更"),
    UPPER_SYSTEM("上位システム"),
    OTHER("その他"),
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

enum class MaterialMasterCsvHeader {
    id,
    material_code,
    material_name,
    created_at,
    updated_at
}

enum class StorageAreaMasterCsvHeader {
    id,
    location_code,
    location_name,
    created_at,
    updated_at
}

enum class StockMasterCsvHeader {
    material_code,
    stock_quantity,
    scanned_at
}

enum class LedgerMasterCsvHeader {
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

enum class InventoryScanResult(val displayName: String) {
    OK("正常"),
    SHORTAGE("在庫不足"),
    OVERLOAD("在庫過多"),
    WRONG_LOCATION("保管場所不一致")
}

enum class EventTypeCode(val displayName: String) {
    IN("IN"),
    OUT("OUT"),
    MOVE("MOVE"),
    ADJUST("ADJUST"),
    INVENTORY("INVENTORY"),
}

enum class CsvHistoryDirection(val displayName: String) {
    EXPORT("EXPORT"),
    IMPORT("IMPORT")
}

enum class CsvHistoryTarget(val displayName: String) {
    WINDOWS("Windows"),
    BACKUP("Backup")
}

enum class CsvHistoryResult(val displayName: String) {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE")
}

enum class InventoryResultType(val displayName: String) {
    FOUND_OK("正常"),
    FOUND_WRONG_LOCATION("保管場所不一致"),
    NOT_FOUND("不足"),
    FOUND_OVER_STOCK("過多"),
}

enum class ItemUnit(val displayName: String) {
    KG("KG"),
    TON("TON"),
    MAI("MAI"),
    HON("HON"),
}

enum class BeeperVolume(val displayName: String) {
    QUIET_BEEP("静音"),
    LOW_BEEP("小"),
    MEDIUM_BEEP("中"),
    HIGH_BEEP("高"),
}

enum class FileTransferMethod(val displayName: String) {
    SELECTION_TITLE("ファイル転送方法選択"),
    USB("USB"),
    WIFI("WIFI")
}

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED
}

enum class StatusCode {
    OK,
    FAILED
}

enum class TagStatus {
    PROCESSED,
    UNPROCESSED
}

enum class InboundResultCsvHeader {
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

enum class OutboundResultCsvHeader {
    ledger_item_id,
    tag_id,
    process_type_id,
    device_id,
    memo,
    occurred_at,
    registerd_at
}

enum class ScanMode {
    NONE,
    INBOUND,
    OUTBOUND,
    LOCATION_CHANGE,
    INVENTORY_SCAN,
    SEARCH
}

fun generateTimeStamp(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")
    return LocalDateTime.now(ZoneId.of("Asia/Tokyo")).format(formatter)
}
