package com.example.sol_denka_stockmanagement.helper.message_mapper

import com.example.sol_denka_stockmanagement.constant.StatusCode

object MessageMapper {
    fun toMessage(code: StatusCode, params: Map<String, Any>? = null): String {
        return when (code) {

            StatusCode.OK,
            StatusCode.DOWNLOAD_SFTP_OK ->
                "処理が正常に完了しました。"

            StatusCode.FILE_NOT_FOUND ->
                "対象のCSVファイルが見つかりません。削除・移動されていないか確認してください。"

            StatusCode.FILE_EMPTY -> {
                val fileParam = params?.get("file")

                when (fileParam) {
                    is List<*> -> {
                        val files = fileParam.filterIsInstance<String>()
                        if (files.isNotEmpty()) {
                            "CSVファイルが空です。\n${files.joinToString("、")}"
                        } else {
                            "CSVファイルの内容が空です。"
                        }
                    }

                    is String -> {
                        "CSVファイルが空です。\n$fileParam"
                    }

                    else -> {
                        "CSVファイルの内容が空です。"
                    }
                }
            }

            StatusCode.MISSING_HEADER -> {
                val headers = params?.get("missing_headers") as? List<*>
                if (!headers.isNullOrEmpty()) {
                    "CSVヘッダーが不足しています\n" +
                            headers.joinToString("、")
                } else {
                    "CSVヘッダーが不足しています。"
                }
            }

            StatusCode.FOLDER_NOT_FOUND ->
                "保存先フォルダーが見つかりません。"

            StatusCode.SAVE_DATA_TO_CSV_FAILED ->
                "CSVファイルの作成に失敗しました。再度お試しください。"

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

            StatusCode.SAVE_DATA_TO_DB_FAILED -> "データの保存に失敗しました。再度お試しください。"

            StatusCode.SQLITE_CONSTRAINT_ERROR -> "外部キーエラー"

            StatusCode.PROCESS_NOT_CHOSEN -> "チェック済みのタグに処理方法を選択してください。"

            StatusCode.IMPORT_OK -> "CSVファイルの取込に成功しました。"
            StatusCode.SAVE_CSV_SUCCESS_FAILED_SFTP -> "CSV ファイルの保存は正常に完了しましたが、\n送信処理でエラーが発生しました。再度送信をお試しください。"
            StatusCode.CANCEL -> "登録作業をキャンセルし、\nホーム画面に戻ってもよろしいですか？"
            StatusCode.MISSING_COLUMN -> {
                val missing = params?.get("missing_columns") as? List<*>
                if (!missing.isNullOrEmpty()) {
                    "必須カラムが不足しています。\n" +
                            missing.joinToString(", ")
                } else {
                    "エラーが発生しました。"
                }
            }
            StatusCode.IMPORT_FAILED -> "CSVファイルの読み取りに失敗しました。"
            StatusCode.REFERENCE_MASTER_MISSING_FILE -> {
                val missing = params?.get("missing_files") as? List<*>
                if (!missing.isNullOrEmpty()) {
                    "必須マスタが不足しています。\n" +
                            missing.joinToString(", ")
                } else {
                    "エラーが発生しました。"
                }
            }

            StatusCode.SAVE_CSV_SEND_SFTP_SUCCESS -> "CSVファイルの保存、送信に成功しました。"
        }
    }
}