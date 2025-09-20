package ru.babaetskv.passionwoman.data.api

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import retrofit2.HttpException
import ru.babaetskv.passionwoman.data.api.exception.ApiExceptionProvider
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.UserDao
import ru.babaetskv.passionwoman.data.database.entity.UserEntity
import ru.babaetskv.passionwoman.data.model.ProfileModel
import ru.babaetskv.passionwoman.domain.DateTimeConverter

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthApiImplTest {
    private val userDaoMock: UserDao = mock()
    private val exceptionProviderMock: ApiExceptionProvider = mock()
    private val databaseMock: PassionWomanDatabase = mock {
        whenever(mock.userDao) doReturn userDaoMock
    }
    private val dateTimeConverter: DateTimeConverter = mock()

    private lateinit var authApiImpl: AuthApiImpl

    @Before
    fun before() {
        authApiImpl = AuthApiImpl(databaseMock, exceptionProviderMock, dateTimeConverter)
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

        verify(userDaoMock, times(1))
            .getProfile()
    }

    @Test
    fun getProfile_throwsNotFound_whenThereIsNoProfileInTheDatabase() = runTest {
        val notFoundException: HttpException = mock()
        whenever(exceptionProviderMock.getNotFoundException(any())) doReturn notFoundException

        try {
            authApiImpl.getProfile()
            fail()
        } catch (e: HttpException) {
            verify(exceptionProviderMock, times(1))
                .getNotFoundException("Profile is not found")
            assertEquals(notFoundException, e)
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
