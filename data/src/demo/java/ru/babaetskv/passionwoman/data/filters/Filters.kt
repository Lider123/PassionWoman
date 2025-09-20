package ru.babaetskv.passionwoman.data.filters

import org.json.JSONArray
import org.json.JSONObject
import ru.babaetskv.passionwoman.data.model.ProductModel

internal class Filters(
    private val filters: JSONArray
) {
    val isDiscountOnly: Boolean
        get() {
            for (i in 0 until filters.length()) {
                val filterObject = filters.getJSONObject(i)
                if (filterObject.getString(PARAM_CODE) == CODE_DISCOUNT_ONLY) {
                    if (filterObject.getBoolean(PARAM_VALUE)) return true
                }
            }
            return false
        }

    fun applyToProducts(products: List<ProductModel>): List<ProductModel> =
        products.filter { product ->
            BooleanBuilder(true)
                .apply {
                    for (i in 0 until filters.length()) {
                        val filter = filters.getJSONObject(i)
                        val code = filter.getString(PARAM_CODE)
                        Filter.getByCode(code, filter)?.let {
                            and(it.matches(product))
                        }
                    }
                }
                .and(!isDiscountOnly || product.price > product.priceWithDiscount)
                .build()
        }

    fun selectAvailable(products: List<ProductModel>): JSONArray {
        val availableFilters = JSONArray()
        for (i in 0 until filters.length()) {
            val filterObject = filters.getJSONObject(i)
            val code = filterObject.getString(PARAM_CODE)
            Filter.getByCode(code, filterObject)?.selectAvailable(products)?.let {
                availableFilters.put(it)
            }
        }
        return availableFilters
    }

    companion object {
        const val PARAM_ID = "id"
        const val PARAM_CODE = "code"
        const val PARAM_VALUE = "value"
        const val PARAM_VALUES = "values"
        const val PARAM_MIN_VALUE = "minValue"
        const val PARAM_MAX_VALUE = "maxValue"
        const val PARAM_TYPE = "type"
        const val PARAM_UI_NAME = "uiName"
        const val PARAM_PRIORITY = "priority"
        const val PARAM_HEX = "hex"
        const val CODE_DISCOUNT_ONLY = "discount_only"

        val discountOnlyFilters: Filters =
            JSONObject().apply {
                put(PARAM_CODE, CODE_DISCOUNT_ONLY)
                put(PARAM_VALUE, true)
            }.asJsonArray().let { Filters(it) }

        private fun JSONObject.asJsonArray(): JSONArray = JSONArray().also { array ->
            array.put(this)
        }
    }
}
