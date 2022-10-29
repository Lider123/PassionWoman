package ru.babaetskv.passionwoman.data.gateway

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.gateway.base.BaseGatewayImpl
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.StringProvider
import ru.babaetskv.passionwoman.domain.gateway.ProfileGateway
import ru.babaetskv.passionwoman.domain.model.Order
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.model.base.Transformable
import java.io.File

class ProfileGatewayImpl(
    private val api: AuthApi,
    stringProvider: StringProvider
) : BaseGatewayImpl(stringProvider), ProfileGateway {

    override suspend fun getProfile(): Transformable<Unit, Profile> = withContext(Dispatchers.IO) {
        return@withContext networkRequest {
            api.getProfile()
        }
    }

    override suspend fun updateProfile(profile: Profile) = withContext(Dispatchers.IO) {
        return@withContext networkRequest {
            api.updateProfile(ProfileModel(profile))
        }
    }

    override suspend fun updateAvatar(imageUri: Uri) = withContext(Dispatchers.IO) {
        return@withContext networkRequest {
            imageUri.toString().let {
                if (it.isNotEmpty() && it.startsWith("file://")) {
                    api.uploadAvatar(getImagePart(it))
                }
            }
        }
    }

    override suspend fun getFavoriteIds(): List<Long> = withContext(Dispatchers.IO) {
        return@withContext networkRequest {
            api.getFavoriteIds()
        }
    }

    override suspend fun setFavoriteIds(ids: List<Long>) = withContext(Dispatchers.IO) {
        return@withContext networkRequest {
            api.setFavoriteIds(ids)
        }
    }

    override suspend fun getOrders(): List<Transformable<DateTimeConverter, Order>> =
        withContext(Dispatchers.IO) {
            return@withContext networkRequest {
                api.getOrders()
            }
        }

    private fun getImagePart(path: String): MultipartBody.Part {
        val file = File(path)
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }
}
