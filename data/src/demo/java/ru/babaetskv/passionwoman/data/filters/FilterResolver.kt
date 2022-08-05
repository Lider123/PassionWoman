package ru.babaetskv.passionwoman.data.filters

import org.json.JSONObject

enum class FilterResolver(
    val code: String,
    val resolveFilter: (JSONObject) -> Filter?,
    val filterModel: FilterModel
) {
    CATEGORY("category", Filter::Categories, FilterModel.Category),
    BRANDS("brands", Filter::Brands, FilterModel.Brand),
    SIZES("sizes", Filter::Sizes, FilterModel.Size),
    PRICE("price", Filter::Price, FilterModel.Price),
    COLOR("color", Filter::Color, FilterModel.Color),
    SEASON("season", Filter::Season, FilterModel.Season),
    MATERIAL("material", Filter::Material, FilterModel.Material),
    STYLE("style", Filter::Style, FilterModel.Style),
    COUNTRY("country", Filter::Country, FilterModel.Country),
    TYPE("type", Filter::Type, FilterModel.Type),
    MODEL("model", Filter::Model, FilterModel.Model);

    companion object {

        fun findByCode(code: String): FilterResolver? =
            values().find { it.code == code }
    }
}
