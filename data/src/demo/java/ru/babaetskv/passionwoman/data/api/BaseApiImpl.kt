package ru.babaetskv.passionwoman.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

abstract class BaseApiImpl {

    protected open fun doBeforeRequest() = Unit

    protected suspend fun <T> processRequest(
        delayMs: Long = DELAY_LOADING,
        block: suspend () -> T
    ): T = withContext(Dispatchers.IO) {
        delay(delayMs)
        doBeforeRequest()
        return@withContext block.invoke()
    }

    companion object {
        @JvmStatic
        protected val DELAY_LOADING = 500L
        @JvmStatic
        val TOKEN = "token"
    }
}
