package ru.babaetskv.passionwoman.data.preferences

import android.content.Context
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

class PreferencesProviderImpl(context: Context) : PreferencesProvider {

    override fun provideAppPreferences(): AppPreferences = AppPreferencesImpl()

    override fun provideAuthPreferences(): AuthPreferences = AuthPreferencesImpl()

    override fun provideFavoritesPreferences(): FavoritesPreferences = FavoritesPreferencesImpl()
}
