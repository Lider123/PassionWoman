package ru.babaetskv.passionwoman.data.api

import android.content.Context
import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseApiImpl(
    context: Context,
    protected val moshi: Moshi
) {
    protected val assetManager: AssetManager = context.assets

    protected inline fun <reified T> loadListFromAsset(assetFile: AssetFile): List<T> {
        val json = assetManager.open(assetFile.fileName).bufferedReader().use { it.readText() }
        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
        return adapter.fromJson(json) ?: emptyList()
    }

    fun getNotFoundException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(NOT_FOUND, it)
        }.let(::HttpException)

    fun getBadRequestException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(BAD_REQUEST, it)
        }.let(::HttpException)

    fun getInternalServerErrorException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(INTERNAL_SERVER_ERROR, it)
        }.let(::HttpException)

    protected enum class AssetFile(
        val fileName: String
    ) {
        STORIES("stories.json"),
    }

    companion object {
        @JvmStatic
        protected val DELAY_LOADING = 500L
        @JvmStatic
        protected val NOT_FOUND = 404
        @JvmStatic
        protected val BAD_REQUEST = 400
        @JvmStatic
        protected val INTERNAL_SERVER_ERROR = 500
    }
}
