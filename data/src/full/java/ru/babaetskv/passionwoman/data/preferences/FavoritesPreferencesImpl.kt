package ru.babaetskv.passionwoman.data.preferences

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import java.lang.StringBuilder

class FavoritesPreferencesImpl(context: Context) : KotprefModel(context), FavoritesPreferences {
    private val actionsChannel = Channel<FavoritesPreferences.Action?>(Channel.RENDEZVOUS)
    private var favoriteIds: String by stringPref()

    override val favoritesUpdatesFlow: Flow<FavoritesPreferences.Action?>
        get() = actionsChannel.receiveAsFlow()

    override fun isFavorite(id: String): Boolean = favoriteIds.contains(id)

    override fun putFavoriteId(id: String) {
        if (favoriteIds.contains(id)) return

        favoriteIds = id.joinIfNeeded(favoriteIds)
        actionsChannel.offer(FavoritesPreferences.Action.Put(id))
    }

    override fun getFavoriteIds(): Collection<String> {
        val values = favoriteIds.split(DELIMITER)
        if (values.size == 1 && values[0].isEmpty()) return emptyList()

        return values
    }

    override fun setFavoriteIds(ids: Collection<String>) {
        favoriteIds = ids.joinToString(DELIMITER)
        actionsChannel.offer(FavoritesPreferences.Action.Set)
    }

    override fun setFavoriteIds(vararg ids: String) {
        favoriteIds = ids.joinToString(DELIMITER)
        actionsChannel.offer(FavoritesPreferences.Action.Set)
    }

    override fun deleteFavoriteId(id: String) {
        if (!favoriteIds.contains(id)) return

        favoriteIds = favoriteIds.split(DELIMITER).filter { it != id }.joinToString(DELIMITER)
        actionsChannel.offer(FavoritesPreferences.Action.Delete(id))
    }

    override fun reset() {
        favoriteIds = ""
    }

    private fun String.joinIfNeeded(other: String, delimiter: String = DELIMITER): String =
        when {
            this.isEmpty() -> other
            other.isEmpty() -> this
            else -> StringBuilder(this).append(delimiter).append(other).toString()
        }

    companion object {
        private const val DELIMITER = ","
    }
}