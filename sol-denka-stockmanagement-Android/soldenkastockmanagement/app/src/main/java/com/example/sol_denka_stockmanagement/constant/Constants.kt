package com.example.sol_denka_stockmanagement.constant

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class ProcessMethod(val displayName: String) {
    USE("使用"),
    SALE("売却"),
    CRUSH("粉砕"),
    DISCARD("破棄"),
    PROCESS("加工"),
}


enum class PackingType(val displayName: String) {
    FLEXIBLE_CONTAINER_1T("1tフレコン"),
    PAPER_BAG_25KG("25Kg詰PPT防湿紙入り3層紙袋"),
}

enum class Tab(val displayName: String) {
    Left("left"),
    Right("right")
}

enum class SelectTitle(val displayName: String) {
    SelectLocation("保管場所選択"),
    SelectCsvType("CSVファイル種類選択"),
    SelectProcessMethod("処理方法選択"),
    SelectPackingStyle("荷姿選択"),
    SelectCategory("区分選択"),
    SelectWinder("巻き取り機選択"),
}

enum class CsvType(val displayName: String) {
    LocationMaster("保管場所マスタCSV"),
    LedgerMaster("台帳アイテムマスタCSV"),
    InventoryResult("棚卸結果データCSV"),
    InboundResult("入庫結果CSV"),
    OutboundResult("出庫結果CSV"),
    LocationChangeResult("保管場所変更CSV"),
    ItemTypeMaster("品目マスタCSV"),
    TagMaster("タグマスタCSV"),
    ItemTypeFieldSettingMaster("品目項目設定マスタCSV"),
}

enum class CsvTaskType(val displayNameJp: String) {
    IN("入庫"),
    OUT("出庫"),
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
enum class InventoryScanResult(val displayName: String) {
    OK("正常一致"),
    SHORTAGE("在庫不足"),
    OVERLOAD("在庫過剰"),
    WRONG_LOCATION("保管場所不一致")
}


enum class CsvHistoryDirection(val displayName: String) {
    EXPORT("EXPORT"),
    IMPORT("IMPORT")
}

enum class CsvHistoryResult(val displayName: String) {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE")
}

enum class InventoryResultType(val displayName: String) {
    UNKNOWN("未知"),
    FOUND_OK("正常"),
    FOUND_WRONG_LOCATION("保管場所不一致"),
    NOT_FOUND("不足"),
    FOUND_OVER_STOCK("過多"),
}

enum class ItemUnit(val engName: String, val jpName: String) {
    KG("KG", "kg"),
    TON("TON", "t"),
    MAI("MAI", "枚"),
    HON("HON","本"),
    KAN("KAN", "缶"),
    TAI("TAI", "袋"),
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

enum class DialogType{
    CONFIRM,
    SAVE_CSV_SUCCESS_FAILED_SFTP,
    SAVE_CSV_SEND_SFTP_SUCCESS,
    ERROR

}

enum class StatusCode(val code: Int) {
    OK(200),
    DOWNLOAD_SFTP_OK(201),
    IMPORT_OK(202),
    EXPORT_OK(203),
    FILE_NOT_FOUND(404),
    FILE_EMPTY(405),
    FOLDER_NOT_FOUND(406),
    WRITE_ERROR(407),
    EMPTY_DATA(408),
    PERMISSION_DENIED(409),
    FAILED(500),
    CSV_IMPORTER_NOT_FOUND(501),
    FILE_CREATED_FAILED(502),
    PROCESS_NOT_CHOSEN(503),
}

enum class TagStatus {
    PROCESSED,
    UNPROCESSED
}

enum class InboundResultCsvHeader {
    tag_id,
    item_type_id,
    winder_id,
    location_id,
    device_id,
    weight,
    thickness,
    width,
    length,
    quantity,
    occurrence_reason,
    lot_no,
    memo,
    occurred_at,
    processed_at,
    registerd_at
}

enum class OutboundResultCsvHeader {
    ledger_item_id,
    tag_id,
    process_type_id,
    device_id,
    memo,
    processed_at,
    registerd_at
}

enum class LocationChangeResultCsvHeader {
    ledger_item_id,
    location_id,
    device_id,
    memo,
    scanned_at,
    executed_at
}

enum class ScanMode {
    NONE,
    INBOUND,
    OUTBOUND,
    LOCATION_CHANGE,
    INVENTORY_SCAN,
    SEARCH
}

enum class Category(val displayName: String){
    SUB_MATERIAL("副資材"),
    SUB_RAW_MATERIAL("副原料"),
    NON_STANDARD_ITEM("格外品"),
    SEMI_FINISHED_PRODUCT("半製品"),
    INTERMEDIATE_PRODUCT("中間品"),
}

enum class InboundInputField(val displayName: String){
    WEIGHT("重量"),
    LENGTH("長さ"),
    WIDTH("巾"),
    THICKNESS("厚み"),
    SPECIFIC_GRAVITY("比重"),
    LOT_NO("Lot No"),
    WINDER_INFO("巻き取り機情報"),
    OCCURRENCE_REASON("発生理由"),
    MEMO("備考"),
    LOCATION("保管場所"),
    PACKING_TYPE("荷姿"),
    OCCURRED_AT("発生日時"),
    PROCESSED_AT("処理日時")
}

enum class DataType {
    TEXT,
    NUMBER,
    DATETIME
}

enum class ControlType{
    INPUT,
    DROPDOWN,
    DATETIMEPICKER
}

enum class WinderType(val displayName: String) {
    MACHINE_2("2号機"),
    SLITTING_B_F("BスリF"),
    SLITTING_B_B("BスリB"),
    MACHINE_3_LINE_1("3号機No.1"),
    MACHINE_3_LINE_2("3号機No.2"),
    MACHINE_4_LINE_1("4号機No.1"),
    MACHINE_4_LINE_2("4号機No.2"),
    OTHERS("その他")
}


fun generateTimeStamp(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")
    return LocalDateTime.now(ZoneId.of("Asia/Tokyo")).format(formatter)
}

fun generateIso8601JstTimestamp(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
    return OffsetDateTime.now(ZoneId.of("Asia/Tokyo")).format(formatter)
}

