package ru.babaetskv.passionwoman.data.gateway

import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import java.io.File

class ProfileGatewayImpl(
    private val api: AuthApi
) : ProfileGateway {

    override suspend fun getProfile(): Transformable<Unit, Profile> = api.getProfile()

    override suspend fun updateProfile(profile: Profile) {
        api.updateProfile(ProfileModel(profile))
    }

    override suspend fun updateAvatar(imageUri: Uri) {
        imageUri.toString().let {
            if (it.isNotEmpty() && it.startsWith("file://")) {
                api.uploadAvatar(getImagePart(it))
            }
        }
    }

    override suspend fun getFavoriteIds(): List<String> = api.getFavoriteIds()

    override suspend fun setFavoriteIds(ids: List<String>) {
        api.setFavoriteIds(ids)
    }

    override suspend fun getOrders(): List<Transformable<DateTimeConverter, Order>> =
        api.getOrders()

    private fun getImagePart(path: String): MultipartBody.Part {
        val file = File(path)
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }
}
