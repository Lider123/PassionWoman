package ru.babaetskv.passionwoman.domain.preferences

import kotlinx.coroutines.flow.Flow

interface FavoritesPreferences {
    val favoritesUpdatesFlow: Flow<Action?>

    fun putFavoriteId(id: Long)
    fun setFavoriteIds(ids: Collection<Long>)
    fun setFavoriteIds(vararg ids: Long)
    fun getFavoriteIds(): Collection<Long>
    fun deleteFavoriteId(id: Long)
    fun isFavorite(id: Long): Boolean
    fun reset()

    sealed class Action {

        object Set : Action()

        data class Put(
            val favoriteId: Long
        ) : Action()

        data class Delete(
            val favoriteId: Long
        ) : Action()
    }
}
