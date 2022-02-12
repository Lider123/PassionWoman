package ru.babaetskv.passionwoman.domain.usecase

import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.exceptions.NetworkDataException
import ru.babaetskv.passionwoman.domain.model.HomeData
import ru.babaetskv.passionwoman.domain.usecase.base.NoParamsUseCase

interface GetHomeDataUseCase : NoParamsUseCase<HomeData> {

    class GetHomeDataException(
        cause: Exception?,
        stringProvider: StringProvider
    ) : NetworkDataException(stringProvider.GET_HOME_DATA_ERROR, cause)
}
