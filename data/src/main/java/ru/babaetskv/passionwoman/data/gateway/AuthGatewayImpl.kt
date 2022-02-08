package ru.babaetskv.passionwoman.data.gateway

import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.api.PassionWomanApi
import ru.babaetskv.passionwoman.data.model.AccessTokenModel
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.gateway.AuthGateway
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import java.io.File
import java.net.URI

class AuthGatewayImpl(
    private val api: PassionWomanApi,
    private val commonApi: CommonApi
) : AuthGateway {

    override suspend fun getProfile(): Transformable<Unit, Profile> = api.getProfile()

    override suspend fun authorize(accessToken: String): String =
        commonApi.authorize(AccessTokenModel(accessToken)).token

    override suspend fun updateProfile(profile: Profile) {
        api.updateProfile(ProfileModel.fromProfile(profile))
    }

    override suspend fun updateAvatar(imageUri: Uri) {
        imageUri.toString().let {
            if (it.isNotEmpty() && it.startsWith("file://")) {
                api.uploadAvatar(getImagePart(it))
            }
        }
    }

    private fun getImagePart(path: String): MultipartBody.Part {
        val file = File(URI.create(path))
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }
}
