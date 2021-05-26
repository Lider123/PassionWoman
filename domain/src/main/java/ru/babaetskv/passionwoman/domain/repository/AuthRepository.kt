package ru.babaetskv.passionwoman.domain.repository

import ru.babaetskv.passionwoman.domain.model.Profile

interface AuthRepository {
    suspend fun getProfile(): Profile
    suspend fun authorize(accessToken: String): String
    suspend fun updateProfile(profile: Profile)
}