package ru.babaetskv.passionwoman.data.assets

import android.content.Context
import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader

class AssetProviderImpl(
    context: Context,
    val moshi: Moshi
) : AssetProvider {
    private val assetManager: AssetManager = context.assets

    override suspend fun <T> loadListFromAsset(assetFile: AssetProvider.AssetFile, klass: Class<T>): List<T> =
        withContext(Dispatchers.IO) {
            val json = assetManager.open(assetFile.fileName)
                .bufferedReader()
                .use(BufferedReader::readText)
            val listType = Types.newParameterizedType(List::class.java, klass)
            val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
            return@withContext adapter.fromJson(json)
                ?: emptyList()
        }

    /*suspend inline fun <reified T> loadListFromAsset(assetFile: AssetProvider.AssetFile): List<T> =
        withContext(Dispatchers.IO) {
            val json = assetManager.open(assetFile.fileName)
                .bufferedReader()
                .use(BufferedReader::readText)
            val listType = Types.newParameterizedType(List::class.java, T::class.java)
            val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
            return@withContext adapter.fromJson(json)
                ?: emptyList()
        }*/
}
