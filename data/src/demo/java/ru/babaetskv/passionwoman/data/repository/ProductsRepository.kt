package ru.babaetskv.passionwoman.data.repository

import ru.babaetskv.passionwoman.domain.model.Product

interface ProductsRepository {
    suspend fun saveProduct(product: Product)
    suspend fun dump()
}
