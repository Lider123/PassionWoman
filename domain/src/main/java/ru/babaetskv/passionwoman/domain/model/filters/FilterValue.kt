package ru.babaetskv.passionwoman.domain.model.filters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONException
import org.json.JSONObject

@Parcelize
data class FilterValue(
    val code: String,
    val uiName: String
) : Parcelable {

    companion object {
        const val PARAM_CODE = "code"
        const val PARAM_UI_NAME = "uiName"

        fun fromJson(json: JSONObject): FilterValue? = try {
            FilterValue(
                code = json.getString(PARAM_CODE),
                uiName = json.getString(PARAM_UI_NAME)
            )
        } catch (e: JSONException) {
            null
        }
    }
}
