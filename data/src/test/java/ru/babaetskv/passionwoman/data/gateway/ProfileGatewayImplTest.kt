package ru.babaetskv.passionwoman.data.gateway

import android.net.Uri
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.data.api.AuthApi
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.utils.transform

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProfileGatewayImplTest {
    @Mock
    private lateinit var authApiMock: AuthApi
    @InjectMocks
    private lateinit var gateway: ProfileGatewayImpl

    private fun createProfileMock() =
        ProfileModel(
            id = "profile1",
            name = "John",
            surname = "Doe",
            phone = "0000000000",
            avatar = null
        )

    @Test
    fun getProfile_returnsProfile() = runTest {
        val profileMock = createProfileMock()
        whenever(authApiMock.getProfile()).doReturn(profileMock)

        val result = gateway.getProfile()

        assertEquals(profileMock, result)
    }

    @Test
    fun getProfile_callsAuthApi() = runTest {
        gateway.getProfile()

        verify(authApiMock, times(1)).getProfile()
    }

    @Test
    fun updateProfile_callsAuthapi() = runTest {
        val profileMock = createProfileMock()

        gateway.updateProfile(profileMock.transform())

        verify(authApiMock, times(1)).updateProfile(profileMock)
    }

    @Test
    fun updateAvatar_callsAuthApi_whenUriIsCorrect() = runTest {
        val uriMock = mock<Uri>()
        whenever(uriMock.toString()).doReturn("file://sample_file")

        gateway.updateAvatar(uriMock)

        verify(authApiMock, times(1)).uploadAvatar(any())
    }

    @Test
    fun updateAvatar_doesNotUpdateApiAvatar_whenUriIsEmpty() = runTest {
        val uriMock = mock<Uri>()
        whenever(uriMock.toString()).doReturn("")

        gateway.updateAvatar(uriMock)

        verify(authApiMock, times(0)).uploadAvatar(any())
    }

    @Test
    fun updateAvatar_doesNotUpdateApiAvatar_whenUriIsNotFile() = runTest {
        val uriMock = mock<Uri>()
        whenever(uriMock.toString()).doReturn("sample_file")

        gateway.updateAvatar(uriMock)

        verify(authApiMock, times(0)).uploadAvatar(any())
    }
}
