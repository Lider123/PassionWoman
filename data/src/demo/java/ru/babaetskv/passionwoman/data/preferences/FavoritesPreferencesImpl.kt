package ru.babaetskv.passionwoman.data.preferences

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import java.util.*

class FavoritesPreferencesImpl : FavoritesPreferences {
    private val actionsChannel = Channel<FavoritesPreferences.Action?>(Channel.RENDEZVOUS)
    private val favoriteIds = LinkedList<Long>()

    override val favoritesUpdatesFlow: Flow<FavoritesPreferences.Action?>
        get() = actionsChannel.receiveAsFlow()

    override fun isFavorite(id: Long): Boolean = favoriteIds.contains(id)

    override fun putFavoriteId(id: Long) {
        if (favoriteIds.contains(id)) return

        favoriteIds.add(0, id)
        actionsChannel.trySend(FavoritesPreferences.Action.Put(id))
    }

    override fun getFavoriteIds(): Collection<Long> = favoriteIds

    override fun setFavoriteIds(ids: Collection<Long>) {
        favoriteIds.run {
            clear()
            addAll(ids)
        }
        actionsChannel.trySend(FavoritesPreferences.Action.Set)
    }

    override fun setFavoriteIds(vararg ids: Long) {
        favoriteIds.run {
            clear()
            addAll(ids.toList())
        }
        actionsChannel.trySend(FavoritesPreferences.Action.Set)
    }

    override fun deleteFavoriteId(id: Long) {
        if (!favoriteIds.contains(id)) return

        favoriteIds.run {
            remove(id)
            actionsChannel.trySend(FavoritesPreferences.Action.Delete(id))
        }
    }

    override fun reset() {
        favoriteIds.clear()
    }
}
