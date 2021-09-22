package ru.babaetskv.passionwoman.data.gateway

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.data.api.CommonApi
import ru.babaetskv.passionwoman.data.api.PassionWomanApi
import ru.babaetskv.passionwoman.data.model.AuthTokenModel
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.model.Image
import ru.babaetskv.passionwoman.domain.model.Profile

@RunWith(JUnit4::class)
class AuthGatewayImplTest {

    private fun createDummyProfile(hasAvatar: Boolean): ProfileModel =
        ProfileModel(
            id = "1",
            avatar = if (hasAvatar) "sampleAvatar" else null,
            name = "Jane",
            surname = "Doe",
            phone = "9600000001"
        )

    @Test
    fun getProfile_withAvatar_returnsProfile() {
        val stubApi = mock<PassionWomanApi> {
            on {
                runBlocking {
                    getProfile()
                }
            } doReturn createDummyProfile(true)
        }
        val stubCommonApi = mock<CommonApi>()
        val authGateway = AuthGatewayImpl(stubApi, stubCommonApi)
        val expected = Profile(
            id = "1",
            avatar = Image("sampleAvatar"),
            name = "Jane",
            surname = "Doe",
            phone = "9600000001"
        )
        runBlocking {
            val result = authGateway.getProfile()
            assertEquals(expected, result)
            verify(stubApi, times(1)).getProfile()
        }
    }

    @Test
    fun getProfile_withoutAvatar_returnsProfile() {
        val stubApi = mock<PassionWomanApi> {
            on {
                runBlocking {
                    getProfile()
                }
            } doReturn createDummyProfile(false)
        }
        val stubCommonApi = mock<CommonApi>()
        val authGateway = AuthGatewayImpl(stubApi, stubCommonApi)
        val expected = Profile(
            id = "1",
            avatar = null,
            name = "Jane",
            surname = "Doe",
            phone = "9600000001"
        )
        runBlocking {
            val result = authGateway.getProfile()
            assertEquals(expected, result)
            verify(stubApi, times(1)).getProfile()
        }
    }

    @Test
    fun authorize_randomData_isCorrect() {
        val stubApi = mock<PassionWomanApi>()
        val stubCommonApi = mock<CommonApi> {
            on {
                runBlocking {
                    authorize(any())
                }
            } doReturn AuthTokenModel("sampleToken")
        }
        val authGateway = AuthGatewayImpl(stubApi, stubCommonApi)
        val expected = "sampleToken"
        runBlocking {
            val result = authGateway.authorize("randomToken")
            assertEquals(expected, result)
            verify(stubCommonApi, times(1)).authorize(any())
        }
    }
}
