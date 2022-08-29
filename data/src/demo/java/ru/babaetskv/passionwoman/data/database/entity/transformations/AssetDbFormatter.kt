package ru.babaetskv.passionwoman.data.database.entity.transformations

object AssetDbFormatter {
    private const val ASSET_DB_PREFIX = "file:///android_asset/demo_db_editor/"

    fun formatAssetDbPath(path: String): String =
        ASSET_DB_PREFIX + path
}
