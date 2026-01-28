package com.example.sol_denka_stockmanagement.helper.message_mapper

import com.example.sol_denka_stockmanagement.constant.StatusCode

object MessageMapper {
    fun toMessage(code: StatusCode, params: Map<String, Any>? = null): String {
        return when (code) {

            StatusCode.OK,
            StatusCode.DOWNLOAD_SFTP_OK ->
                "処理が正常に完了しました。"

            StatusCode.FILE_NOT_FOUND ->
                "CSVファイルが見つかりません。"

            StatusCode.FILE_EMPTY ->
                "CSVファイルの内容が空です。"

            StatusCode.FOLDER_NOT_FOUND ->
                "保存先フォルダーが見つかりません。"

            StatusCode.WRITE_ERROR ->
                "CSVファイルの書き込みに失敗しました。"

            StatusCode.EMPTY_DATA ->
                "保存するデータがありません。"

            StatusCode.PERMISSION_DENIED ->
                "権限がないため実行できません。"

            StatusCode.CSV_IMPORTER_NOT_FOUND ->
                "CSVインポーターが見つかりません。"

            StatusCode.FILE_CREATED_FAILED ->
                "CSVファイルの作成に失敗しました。"

            StatusCode.FAILED ->
                "エラーが発生しました。"

            StatusCode.SQLITE_CONSTRAINT_ERROR -> "外部キーエラー"

            StatusCode.PROCESS_NOT_CHOSEN -> "チェック済みのタグに処理方法を選択してください。"

            StatusCode.IMPORT_OK -> "CSVファイルの取込に成功しました。"
            StatusCode.EXPORT_OK -> "CSV ファイルの保存は正常に完了しましたが、\n送信処理でエラーが発生しました。再度送信をお試しください。"
            StatusCode.CANCEL -> "登録作業をキャンセルし、\nホーム画面に戻ってもよろしいですか？"
            StatusCode.MISSING_COLUMN -> {
                val missing = params?.get("missing_headers") as? List<*>
                if (!missing.isNullOrEmpty()) {
                    "CSVに不足している必須カラムがあります：\n" +
                            missing.joinToString(", ")
                } else {
                    "エラーが発生しました。"
                }
            }
            StatusCode.CSV_SCHEMA_ERROR -> "CSVスキーマにエラーがあります。"
        }
    }
}