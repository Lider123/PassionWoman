package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.EmptyDataException
import ru.babaetskv.passionwoman.domain.exceptions.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.Category
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetCategoriesUseCase : NoParamsUseCase<List<Category>> {

    class GetCategoriesException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkDataException(stringProvider.GET_CATEGORIES_ERROR, cause)

    class EmptyCategoriesException(
        stringProvider: StringProvider
    ) : EmptyDataException(stringProvider.EMPTY_CATEGORIES_ERROR)
}
