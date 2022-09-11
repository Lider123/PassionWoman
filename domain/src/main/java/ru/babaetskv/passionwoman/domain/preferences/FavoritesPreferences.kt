package ru.babaetskv.passionwoman.domain.preferences

import kotlinx.coroutines.flow.Flow

interface FavoritesPreferences {
    val favoritesUpdatesFlow: Flow<Action?>

    fun putFavoriteId(id: Int)
    fun setFavoriteIds(ids: Collection<Int>)
    fun setFavoriteIds(vararg ids: Int)
    fun getFavoriteIds(): Collection<Int>
    fun deleteFavoriteId(id: Int)
    fun isFavorite(id: Int): Boolean
    fun reset()

    sealed class Action {

        object Set : Action()

        data class Put(
            val favoriteId: Int
        ) : Action()

        data class Delete(
            val favoriteId: Int
        ) : Action()
    }
}
