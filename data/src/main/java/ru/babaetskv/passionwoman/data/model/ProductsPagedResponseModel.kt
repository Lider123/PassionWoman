package ru.babaetskv.passionwoman.data.model

import com.squareup.moshi.Json
import org.json.JSONObject
import ru.babaetskv.passionwoman.domain.interactor.exception.StringProvider
import ru.babaetskv.passionwoman.domain.model.ProductsPagedResponse
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import ru.babaetskv.passionwoman.domain.model.filters.Filter
import ru.babaetskv.passionwoman.domain.utils.transformList

data class ProductsPagedResponseModel(
    @Json(name = "products") val products: List<ProductModel>,
    @Json(name = "total") val total: Int,
    @Json(name = "availableFilters") val availableFilters: List<JSONObject>
) : Transformable<StringProvider, ProductsPagedResponse> {

    override fun transform(params: StringProvider): ProductsPagedResponse =
        ProductsPagedResponse(
            products = products.transformList(),
            total = total,
            availableFilters = availableFilters.mapNotNull { Filter.fromJson(it) }
                + listOf(Filter.DiscountOnly(params))
        )
}
