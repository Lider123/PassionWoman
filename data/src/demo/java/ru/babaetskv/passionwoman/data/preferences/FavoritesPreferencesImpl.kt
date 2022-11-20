package ru.babaetskv.passionwoman.data.preferences

import com.chibatching.kotpref.KotprefModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

class FavoritesPreferencesImpl : KotprefModel(), FavoritesPreferences {
    private var favoriteIdsPref: String by stringPref()
    private val actionsChannel = Channel<FavoritesPreferences.Action?>(Channel.RENDEZVOUS)
    private val favoriteIds: MutableList<Long>
        get() = object : MutableList<Long> by readFavoriteIdsPref() {

            override fun add(index: Int, element: Long) {
                favoriteIdsPref = readFavoriteIdsPref()
                    .apply {
                        add(index, element)
                    }
                    .joinToString(",")
            }

            override fun remove(element: Long): Boolean {
                val result: Boolean
                favoriteIdsPref = readFavoriteIdsPref()
                    .apply {
                        result = remove(element)
                    }
                    .joinToString(",")
                return result
            }

            override fun clear() {
                favoriteIdsPref = ""
            }

            override fun addAll(elements: Collection<Long>): Boolean {
                val result: Boolean
                favoriteIdsPref = readFavoriteIdsPref()
                    .apply {
                        result = addAll(elements)
                    }
                    .joinToString(",")
                return result
            }
        }

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

    private fun readFavoriteIdsPref(): MutableList<Long> =
        favoriteIdsPref.takeIf(String::isNotEmpty)
            ?.split(",")
            ?.map(String::toLong)
            ?.toMutableList()
            ?: mutableListOf()

    override fun reset() {
        favoriteIds.clear()
    }
}
