package ru.babaetskv.passionwoman.data

import android.content.Context
import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader

class AssetProvider(
    context: Context,
    val moshi: Moshi
) {
    val assetManager: AssetManager = context.assets

    suspend inline fun <reified T> loadListFromAsset(assetFile: AssetFile): List<T> =
        withContext(Dispatchers.IO) {
            val json = assetManager.open(assetFile.fileName)
                .bufferedReader()
                .use(BufferedReader::readText)
            val listType = Types.newParameterizedType(List::class.java, T::class.java)
            val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
            return@withContext adapter.fromJson(json)
                ?: emptyList()
        }

    enum class AssetFile(
        val fileName: String
    ) {
        STORIES("stories.json"),
        PRODUCTS("products.json")
    }
}