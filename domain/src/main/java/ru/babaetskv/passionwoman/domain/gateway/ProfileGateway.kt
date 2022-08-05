package ru.babaetskv.passionwoman.domain.gateway

import android.net.Uri
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable

interface ProfileGateway {
    suspend fun getProfile(): Transformable<Unit, Profile>
    suspend fun updateProfile(profile: Profile)
    suspend fun updateAvatar(imageUri: Uri)
    suspend fun getFavoriteIds(): List<Int>
    suspend fun setFavoriteIds(ids: List<Int>)
    suspend fun getOrders(): List<Transformable<DateTimeConverter, Order>> // TODO: add pagination
}
