package ru.babaetskv.passionwoman.app

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.babaetskv.passionwoman.domain.AppDispatchers

class DefaultAppDispatchers : AppDispatchers {
    override val IO: CoroutineDispatcher = Dispatchers.IO
    override val Main: CoroutineDispatcher = Dispatchers.Main
}
