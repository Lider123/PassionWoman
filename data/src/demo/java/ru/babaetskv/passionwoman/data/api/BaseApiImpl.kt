package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.babaetskv.passionwoman.domain.AppDispatchers

abstract class BaseApiImpl(
    protected val assetManager: AssetManager,
    protected val moshi: Moshi,
    protected val dispatchers: AppDispatchers
) {

    protected open fun doBeforeRequest() = Unit

    protected inline fun <reified T> loadListFromAsset(assetFile: AssetFile): List<T> {
        val json = assetManager.open(assetFile.fileName).bufferedReader().use { it.readText() }
        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
        return adapter.fromJson(json) ?: emptyList()
    }

    protected suspend fun <T> processRequest(
        delayMs: Long = DELAY_LOADING,
        block: suspend () -> T
    ): T = withContext(dispatchers.IO) {
        delay(delayMs)
        doBeforeRequest()
        return@withContext block.invoke()
    }

    protected enum class AssetFile(
        val fileName: String
    ) {
        STORIES("stories.json"),
    }

    companion object {
        @JvmStatic
        protected val DELAY_LOADING = 500L
        @JvmStatic
        val TOKEN = "token"
    }
}
