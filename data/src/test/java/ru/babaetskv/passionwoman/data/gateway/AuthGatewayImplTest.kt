package ru.babaetskv.passionwoman.data.gateway

import android.net.Uri
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.api.PassionWomanApi
import ru.babaetskv.passionwoman.data.model.AuthTokenModel
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.model.Profile
import ru.babaetskv.passionwoman.domain.utils.transform

@RunWith(MockitoJUnitRunner::class)
class AuthGatewayImplTest {
    @Mock
    private lateinit var apiMock: PassionWomanApi
    @Mock
    private lateinit var commonApiMock: CommonApi
    @InjectMocks
    private lateinit var gateway: AuthGatewayImpl

    private fun createProfileMock() =
        ProfileModel(
            id = "profile1",
            name = "John",
            surname = "Doe",
            phone = "0000000000",
            avatar = null
        )

    @Test
    fun getProfile_returnsProfileFromApi() = runBlocking {
        val profileMock = createProfileMock()
        whenever(apiMock.getProfile()).doReturn(profileMock)

        assertEquals(profileMock, gateway.getProfile())
    }

    @Test
    fun updateProfile_updatesApiProfile() = runBlocking {
        val profileMock = createProfileMock()

        gateway.updateProfile(profileMock.transform())

        verify(apiMock, times(1)).updateProfile(profileMock)
    }

    @Test
    fun authorize_returnsToken(): Unit = runBlocking {
        val tokenMock = AuthTokenModel("token")
        whenever(commonApiMock.authorize(any())).doReturn(tokenMock)

        assertEquals(tokenMock.token, gateway.authorize(any()))
    }

    @Test
    fun updateAvatar_updatesApiAvatar_whenUriIsCorrect() = runBlocking {
        val uriMock = mock<Uri>()
        whenever(uriMock.toString()).doReturn("file://sample_file")

        gateway.updateAvatar(uriMock)

        verify(apiMock, times(1)).uploadAvatar(any())
    }

    @Test
    fun updateAvatar_doesNotUpdateApiAvatar_whenUriIsEmpty() = runBlocking {
        val uriMock = mock<Uri>()
        whenever(uriMock.toString()).doReturn("")

        gateway.updateAvatar(uriMock)

        verify(apiMock, times(0)).uploadAvatar(any())
    }

    @Test
    fun updateAvatar_doesNotUpdateApiAvatar_whenUriIsNotFile() = runBlocking {
        val uriMock = mock<Uri>()
        whenever(uriMock.toString()).doReturn("sample_file")

        gateway.updateAvatar(uriMock)

        verify(apiMock, times(0)).uploadAvatar(any())
    }
}
