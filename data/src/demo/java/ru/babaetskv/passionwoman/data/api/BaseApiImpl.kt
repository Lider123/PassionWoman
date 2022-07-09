package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

abstract class BaseApiImpl {

    protected inline fun <reified T> loadListFromAsset(
        assetManager: AssetManager,
        assetFile: AssetFile,
        moshi: Moshi
    ): List<T> {
        val json = assetManager.open(assetFile.fileName).bufferedReader().use { it.readText() }
        val listType = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter: JsonAdapter<List<T>> = moshi.adapter(listType)
        return adapter.fromJson(json) ?: emptyList()
    }

    protected inline fun <reified T> loadObjectFromAsset(
        assetManager: AssetManager,
        assetFile: AssetFile,
        moshi: Moshi
    ): T {
        val json = assetManager.open(assetFile.fileName).bufferedReader().use { it.readText() }
        val adapter: JsonAdapter<T> = moshi.adapter(T::class.java)
        return adapter.fromJson(json)!!
    }

    protected fun loadArrayOfJsonFromAsset(
        assetManager: AssetManager,
        assetFile: AssetFile
    ): List<JSONObject> {
        val json = assetManager.open(assetFile.fileName).bufferedReader().use { it.readText() }
        return JSONArray(json).let {
            val values = mutableListOf<JSONObject>()
            for (i in 0 until it.length()) {
                values.add(it.getJSONObject(i))
            }
            values
        }
    }

    protected fun getNotFoundException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(NOT_FOUND, it)
        }.let(::HttpException)

    protected fun getBadRequestException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(BAD_REQUEST, it)
        }.let(::HttpException)

    protected fun getInternalServerErrorException(message: String) : HttpException =
        message.toResponseBody("text/plain".toMediaType()).let {
            Response.error<Nothing>(INTERNAL_SERVER_ERROR, it)
        }.let(::HttpException)

    protected enum class AssetFile(
        val fileName: String
    ) {
        CATEGORIES("categories.json"),
        BRANDS("brands.json"),
        PROMOTIONS("promotions.json"),
        PROFILE("profile.json"),
        STORIES("stories.json"),
        FILTERS_COMMON("filters_common.json"),
        PRODUCTS_BRA("products_bra.json"),
        PRODUCTS_PANTIES("products_panties.json"),
        PRODUCTS_LINGERIE("products_lingerie.json"),
        PRODUCTS_EROTIC("products_erotic.json"),
        PRODUCTS_SWIM("products_swim.json"),
        PRODUCTS_CORSET("products_corset.json"),
        PRODUCTS_GARTER_BELTS("products_garter_belts.json"),
        PRODUCTS_BABYDOLL("products_babydoll.json"),
        PRODUCTS_STOCKINGS("products_stockings.json"),
        PRODUCTS_PANTYHOSE("products_pantyhose.json"),
        FAVORITE_IDS("favoriteIds.json")
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
