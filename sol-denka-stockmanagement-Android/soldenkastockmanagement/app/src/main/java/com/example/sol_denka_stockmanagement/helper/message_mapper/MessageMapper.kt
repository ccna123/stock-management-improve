package com.example.sol_denka_stockmanagement.helper.message_mapper

import com.example.sol_denka_stockmanagement.constant.StatusCode

object MessageMapper {
    fun toMessage(code: StatusCode): String {
        return when (code) {

            StatusCode.OK,
            StatusCode.DOWNLOAD_SFTP_OK ->
                "処理が正常に完了しました"

            StatusCode.FILE_NOT_FOUND ->
                "CSVファイルが見つかりません"

            StatusCode.FILE_EMPTY ->
                "CSVファイルの内容が空です"

            StatusCode.FOLDER_NOT_FOUND ->
                "保存先フォルダーが見つかりません"

            StatusCode.WRITE_ERROR ->
                "CSVファイルの書き込みに失敗しました"

            StatusCode.EMPTY_DATA ->
                "保存するデータがありません"

            StatusCode.PERMISSION_DENIED ->
                "権限がないため実行できません"

            StatusCode.CSV_IMPORTER_NOT_FOUND ->
                "CSVインポーターが見つかりません"

            StatusCode.FILE_CREATED_FAILED ->
                "CSVファイルの作成に失敗しました"

            StatusCode.FAILED ->
                "エラーが発生しました"

            StatusCode.IMPORT_OK -> "取り込み成功しました"
            StatusCode.EXPORT_OK -> "CSVファイルの出力、\nWindowsアプリへの送信に成功しました。"
        }
    }
}