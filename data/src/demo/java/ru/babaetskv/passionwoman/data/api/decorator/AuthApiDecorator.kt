package ru.babaetskv.passionwoman.data.api.decorator

import ru.babaetskv.passionwoman.data.api.AuthApi

abstract class AuthApiDecorator(
    protected val api: AuthApi
) : AuthApi
