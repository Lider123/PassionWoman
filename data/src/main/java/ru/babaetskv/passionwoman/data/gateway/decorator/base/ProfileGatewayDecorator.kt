package ru.babaetskv.passionwoman.data.gateway.decorator.base

import android.net.Uri
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable

abstract class ProfileGatewayDecorator(
    val gateway: ProfileGateway
) : ProfileGateway {

    override suspend fun getFavoriteIds(): List<Long> = gateway.getFavoriteIds()

    override suspend fun getOrders(): List<Transformable<DateTimeConverter, Order>> =
        gateway.getOrders()

    override suspend fun getProfile(): Transformable<Unit, Profile> = gateway.getProfile()

    override suspend fun setFavoriteIds(ids: List<Long>) {
        gateway.setFavoriteIds(ids)
    }

    override suspend fun updateAvatar(imageUri: Uri) {
        gateway.updateAvatar(imageUri)
    }

    override suspend fun updateProfile(profile: Profile) {
        gateway.updateProfile(profile)
    }
}
