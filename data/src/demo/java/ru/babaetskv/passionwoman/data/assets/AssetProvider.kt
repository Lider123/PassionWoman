package ru.babaetskv.passionwoman.data.assets

interface AssetProvider {

    suspend fun <T> loadListFromAsset(assetFile: AssetFile, klass: Class<T>): List<T>

    enum class AssetFile(
        val fileName: String
    ) {
        STORIES("stories.json"),
        PRODUCTS("products.json")
    }
}