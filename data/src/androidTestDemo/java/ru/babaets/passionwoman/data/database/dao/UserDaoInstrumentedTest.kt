package ru.babaets.passionwoman.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.data.database.dao.UserDao
import ru.babaetskv.passionwoman.data.database.entity.UserEntity
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserDaoInstrumentedTest {
    private lateinit var database: PassionWomanDatabase
    private lateinit var userDao: UserDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PassionWomanDatabase::class.java)
            .build()
        userDao = database.userDao
    }

    @Test
    @Throws(Exception::class)
    fun getProfile_returnsNull_whenThereIsNoProfileInTheDatabase() = runTest {
        val result = userDao.getProfile()

        assertNull(result)
    }

    @Test
    @Throws(Exception::class)
    fun getProfile_returnsProfile_whenThereIsProfileInTheDatabase() = runTest {
        val userEntity = UserEntity(
            id = 1,
            name = "Jane",
            surname = "Doe",
            phone = "9100000000",
            avatar = null
        )
        userDao.insert(userEntity)

        val result = userDao.getProfile()

        assertEquals(userEntity, result)
    }

    @After
    @Throws(IOException::class)
    fun after() {
        database.close()
    }
}
