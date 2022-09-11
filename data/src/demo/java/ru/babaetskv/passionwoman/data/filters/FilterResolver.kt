package ru.babaetskv.passionwoman.data.filters

import org.json.JSONObject

enum class FilterResolver(
    val code: String,
    val uiName: String,
    val resolveFilter: (JSONObject) -> Filter?,
    val getFilterExtractor: () -> FilterExtractor
) {
    CATEGORY("category", "Categories", Filter::Categories, FilterExtractor::Category),
    BRANDS("brands", "Brands", Filter::Brands, FilterExtractor::Brand),
    SIZES("sizes", "Sizes", Filter::Sizes, FilterExtractor::Size),
    PRICE("price", "Price", Filter::Price, FilterExtractor::Price),
    COLOR("color", "Color", Filter::Color, FilterExtractor::Color),
    SEASON("season", "Season", Filter::Season, FilterExtractor::Season),
    MATERIAL("material", "Material", Filter::Material, FilterExtractor::Material),
    STYLE("style", "Style", Filter::Style, FilterExtractor::Style),
    COUNTRY("country", "Country", Filter::Country, FilterExtractor::Country),
    TYPE("type", "Type", Filter::Type, FilterExtractor::Type),
    MODEL("model", "Model", Filter::Model, FilterExtractor::Extractor);

    companion object {

        fun findByCode(code: String): FilterResolver? =
            values().find { it.code == code }
    }
}
