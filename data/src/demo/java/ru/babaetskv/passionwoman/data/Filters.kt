package ru.babaetskv.passionwoman.data

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

    private class BooleanBuilder(
        private var value: Boolean
    ) {

        fun and(other: Boolean): BooleanBuilder = apply {
            value = value && other
        }

        fun andNot(other: Boolean): BooleanBuilder = apply {
            value = value && !other
        }

        fun build(): Boolean = value
    }

    private sealed class Filter(
        protected val filterObject: JSONObject,
    ) {

        abstract fun matches(product: ProductModel): Boolean

        abstract fun selectAvailable(products: List<ProductModel>): JSONObject?

        class Categories(filterObject: JSONObject) : Filter(filterObject) {

            override fun matches(product: ProductModel): Boolean {
                val values = JSONArray(filterObject.getString(PARAM_VALUES)).asListOfValues()
                return values.isEmpty() || product.category.id in values
            }

            override fun selectAvailable(products: List<ProductModel>): JSONObject? {
                val availableCodes = products.map { it.category.id }.distinct()
                val values = filterObject.getJSONArray(PARAM_VALUES)
                val newValues = JSONArray()
                for (i in 0 until values.length()) {
                    val value = values.getJSONObject(i)
                    if (value.getString(PARAM_CODE) in availableCodes) newValues.put(value)
                }
                return if (newValues.length() == 0) null else {
                    filterObject.put(PARAM_VALUES, newValues)
                }
            }
        }

        class Brands(filterObject: JSONObject) : Filter(filterObject) {

            override fun matches(product: ProductModel): Boolean {
                val values = JSONArray(filterObject.getString(PARAM_VALUES)).asListOfValues()
                return values.isEmpty() || product.brand?.id in values
            }

            override fun selectAvailable(products: List<ProductModel>): JSONObject? {
                val availableCodes = products.mapNotNull { it.brand?.id }.distinct()
                val values = filterObject.getJSONArray(PARAM_VALUES)
                val newValues = JSONArray()
                for (i in 0 until values.length()) {
                    val value = values.getJSONObject(i)
                    if (value.getString(PARAM_CODE) in availableCodes) newValues.put(value)
                }
                return if (newValues.length() == 0) null else {
                    filterObject.put(PARAM_VALUES, newValues)
                }
            }
        }

        class Sizes(filterObject: JSONObject) : Filter(filterObject) {

            override fun matches(product: ProductModel): Boolean {
                val values = JSONArray(filterObject.getString(PARAM_VALUES)).asListOfValues()
                val availableSizes = product.colors.flatMap { it.availableSizes ?: emptyList() }
                return values.isEmpty() || availableSizes.intersect(values.toSet()).isNotEmpty()
            }

            override fun selectAvailable(products: List<ProductModel>): JSONObject? {
                val availableCodes: List<String> = products
                    .flatMap { it.colors }
                    .flatMap { it.availableSizes ?: listOf() }
                    .distinct()
                val values = filterObject.getJSONArray(PARAM_VALUES)
                val newValues = JSONArray()
                for (i in 0 until values.length()) {
                    val value = values.getJSONObject(i)
                    if (value.getString(PARAM_CODE) in availableCodes) newValues.put(value)
                }
                return if (newValues.length() == 0) null else {
                    filterObject.put(PARAM_VALUES, newValues)
                }
            }
        }

        class Price(filterObject: JSONObject) : Filter(filterObject) {

            override fun matches(product: ProductModel): Boolean {
                val min = filterObject.getDouble(PARAM_MIN_VALUE)
                val max = filterObject.getDouble(PARAM_MAX_VALUE)
                return product.priceWithDiscount in min..max
            }

            override fun selectAvailable(products: List<ProductModel>): JSONObject? {
                val minPrice = products.minByOrNull { it.priceWithDiscount }?.priceWithDiscount
                val maxPrice = products.maxByOrNull { it.priceWithDiscount }?.priceWithDiscount
                return if (minPrice == null || maxPrice == null || minPrice == maxPrice) null else {
                    filterObject.put(PARAM_MIN_VALUE, minPrice).put(PARAM_MAX_VALUE, maxPrice)
                }
            }
        }

        class Color(filterObject: JSONObject) : Filter(filterObject) {

            override fun matches(product: ProductModel): Boolean {
                val values = JSONArray(filterObject.getString(PARAM_VALUES)).asListOfValues()
                val colors = product.colors.map { it.color.code }
                return values.isEmpty() || colors.intersect(values.toSet()).isNotEmpty()
            }

            override fun selectAvailable(products: List<ProductModel>): JSONObject? {
                val availableCodes: List<String> = products.flatMap { it.colors }
                    .map { it.color.code }
                    .distinct()
                val values = filterObject.getJSONArray(PARAM_VALUES)
                val newValues = JSONArray()
                for (i in 0 until values.length()) {
                    val value = values.getJSONObject(i)
                    if (value.getString(PARAM_CODE) in availableCodes) newValues.put(value)
                }
                return if (newValues.length() == 0) null else {
                    filterObject.put(PARAM_VALUES, newValues)
                }
            }
        }

        class Model(filterObject: JSONObject) : Filter(filterObject) {

            override fun matches(product: ProductModel): Boolean {
                val values = JSONArray(filterObject.getString(PARAM_VALUES)).asListOfValues()
                return values.isEmpty() || product.model in values
            }

            override fun selectAvailable(products: List<ProductModel>): JSONObject? {
                val availableCodes: List<String> = products.mapNotNull { it.model }.distinct()
                val values = filterObject.getJSONArray(PARAM_VALUES)
                val newValues = JSONArray()
                for (i in 0 until values.length()) {
                    val value = values.getJSONObject(i)
                    if (value.getString(PARAM_CODE) in availableCodes) newValues.put(value)
                }
                return if (newValues.length() == 0) null else {
                    filterObject.put(PARAM_VALUES, newValues)
                }
            }
        }

        private enum class FilterResolver(
            private val code: String,
            val resolve: (JSONObject) -> Filter?
        ) {
            CATEGORY("category", ::Categories),
            BRANDS("brands", ::Brands),
            SIZES("sizes", ::Sizes),
            PRICE("price", ::Price),
            COLOR("color", ::Color),
            MODEL("model", ::Model);

            companion object {

                fun findByCode(code: String): FilterResolver? =
                    values().find { it.code == code }
            }
        }

        companion object {

            fun getByCode(code: String, filterObject: JSONObject): Filter? =
                FilterResolver.findByCode(code)?.resolve?.invoke(filterObject)
        }
    }

    companion object {
        private const val PARAM_CODE = "code"
        private const val PARAM_VALUE = "value"
        private const val PARAM_VALUES = "values"
        private const val PARAM_MIN_VALUE = "minValue"
        private const val PARAM_MAX_VALUE = "maxValue"
        private const val CODE_DISCOUNT_ONLY = "discount_only"

        val discountOnlyFilters: Filters =
            JSONObject().apply {
                put(PARAM_CODE, CODE_DISCOUNT_ONLY)
                put(PARAM_VALUE, true)
            }.asJsonArray().let { Filters(it) }

        private fun JSONObject.asJsonArray(): JSONArray = JSONArray().also { array ->
            array.put(this)
        }

        private fun JSONArray.asListOfValues(): List<String> = mutableListOf<String>().also { list ->
            for (i in 0 until length()) {
                list.add(getString(i))
            }
        }
    }
}
