package ru.babaetskv.passionwoman.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONException
import org.json.JSONObject
import ru.babaetskv.passionwoman.domain.model.filters.Filter

@Parcelize
data class Color(
    val code: String,
    val uiName: String,
    val hex: String
) : Parcelable {
    val isMulticolor: Boolean
        get() = hex == MULTICOLOR

    companion object {
        private const val MULTICOLOR = "multicolor"

        fun fromJson(json: JSONObject): Color? = try {
            Color(
                code = json.getString(Filter.PARAM_CODE),
                uiName = json.getString(Filter.PARAM_UI_NAME),
                hex = json.getString(Filter.PARAM_HEX)
            )
        } catch (e: JSONException) {
            null
        }
    }
}
