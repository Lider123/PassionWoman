package ru.babaetskv.passionwoman.app.utils.deeplink

interface ExternalDeeplinkGenerator : DeeplinkGenerator {

    companion object {
        const val URI_PREFIX = "https://passionwoman.page.link"
    }
}
