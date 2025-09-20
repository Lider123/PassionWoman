package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.UseCaseException
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetCategoriesUseCase : NoParamsUseCase<List<Category>> {

    class GetCategoriesException(
        cause: Exception,
        stringProvider: StringProvider
    ) : UseCaseException.Data(cause, stringProvider.GET_CATEGORIES_ERROR)

    class EmptyCategoriesException(
        stringProvider: StringProvider
    ) : UseCaseException.EmptyData(stringProvider.EMPTY_CATEGORIES_ERROR)
}
