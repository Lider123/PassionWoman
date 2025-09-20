package ru.babaetskv.passionwoman.app.presentation.feature.productlist

import ru.babaetskv.passionwoman.app.presentation.base.NewPager
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.usecase.GetProductsUseCase

class ProductsPagingExceptionProvider(
    private val stringProvider: StringProvider
) : NewPager.PagingExceptionProvider {
    override val emptyError: UseCaseException.EmptyData
        get() = GetProductsUseCase.EmptyProductsException(stringProvider)

    override fun getListError(cause: Exception): UseCaseException.Data =
        GetProductsUseCase.GetProductsException(cause, stringProvider)

    override fun getPageError(cause: Exception): UseCaseException.Data =
        GetProductsUseCase.GetProductsPageException(cause, stringProvider)
}
