package ru.babaetskv.passionwoman.data.gateway.decorator

import android.net.Uri
import ru.babaetskv.passionwoman.data.gateway.decorator.base.ProfileGatewayDecorator
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable

class SafeProfileGatewayDecorator(
    gateway: ProfileGateway,
    private val exceptionProvider: GatewayExceptionProvider
) : ProfileGatewayDecorator(gateway) {

    override suspend fun getFavoriteIds(): List<Long> =
        try {
            super.getFavoriteIds()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getOrders(): List<Transformable<DateTimeConverter, Order>> =
        try {
            super.getOrders()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getProfile(): Transformable<Unit, Profile> =
        try {
            super.getProfile()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun setFavoriteIds(ids: List<Long>) =
        try {
            super.setFavoriteIds(ids)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun updateAvatar(imageUri: Uri) =
        try {
            super.updateAvatar(imageUri)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun updateProfile(profile: Profile) =
        try {
            super.updateProfile(profile)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }
}
