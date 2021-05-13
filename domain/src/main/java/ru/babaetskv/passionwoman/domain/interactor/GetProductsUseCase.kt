package ru.babaetskv.passionwoman.domain.interactor

import ru.babaetskv.passionwoman.domain.interactor.base.BaseUseCase
import ru.babaetskv.passionwoman.domain.model.Product
import ru.babaetskv.passionwoman.domain.repository.CatalogRepository

class GetProductsUseCase(
    private val catalogRepository: CatalogRepository
) : BaseUseCase<String, List<Product>>() {

    override suspend fun run(params: String): List<Product> = catalogRepository.getProducts(params)
}
