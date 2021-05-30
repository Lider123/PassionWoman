package ru.babaetskv.passionwoman.domain.gateway

import android.net.Uri
import ru.babaetskv.passionwoman.domain.model.Profile

interface AuthGateway {
    suspend fun getProfile(): Profile
    suspend fun authorize(accessToken: String): String
    suspend fun updateProfile(profile: Profile)
    suspend fun updateAvatar(imageUri: Uri)
}