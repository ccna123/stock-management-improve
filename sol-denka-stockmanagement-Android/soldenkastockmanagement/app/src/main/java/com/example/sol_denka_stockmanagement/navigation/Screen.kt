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
    data object Receiving : Screen("receiving", "入庫")
    data object Shipping : Screen("shipping", "出庫")
    data object Setting : Screen("setting", "設定")
    data class InventoryScan(val preScreen: String) : Screen("inventory_scan", "棚卸")
    data object VersionInfo : Screen("version_info", "バージョン情報")
    data object LicenseInfo : Screen("license_info", "ライセンス情報")
    data object ReaderConnect : Screen("reader_connect", "リーダー接続")
    data object ReceivingScan : Screen("receiving_scan", "入庫")
    data object ShippingScan : Screen("shipping_scan", "出庫")
    data class SearchTagsScreen(val preScreen: String) : Screen("search", "探索")
    data object CsvExport : Screen("csv_export", "CSVファイル出力")
    data object CsvImport : Screen("csv_import", "CSVファイル取り込み")
    data object InventoryComplete : Screen("inventory_complete", "棚卸 (完了)")
    data object StockArea : Screen("stock_area", "保管場所変更")
    data object Detail : Screen("detail", "詳細情報")

    companion object {
        /** Get destination by routeId */
        fun fromRouteId(id: String): Screen? = when (id) {
            Home.routeId -> Home
            Inventory.routeId -> Inventory
            Receiving.routeId -> Receiving
            Shipping.routeId -> Shipping
            Setting.routeId -> Setting
            VersionInfo.routeId -> VersionInfo
            LicenseInfo.routeId -> LicenseInfo
            ReaderConnect.routeId -> ReaderConnect
            CsvExport.routeId -> CsvExport
            CsvImport.routeId -> CsvImport
            InventoryComplete.routeId -> InventoryComplete
            Detail.routeId -> Detail
            ReceivingScan.routeId -> ReceivingScan
            ShippingScan.routeId -> ShippingScan
            else -> null
        }
    }
}