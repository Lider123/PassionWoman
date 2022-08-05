package ru.babaetskv.passionwoman.data.preferences

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences
import java.util.*

class FavoritesPreferencesImpl : FavoritesPreferences {
    private val actionsChannel = Channel<FavoritesPreferences.Action?>(Channel.RENDEZVOUS)
    private val favoriteIds = LinkedList<Int>()

    override val favoritesUpdatesFlow: Flow<FavoritesPreferences.Action?>
        get() = actionsChannel.receiveAsFlow()

    override fun isFavorite(id: Int): Boolean = favoriteIds.contains(id)

    override fun putFavoriteId(id: Int) {
        if (favoriteIds.contains(id)) return

        favoriteIds.add(0, id)
        actionsChannel.offer(FavoritesPreferences.Action.Put(id))
    }

    override fun getFavoriteIds(): Collection<Int> = favoriteIds

    override fun setFavoriteIds(ids: Collection<Int>) {
        favoriteIds.run {
            clear()
            addAll(ids)
        }
        actionsChannel.offer(FavoritesPreferences.Action.Set)
    }

    override fun setFavoriteIds(vararg ids: Int) {
        favoriteIds.run {
            clear()
            addAll(ids.toList())
        }
        actionsChannel.offer(FavoritesPreferences.Action.Set)
    }

    override fun deleteFavoriteId(id: Int) {
        if (!favoriteIds.contains(id)) return

        favoriteIds.run {
            remove(id)
            actionsChannel.offer(FavoritesPreferences.Action.Delete(id))
        }
    }

    override fun reset() {
        favoriteIds.clear()
    }
}
