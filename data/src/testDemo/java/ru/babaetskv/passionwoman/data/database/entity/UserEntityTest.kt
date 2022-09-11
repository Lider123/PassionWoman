package ru.babaetskv.passionwoman.data.database.entity

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import ru.babaetskv.passionwoman.domain.model.base.Transformable.Companion.transform

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserEntityTest {
    private val user = UserEntity(
        id = 1,
        name = "Jane",
        surname = "Doe",
        phone = "+79000000000",
        avatar = "static/image/user_1.jpg"
    )

    @Test
    fun transform_returnsModelWithTheSameId() = runTest {
        val result = user.transform()

        assertEquals(1, result.id)
    }

    @Test
    fun transform_returnsModelWithTheSameName() = runTest {
        val result = user.transform()

        assertEquals("Jane", result.name)
    }

    @Test
    fun transform_returnsModelWithEmptyName_whenNameIsNull() = runTest {
        val result = user.copy(
            name = null
        ).transform()

        assertEquals("", result.name)
    }

    @Test
    fun transform_returnsModelWithTheSameSurname() = runTest {
        val result = user.transform()

        assertEquals("Doe", result.surname)
    }

    @Test
    fun transform_returnsModelWithEmptySurname_whenSurnameIsNull() = runTest {
        val result = user.copy(
            surname = null
        ).transform()

        assertEquals("", result.surname)
    }

    @Test
    fun transform_returnsModelWithFormattedAvatar() = runTest {
        val result = user.transform()

        assertEquals("asset:///demo_db_editor/static/image/user_1.jpg", result.avatar)
    }

    @Test
    fun transform_returnsModelWithNullAvatar_whenAvatarIsNull() = runTest {
        val result = user.copy(
            avatar = null
        ).transform()

        assertNull(result.avatar)
    }
}
