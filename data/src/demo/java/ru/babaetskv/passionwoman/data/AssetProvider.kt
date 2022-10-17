package ru.babaetskv.passionwoman.data

import android.content.Context
import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class AssetProvider(
    context: Context,
    val moshi: Moshi
) {
    val assetManager: AssetManager = context.assets

    inline fun <reified T> loadListFromAsset(assetFile: AssetFile): List<T> {
        val json = assetManager.open(assetFile.fileName).bufferedReader().use { it.readText() }
        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
        return adapter.fromJson(json) ?: emptyList()
    }

    enum class AssetFile(
        val fileName: String
    ) {
        STORIES("stories.json"),
        PRODUCTS("products.json")
    }
}