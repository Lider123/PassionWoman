package ru.babaetskv.passionwoman.data.filters

import org.json.JSONObject
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.utils.toJsonArray

sealed class FilterModel {
    protected abstract val type: FilterType
    protected abstract val filterResolver: FilterResolver
    protected abstract val uiName: String
    protected abstract val priority: Int

    protected abstract suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase)

    suspend fun toJson(database: PassionWomanDatabase): JSONObject =
        JSONObject().apply {
            put(Filters.PARAM_TYPE, type.code)
            put(Filters.PARAM_CODE, filterResolver.code)
            put(Filters.PARAM_UI_NAME, uiName)
            put(Filters.PARAM_PRIORITY, priority)
            putFilterValues(this, database)
        }

    object Category : FilterModel() {
        override val type: FilterType = FilterType.MULTI
        override val filterResolver: FilterResolver = FilterResolver.CATEGORY
        override val uiName: String = "Categories"
        override val priority: Int = 5

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.categoryDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_ID, it.id)
                    put(Filters.PARAM_UI_NAME, it.name)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    object Season : FilterModel() {
        override val type: FilterType = FilterType.MULTI
        override val filterResolver: FilterResolver = FilterResolver.SEASON
        override val uiName: String = "Season"
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

    object Style : FilterModel() {
        override val type: FilterType = FilterType.MULTI
        override val filterResolver: FilterResolver = FilterResolver.STYLE
        override val uiName: String = "Style"
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

    object Country : FilterModel() {
        override val type: FilterType = FilterType.MULTI
        override val filterResolver: FilterResolver = FilterResolver.COUNTRY
        override val uiName: String = "Country"
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

    object Material : FilterModel() {
        override val type: FilterType = FilterType.MULTI
        override val filterResolver: FilterResolver = FilterResolver.MATERIAL
        override val uiName: String = "Material"
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

    object Brand : FilterModel() {
        override val type: FilterType = FilterType.MULTI
        override val filterResolver: FilterResolver = FilterResolver.BRANDS
        override val uiName: String = "Brands"
        override val priority: Int = 4

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val values = database.brandDao.getAll().map {
                JSONObject().apply {
                    put(Filters.PARAM_ID, it.id)
                    put(Filters.PARAM_UI_NAME, it.name)
                }
            }.toJsonArray()
            json.put(Filters.PARAM_VALUES, values)
        }
    }

    object Size : FilterModel() {
        override val type: FilterType = FilterType.MULTI
        override val filterResolver: FilterResolver = FilterResolver.SIZES
        override val uiName: String = "Sizes"
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

    object Price : FilterModel() {
        override val type: FilterType = FilterType.RANGE
        override val filterResolver: FilterResolver = FilterResolver.PRICE
        override val uiName: String = "Price"
        override val priority: Int = 2

        override suspend fun putFilterValues(json: JSONObject, database: PassionWomanDatabase) {
            val minValue = database.productDao.getMinPrice()
            val maxValue = database.productDao.getMaxPrice()
            json.put(Filters.PARAM_MIN_VALUE, minValue)
            json.put(Filters.PARAM_MAX_VALUE, maxValue)
        }
    }

    object Color : FilterModel() {
        override val type: FilterType = FilterType.COLOR
        override val filterResolver: FilterResolver = FilterResolver.COLOR
        override val uiName: String = "Color"
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

    object Model : FilterModel() {
        override val type: FilterType = FilterType.MULTI
        override val filterResolver: FilterResolver = FilterResolver.MODEL
        override val uiName: String = "Model"
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

    object Type : FilterModel() {
        override val type: FilterType = FilterType.MULTI
        override val filterResolver: FilterResolver = FilterResolver.TYPE
        override val uiName: String = "Type"
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
