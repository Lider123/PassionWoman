package ru.babaetskv.passionwoman.data.gateway

import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.api.PassionWomanApi
import ru.babaetskv.passionwoman.data.model.AccessTokenModel
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.model.Profile

class AuthGatewayImpl(
    private val api: PassionWomanApi,
    private val commonApi: CommonApi
) : AuthGateway {

    override suspend fun getProfile(): Profile = api.getProfile().toProfile()

    override suspend fun authorize(accessToken: String): String =
        commonApi.authorize(AccessTokenModel(accessToken)).token

    override suspend fun updateProfile(profile: Profile) {
        api.updateProfile(ProfileModel.fromProfile(profile))
    }
}
