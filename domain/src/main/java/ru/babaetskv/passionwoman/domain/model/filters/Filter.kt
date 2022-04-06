package ru.babaetskv.passionwoman.domain.model.filters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONException
import org.json.JSONObject
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.model.Color
import ru.babaetskv.passionwoman.domain.model.Price
import ru.babaetskv.passionwoman.domain.model.base.SelectableItem

sealed class Filter(
    val type: Type
) : Parcelable {
    abstract val code: String
    abstract val uiName: String
    abstract val isSelected: Boolean
    abstract val priority: Int

    abstract fun toJsonObject(): JSONObject?
    abstract fun clear(): Filter

    protected fun createBaseJsonObject(): JSONObject = JSONObject().apply {
        put(PARAM_CODE, code)
    }

    @Parcelize
    open class Multi(
        override val code: String,
        override val uiName: String,
        override val priority: Int,
        val values: List<SelectableItem<FilterValue>>,
    ) : Filter(Type.MULTI) {
        override val isSelected: Boolean
            get() = values.any { it.isSelected }

        override fun toJsonObject(): JSONObject? =
            values.filter { it.isSelected }.map { it.value.code }.let {
                if (it.isEmpty()) return@let null

                return@let createBaseJsonObject().apply {
                    put(PARAM_VALUES, it)
                }
            }

        override fun clear(): Filter =
            Multi(
                code = code,
                uiName = uiName,
                priority = priority,
                values = values.map {
                    it.copy(
                        isSelected = false
                    )
                }
            )

        companion object {

            fun fromJson(json: JSONObject): Multi? = try {
                Multi(
                    code = json.getString(PARAM_CODE),
                    uiName = json.getString(PARAM_UI_NAME),
                    priority = json.optInt(PARAM_PRIORITY, DEFAULT_PRIORITY),
                    values = json.getJSONArray(PARAM_VALUES).let {
                        val values = mutableListOf<FilterValue>()
                        for (i in 0 until it.length()) {
                            FilterValue.fromJson(it.getJSONObject(i))?.run {
                                values.add(this)
                            }
                        }
                        values
                    }.map { SelectableItem(it) }
                )
            } catch (e: JSONException) {
                null
            }
        }
    }

    @Parcelize
    open class Range(
        override val code: String,
        override val uiName: String,
        override val priority: Int,
        val min: Price,
        val max: Price,
        val selectedMin: Price,
        val selectedMax: Price,
    ) : Filter(Type.RANGE) {
        override val isSelected: Boolean
            get() = selectedMin > min || selectedMax < max

        override fun toJsonObject(): JSONObject? =
            if (selectedMin == min && selectedMax == max) null else {
                createBaseJsonObject().apply {
                    put(PARAM_MIN_VALUE, selectedMin.toFloat())
                    put(PARAM_MAX_VALUE, selectedMax.toFloat())
                }
            }

        override fun clear(): Filter =
            Range(
                code = code,
                uiName = uiName,
                priority = priority,
                min = min,
                max = max,
                selectedMin = min,
                selectedMax = max
            )

        companion object {

            fun fromJson(json: JSONObject): Range? = try {
                val minValue = json.getDouble(PARAM_MIN_VALUE).toFloat()
                val maxValue = json.getDouble(PARAM_MAX_VALUE).toFloat()
                Range(
                    code = json.getString(PARAM_CODE),
                    uiName = json.getString(PARAM_UI_NAME),
                    priority = json.optInt(PARAM_PRIORITY, DEFAULT_PRIORITY),
                    min = Price(minValue),
                    max = Price(maxValue),
                    selectedMin = Price(minValue),
                    selectedMax = Price(maxValue)
                )
            } catch (e: JSONException) {
                null
            }
        }
    }

    @Parcelize
    open class Bool(
        override val code: String,
        override val uiName: String,
        override val priority: Int,
        var value: Boolean
    ) : Filter(Type.BOOLEAN) {
        override val isSelected: Boolean
            get() = value

        override fun toJsonObject(): JSONObject = createBaseJsonObject().apply {
            put(PARAM_VALUE, value)
        }

        override fun clear(): Filter =
            Bool(
                code = code,
                uiName = uiName,
                priority = priority,
                value = false
            )

        companion object {

            fun fromJson(json: JSONObject): Bool? = try {
                Bool(
                    code = json.getString(PARAM_CODE),
                    uiName = json.getString(PARAM_UI_NAME),
                    priority = json.optInt(PARAM_PRIORITY, DEFAULT_PRIORITY),
                    value = false
                )
            } catch (e: JSONException) {
                null
            }
        }
    }

    @Parcelize
    open class ColorMulti(
        override val code: String,
        override val uiName: String,
        override val priority: Int,
        val values: List<SelectableItem<Color>>,
    ) : Filter(Type.COLOR) {
        override val isSelected: Boolean
            get() = values.any { it.isSelected }

        override fun toJsonObject(): JSONObject? =
            values.filter { it.isSelected }.map { it.value.code }.let {
                if (it.isEmpty()) return@let null

                return@let createBaseJsonObject().apply {
                    put(PARAM_VALUES, it)
                }
            }

        override fun clear(): Filter =
            ColorMulti(
                code = code,
                uiName = uiName,
                priority = priority,
                values = values.map {
                    it.copy(
                        isSelected = false
                    )
                }
            )

        companion object {
            fun fromJson(json: JSONObject): Filter? = try {
                ColorMulti(
                    code = json.getString(PARAM_CODE),
                    uiName = json.getString(PARAM_UI_NAME),
                    priority = json.optInt(PARAM_PRIORITY, DEFAULT_PRIORITY),
                    values = json.getJSONArray(PARAM_VALUES).let {
                        val values = mutableListOf<Color>()
                        for (i in 0 until it.length()) {
                            Color.fromJson(it.getJSONObject(i))?.run {
                                values.add(this)
                            }
                        }
                        values
                    }.map { SelectableItem(it) }
                )
            } catch (e: JSONException) {
                null
            }
        }
    }

    class DiscountOnly(
        stringProvider: StringProvider,
        value: Boolean = false
    ) : Bool(CODE_DISCOUNT_ONLY, stringProvider.FILTER_DISCOUNT, 0, value)

    enum class Type(
        private val apiName: String,
        val resolver: (JSONObject) -> Filter?
    ) {
        MULTI("multi", Multi::fromJson),
        BOOLEAN("boolean", Bool::fromJson),
        RANGE("range", Range::fromJson),
        COLOR("color", ColorMulti::fromJson);

        companion object {

            fun getValueByApiName(apiName: String): Type? = values().find { it.apiName == apiName }
        }
    }

    companion object {
        const val PARAM_CODE = "code"
        const val PARAM_PRIORITY = "priority"
        const val PARAM_VALUES = "values"
        const val PARAM_VALUE = "value"
        const val PARAM_UI_NAME = "uiName"
        const val PARAM_MIN_VALUE = "minValue"
        const val PARAM_MAX_VALUE = "maxValue"
        const val PARAM_TYPE = "type"
        const val PARAM_HEX = "hex"
        const val CODE_DISCOUNT_ONLY = "discount_only"
        const val DEFAULT_PRIORITY = 1

        fun fromJson(json: JSONObject): Filter? = try {
            Type.getValueByApiName(json.getString(PARAM_TYPE))?.resolver?.invoke(json)
        } catch (e: JSONException) {
            null
        }
    }
}
