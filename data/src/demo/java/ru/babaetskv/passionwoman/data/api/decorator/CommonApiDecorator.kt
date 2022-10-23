package ru.babaetskv.passionwoman.data.api.decorator

import ru.babaetskv.passionwoman.data.api.CommonApi

abstract class CommonApiDecorator(
    protected val api: CommonApi
) : CommonApi
