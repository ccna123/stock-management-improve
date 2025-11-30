package com.example.sol_denka_stockmanagement.navigation


/**
 * Represents all destinations used in navigation.
 * Each screen has a unique routeId (used in NavHost)
 * and a displayName (used for UI titles, breadcrumbs, etc.)
 */
sealed class Screen(
    val routeId: String,
    val displayName: String
) {
    data object Home : Screen("home", "ホーム")
    data object Inventory : Screen("inventory", "棚卸")
    data object Inbound : Screen("inbound", "入庫")
    data object Outbound : Screen("outbound", "出庫")
    data object Setting : Screen("setting", "設定")
    data class InventoryScan(val preScreen: String) : Screen("inventory_scan", "棚卸")
    data object VersionInfo : Screen("version_info", "バージョン情報")
    data object LicenseInfo : Screen("license_info", "ライセンス情報")
    data object ReaderConnect : Screen("reader_connect", "リーダー接続")
    data class SearchTagsScreen(val preScreen: String) : Screen("search", "探索")
    data object CsvExport : Screen("csv_export", "CSVファイル出力")
    data object CsvImport : Screen("csv_import", "CSVファイル取り込み")
    data object InventoryComplete : Screen("inventory_complete", "棚卸 (完了)")
    data object StorageAreaChange : Screen("storage_area_change", "保管場所変更")
    data object Detail : Screen("detail", "詳細情報")
    data class Scan(val preScreen: String) : Screen("scan", "")

    companion object {
        /** Get destination by routeId */
        fun fromRouteId(id: String): Screen? = when (id) {
            Home.routeId -> Home
            Inventory.routeId -> Inventory
            Inbound.routeId -> Inbound
            Outbound.routeId -> Outbound
            Setting.routeId -> Setting
            VersionInfo.routeId -> VersionInfo
            LicenseInfo.routeId -> LicenseInfo
            ReaderConnect.routeId -> ReaderConnect
            CsvExport.routeId -> CsvExport
            CsvImport.routeId -> CsvImport
            InventoryComplete.routeId -> InventoryComplete
            Detail.routeId -> Detail
            StorageAreaChange.routeId -> StorageAreaChange
            else -> null
        }
    }
}