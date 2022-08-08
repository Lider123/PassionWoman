package ru.babaetskv.passionwoman.data.filters

import org.json.JSONObject
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.utils.toJsonArray

sealed class FilterExtractor(
    private val filterResolver: FilterResolver
) {
    private val uiName: String = filterResolver.uiName

    protected abstract val type: FilterType
    protected abstract val priority: Int

    protected abstract suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase)

    suspend fun extractAsJson(database: PassionWomanDatabase): JSONObject =
        JSONObject().apply {
            put(Filters.PARAM_TYPE, type.code)
            put(Filters.PARAM_CODE, filterResolver.code)
            put(Filters.PARAM_UI_NAME, uiName)
            put(Filters.PARAM_PRIORITY, priority)
            putFilterValues(this, database)
        }

    class Category : FilterExtractor(FilterResolver.CATEGORY) {
        override val type: FilterType = FilterType.MULTI
        override val priority: Int = 5

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.categoryDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_CODE, it.id)
                    put(Filters.PARAM_UI_NAME, it.name)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    class Season : FilterExtractor(FilterResolver.SEASON) {
        override val type: FilterType = FilterType.MULTI
        override val priority: Int = 4

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.productSeasonDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_CODE, it.code)
                    put(Filters.PARAM_UI_NAME, it.uiName)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    class Style : FilterExtractor(FilterResolver.STYLE) {
        override val type: FilterType = FilterType.MULTI
        override val priority: Int = 4

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.productStyleDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_CODE, it.code)
                    put(Filters.PARAM_UI_NAME, it.uiName)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    class Country : FilterExtractor(FilterResolver.COUNTRY) {
        override val type: FilterType = FilterType.MULTI
        override val priority: Int = 4

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.productCountryDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_CODE, it.code)
                    put(Filters.PARAM_UI_NAME, it.uiName)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    class Material : FilterExtractor(FilterResolver.MATERIAL) {
        override val type: FilterType = FilterType.MULTI
        override val priority: Int = 4

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.productMaterialDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_CODE, it.code)
                    put(Filters.PARAM_UI_NAME, it.uiName)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    class Brand : FilterExtractor(FilterResolver.BRANDS) {
        override val type: FilterType = FilterType.MULTI
        override val priority: Int = 4

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.brandDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_CODE, it.id)
                    put(Filters.PARAM_UI_NAME, it.name)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    class Size : FilterExtractor(FilterResolver.SIZES) {
        override val type: FilterType = FilterType.MULTI
        override val priority: Int = 4

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.productSizeDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_CODE, it.code)
                    put(Filters.PARAM_UI_NAME, it.uiName)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    class Price : FilterExtractor(FilterResolver.PRICE) {
        override val type: FilterType = FilterType.RANGE
        override val priority: Int = 2

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val minValue = database.productDao.getMinPrice()
            val maxValue = database.productDao.getMaxPrice()
            json.put(Filters.PARAM_MIN_VALUE, minValue)
            json.put(Filters.PARAM_MAX_VALUE, maxValue)
        }
    }

    class Color : FilterExtractor(FilterResolver.COLOR) {
        override val type: FilterType = FilterType.COLOR
        override val priority: Int = 3

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val colors = database.colorDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_ID, it.id)
                    put(Filters.PARAM_UI_NAME, it.uiName)
                    put(Filters.PARAM_HEX, it.hex)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, colors)
        }
    }

    class Extractor : FilterExtractor(FilterResolver.MODEL) {
        override val type: FilterType = FilterType.MULTI
        override val priority: Int = 4

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.productModelDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_CODE, it.code)
                    put(Filters.PARAM_UI_NAME, it.uiName)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    class Type : FilterExtractor(FilterResolver.TYPE) {
        override val type: FilterType = FilterType.MULTI
        override val priority: Int = 4

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.productTypeDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_CODE, it.code)
                    put(Filters.PARAM_UI_NAME, it.uiName)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    protected enum class FilterType(
        val code: String
    ) {
        MULTI("multi"),
        RANGE("range"),
        COLOR("color")
    }
}
