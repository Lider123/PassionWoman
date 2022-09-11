package ru.babaetskv.passionwoman.data.api

import android.content.res.AssetManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import retrofit2.HttpException
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.UserDao
import ru.babaetskv.passionwoman.data.database.entity.UserEntity
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.DateTimeConverter
import ru.babaetskv.passionwoman.domain.preferences.AuthPreferences

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthApiImplTest {
    private val userDaoMock: UserDao = mock()

    private val assetManager: AssetManager = mock()
    private val database: PassionWomanDatabase = mock {
        whenever(mock.userDao) doReturn userDaoMock
    }
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val authPreferences: AuthPreferences = mock {
        whenever(mock.authToken) doReturn "token"
    }
    private val dateTimeConverter: DateTimeConverter = mock()

    private lateinit var authApiImpl: AuthApiImpl

    @Before
    fun before() {
        authApiImpl = AuthApiImpl(assetManager, database, moshi, authPreferences, dateTimeConverter)
    }

    @Test
    fun getProfile_callsDatabaseOneTime() = runTest {
        val profile = UserEntity(
            id = 1,
            name = "Jane",
            surname = "Doe",
            phone = "+79000000000",
            avatar = null
        )
        whenever(userDaoMock.getProfile()) doReturn profile

        authApiImpl.getProfile()
        authApiImpl.getProfile()

        verify(userDaoMock, times(1)).getProfile()
    }

    @Test
    fun getProfile_throwsUnauthorized_whenGotIncorrectToken() = runTest {
        whenever(authPreferences.authToken) doReturn "1234"

        try {
            authApiImpl.getProfile()
            fail()
        } catch (e: HttpException) {
            assertEquals(401, e.code())
            assertEquals("Unauthorized", e.response()?.errorBody()?.string())
        }
    }

    @Test
    fun getProfile_throwsNotFound_whenThereIsNoProfileInTheDatabase() = runTest {
        try {
            authApiImpl.getProfile()
            fail()
        } catch (e: HttpException) {
            assertEquals(404, e.code())
            assertEquals("Profile is not found", e.response()?.errorBody()?.string())
        }
    }

    @Test
    fun getProfile_returnsTheSameProfileEachTime() = runTest {
        val profile = UserEntity(
            id = 1,
            name = "Jane",
            surname = "Doe",
            phone = "+79000000000",
            avatar = null
        )
        whenever(userDaoMock.getProfile()) doReturn profile

        val result1 = authApiImpl.getProfile()
        val result2 = authApiImpl.getProfile()

        assertEquals(result1.hashCode(), result2.hashCode())
    }

    @Test
    fun getProfile_returnsProfile_whenThereIsProfileInTheDatabase() = runTest {
        val profile = UserEntity(
            id = 1,
            name = "Jane",
            surname = "Doe",
            phone = "+79000000000",
            avatar = null
        )
        whenever(userDaoMock.getProfile()) doReturn profile

        val result = authApiImpl.getProfile()

        val expected = ProfileModel(
            id = 1,
            name = "Jane",
            surname = "Doe",
            phone = "+79000000000",
            avatar = null
        )
        assertEquals(expected, result)
    }

    @Test
    fun updateProfile_throwsUnauthorized_whenGotIncorrectToken() = runTest {
        val profile = ProfileModel(
            id = 1,
            name = "Jane",
            surname = "Doe",
            phone = "+79000000000",
            avatar = null
        )
        whenever(authPreferences.authToken) doReturn "1234"

        try {
            authApiImpl.updateProfile(profile)
            fail()
        } catch (e: HttpException) {
            assertEquals(401, e.code())
            assertEquals("Unauthorized", e.response()?.errorBody()?.string())
        }
    }

    @Test
    fun updateProfile_savesProfile() = runTest {
        val profile = ProfileModel(
            id = 1,
            name = "Jane",
            surname = "Doe",
            phone = "+79000000000",
            avatar = null
        )

        authApiImpl.updateProfile(profile)
        val result = authApiImpl.getProfile()

        assertEquals(profile, result)
    }

    // TODO: add tests
}
