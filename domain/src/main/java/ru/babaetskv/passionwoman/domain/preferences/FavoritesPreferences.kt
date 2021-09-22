package ru.babaetskv.passionwoman.domain.preferences

import kotlinx.coroutines.flow.Flow

interface FavoritesPreferences {
    val favoritesUpdatesFlow: Flow<Action?>

    fun putFavoriteId(id: String)
    fun setFavoriteIds(ids: Collection<String>)
    fun setFavoriteIds(vararg ids: String)
    fun getFavoriteIds(): Collection<String>
    fun deleteFavoriteId(id: String)
    fun isFavorite(id: String): Boolean
    fun reset()

    sealed class Action {

        object Set : Action()

        data class Put(
            val favoriteId: String
        ) : Action()

        data class Delete(
            val favoriteId: String
        ) : Action()
    }
}
