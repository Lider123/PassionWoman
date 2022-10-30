package ru.babaetskv.passionwoman.data.gateway.decorator

import android.net.Uri
import ru.babaetskv.passionwoman.domain.gateway.exception.GatewayExceptionProvider
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable

class SafeProfileGatewayDecorator(
    private val gateway: ProfileGateway,
    private val exceptionProvider: GatewayExceptionProvider
) : ProfileGateway {

    override suspend fun getFavoriteIds(): List<Long> =
        try {
            gateway.getFavoriteIds()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getOrders(): List<Transformable<DateTimeConverter, Order>> =
        try {
            gateway.getOrders()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun getProfile(): Transformable<Unit, Profile> =
        try {
            gateway.getProfile()
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun setFavoriteIds(ids: List<Long>) =
        try {
            gateway.setFavoriteIds(ids)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun updateAvatar(imageUri: Uri) =
        try {
            gateway.updateAvatar(imageUri)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }

    override suspend fun updateProfile(profile: Profile) =
        try {
            gateway.updateProfile(profile)
        } catch (e: Exception) {
            throw exceptionProvider.getGatewayException(e)
        }
}
