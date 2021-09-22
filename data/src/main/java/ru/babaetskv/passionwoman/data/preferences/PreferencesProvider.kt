package ru.babaetskv.passionwoman.data.preferences

import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

interface PreferencesProvider {
    fun provideAppPreferences(): AppPreferences
    fun provideAuthPreferences(): AuthPreferences
    fun provideFavoritesPreferences(): FavoritesPreferences
}
