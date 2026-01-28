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
    SelectPackingType("荷姿選択"),
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
    ReferenceMaster("参照マスタZIP"),
}

enum class CsvTaskType(val displayNameJp: String) {
    IN("入庫"),
    OUT("出庫"),
    INVENTORY("棚卸"),
    LOCATION_CHANGE("保管場所変更"),
    UPPER_SYSTEM("上位システム"),
    OTHER("その他"),
}

enum class CsvFileType(){
    SINGLE_FILE,
    ZIP_FILE
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
    HON("HON", "本"),
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

enum class DialogType {
    CONFIRM,
    SAVE_CSV_SUCCESS_FAILED_SFTP,
    SAVE_CSV_SEND_SFTP_SUCCESS,
    ERROR

}

enum class StatusCode() {
    OK,
    DOWNLOAD_SFTP_OK,
    IMPORT_OK,
    IMPORT_FAILED,
    EXPORT_OK,
    FILE_NOT_FOUND,
    FILE_EMPTY,
    FOLDER_NOT_FOUND,
    WRITE_ERROR,
    EMPTY_DATA,
    PERMISSION_DENIED,
    FAILED,
    CSV_IMPORTER_NOT_FOUND,
    FILE_CREATED_FAILED,
    PROCESS_NOT_CHOSEN,
    CANCEL,
    MISSING_COLUMN,
    SQLITE_CONSTRAINT_ERROR,
    CSV_SCHEMA_ERROR,
    REFERENCE_MASTER_MISSING_FILE,
    MISSING_HEADER
}

enum class TagScanStatus {
    PROCESSED,
    UNPROCESSED
}
enum class ScanMode {
    NONE,
    INBOUND,
    OUTBOUND,
    LOCATION_CHANGE,
    INVENTORY_SCAN,
    SEARCH
}

enum class Category(val displayName: String) {
    SUB_MATERIAL("副資材"),
    SUB_RAW_MATERIAL("副原料"),
    NON_STANDARD_ITEM("格外品"),
    SEMI_FINISHED_PRODUCT("半製品"),
    INTERMEDIATE_PRODUCT("中間品"),
}

enum class InboundInputField(val displayName: String, val code: String) {
    WEIGHT("重量", "Weight"),
    LENGTH("長さ", "Length"),
    WIDTH("巾", "Width"),
    THICKNESS("厚み", "Thickness"),
    SPECIFIC_GRAVITY("比重", "SpecificGravity"),
    LOT_NO("Lot No", "LotNo"),
    WINDER("巻き取り機", "Winder"),
    OCCURRENCE_REASON("発生理由", "OccurrenceReason"),
    MEMO("備考", "Memo"),
    LOCATION("保管場所", "Location"),
    PACKING_TYPE("荷姿", "PackingType"),
    OCCURRED_AT("発生日時", "OccurredAt"),
    PROCESSED_AT("処理日時", "ProcessedAt"),
    QUANTITY("数量", "Quantity"),
}

enum class DataType {
    TEXT,
    NUMBER,
    DATETIME
}

enum class ControlType {
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

enum class TagStatus(val statusName: String, val statusCode: String){
    UNASSIGNED("未使用", "UNASSIGNED"),
    ATTACHED("紐づけ済み", "ATTACHED"),
    DETACHED("紐づけ解除済み", "DETACHED"),
    DISABLED("無効", "DISABLED"),
}


fun generateTimeStamp(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS")
    return LocalDateTime.now(ZoneId.of("Asia/Tokyo")).format(formatter)
}

fun generateIso8601JstTimestamp(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    return OffsetDateTime.now(ZoneId.of("Asia/Tokyo")).format(formatter)
}

