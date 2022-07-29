package ru.babaetskv.passionwoman.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.babaetskv.passionwoman.data.database.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users LIMIT 1")
    fun getProfile(): UserEntity
}
