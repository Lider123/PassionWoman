package ru.babaetskv.passionwoman.data.filters

import org.json.JSONArray
import org.json.JSONObject
import ru.babaetskv.passionwoman.data.model.ProductModel

sealed class Filter(
    protected val filterObject: JSONObject
) {

    abstract fun matches(product: ProductModel): Boolean

    abstract fun selectAvailable(products: List<ProductModel>): JSONObject?

    abstract class AdditionalInfoFilter(filterObject: JSONObject) : Filter(filterObject) {
        abstract val resolver: FilterResolver

        override fun matches(product: ProductModel): Boolean {
            val values = JSONArray(filterObject.getString(Filters.PARAM_VALUES)).asListOfValues()
            return values.isEmpty() || matchesAdditionalInfo(product)
        }

        override fun selectAvailable(products: List<ProductModel>): JSONObject? {
            val availableCodes: List<String> = products.flatMap {
                it.additionalInfo?.get(resolver.code).orEmpty()
            }.distinct()
            val values = filterObject.getJSONArray(Filters.PARAM_VALUES)
            val newValues = JSONArray()
            for (i in 0 until values.length()) {
                val value = values.getJSONObject(i)
                if (value.getString(Filters.PARAM_CODE) in availableCodes) newValues.put(value)
            }
            return if (newValues.length() == 0) null else {
                filterObject.put(Filters.PARAM_VALUES, newValues)
            }
        }

        private fun matchesAdditionalInfo(product: ProductModel): Boolean {
            val values = JSONArray(filterObject.getString(Filters.PARAM_VALUES)).asListOfValues()
            return product.additionalInfo?.get(resolver.code)
                ?.intersect(values.toSet())
                .isNullOrEmpty()
                .not()
        }
    }

    class Categories(filterObject: JSONObject) : Filter(filterObject) {

        override fun matches(product: ProductModel): Boolean {
            val values = JSONArray(filterObject.getString(Filters.PARAM_VALUES)).asListOfValues()
            return values.isEmpty() || product.category.id.toString() in values
        }

        override fun selectAvailable(products: List<ProductModel>): JSONObject? {
            val availableIds = products.map { it.category.id.toString() }.distinct()
            val values = filterObject.getJSONArray(Filters.PARAM_VALUES)
            val newValues = JSONArray()
            for (i in 0 until values.length()) {
                val value = values.getJSONObject(i)
                if (value.getString(Filters.PARAM_CODE) in availableIds) newValues.put(value)
            }
            return if (newValues.length() == 0) null else {
                filterObject.put(Filters.PARAM_VALUES, newValues)
            }
        }
    }

    class Brands(filterObject: JSONObject) : Filter(filterObject) {

        override fun matches(product: ProductModel): Boolean {
            val values = JSONArray(filterObject.getString(Filters.PARAM_VALUES)).asListOfValues()
            return values.isEmpty() || product.brand?.id.toString() in values
        }

        override fun selectAvailable(products: List<ProductModel>): JSONObject? {
            val availableIds = products.mapNotNull { it.brand?.id?.toString() }.distinct()
            val values = filterObject.getJSONArray(Filters.PARAM_VALUES)
            val newValues = JSONArray()
            for (i in 0 until values.length()) {
                val value = values.getJSONObject(i)
                if (value.getString(Filters.PARAM_CODE) in availableIds) newValues.put(value)
            }
            return if (newValues.length() == 0) null else {
                filterObject.put(Filters.PARAM_VALUES, newValues)
            }
        }
    }

    class Sizes(filterObject: JSONObject) : Filter(filterObject) {

        override fun matches(product: ProductModel): Boolean {
            val values = JSONArray(filterObject.getString(Filters.PARAM_VALUES)).asListOfValues()
            val availableSizes = product.items.flatMap { it.availableSizes ?: emptyList() }
            return values.isEmpty() || availableSizes.intersect(values.toSet()).isNotEmpty()
        }

        override fun selectAvailable(products: List<ProductModel>): JSONObject? {
            val availableCodes: List<String> = products
                .flatMap { it.items }
                .flatMap { it.availableSizes ?: listOf() }
                .distinct()
            val values = filterObject.getJSONArray(Filters.PARAM_VALUES)
            val newValues = JSONArray()
            for (i in 0 until values.length()) {
                val value = values.getJSONObject(i)
                if (value.getString(Filters.PARAM_CODE) in availableCodes) newValues.put(value)
            }
            return if (newValues.length() == 0) null else {
                filterObject.put(Filters.PARAM_VALUES, newValues)
            }
        }
    }

    class Price(filterObject: JSONObject) : Filter(filterObject) {

        override fun matches(product: ProductModel): Boolean {
            val min = filterObject.getDouble(Filters.PARAM_MIN_VALUE)
            val max = filterObject.getDouble(Filters.PARAM_MAX_VALUE)
            return product.priceWithDiscount in min..max
        }

        override fun selectAvailable(products: List<ProductModel>): JSONObject? {
            val minPrice = products.minByOrNull { it.priceWithDiscount }?.priceWithDiscount
            val maxPrice = products.maxByOrNull { it.priceWithDiscount }?.priceWithDiscount
            return if (minPrice == null || maxPrice == null || minPrice == maxPrice) null else {
                filterObject.put(Filters.PARAM_MIN_VALUE, minPrice).put(Filters.PARAM_MAX_VALUE, maxPrice)
            }
        }
    }

    class Color(filterObject: JSONObject) : Filter(filterObject) {

        override fun matches(product: ProductModel): Boolean {
            val values = JSONArray(filterObject.getString(Filters.PARAM_VALUES)).asListOfValues()
            val colorIds = product.items.map { it.color.id.toString() }
            return values.isEmpty() || colorIds.intersect(values.toSet()).isNotEmpty()
        }

        override fun selectAvailable(products: List<ProductModel>): JSONObject? {
            val availableIds: List<Long> = products.flatMap { it.items }
                .map { it.color.id }
                .distinct()
            val values = filterObject.getJSONArray(Filters.PARAM_VALUES)
            val newValues = JSONArray()
            for (i in 0 until values.length()) {
                val value = values.getJSONObject(i)
                if (value.getLong(Filters.PARAM_ID) in availableIds) newValues.put(value)
            }
            return if (newValues.length() == 0) null else {
                filterObject.put(Filters.PARAM_VALUES, newValues)
            }
        }
    }

    class Model(filterObject: JSONObject) : AdditionalInfoFilter(filterObject) {
        override val resolver: FilterResolver = FilterResolver.MODEL
    }

    class Season(filterObject: JSONObject) : AdditionalInfoFilter(filterObject) {
        override val resolver: FilterResolver = FilterResolver.SEASON
    }

    class Material(filterObject: JSONObject) : AdditionalInfoFilter(filterObject) {
        override val resolver: FilterResolver = FilterResolver.MATERIAL
    }

    class Style(filterObject: JSONObject) : AdditionalInfoFilter(filterObject) {
        override val resolver: FilterResolver = FilterResolver.STYLE
    }

    class Country(filterObject: JSONObject) : AdditionalInfoFilter(filterObject) {
        override val resolver: FilterResolver = FilterResolver.COUNTRY
    }

    class Type(filterObject: JSONObject) : AdditionalInfoFilter(filterObject) {
        override val resolver: FilterResolver = FilterResolver.TYPE
    }

    companion object {

        private fun JSONArray.asListOfValues(): List<String> = mutableListOf<String>().also { list ->
            for (i in 0 until length()) {
                list.add(getString(i))
            }
        }

        fun getByCode(code: String, filterObject: JSONObject): Filter? =
            FilterResolver.findByCode(code)?.resolveFilter?.invoke(filterObject)
    }
}
