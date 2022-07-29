package ru.babaetskv.passionwoman.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.babaetskv.passionwoman.data.database.dao.BrandDao
import ru.babaetskv.passionwoman.data.database.dao.CategoryDao
import ru.babaetskv.passionwoman.data.database.dao.ColorDao
import ru.babaetskv.passionwoman.data.database.dao.UserDao
import ru.babaetskv.passionwoman.data.database.entity.BrandEntity
import ru.babaetskv.passionwoman.data.database.entity.CategoryEntity
import ru.babaetskv.passionwoman.data.database.entity.ColorEntity
import ru.babaetskv.passionwoman.data.database.entity.UserEntity

@Database(
    entities = [
        ColorEntity::class,
        BrandEntity::class,
        UserEntity::class,
        CategoryEntity::class
    ],
    version = 1
)
abstract class PassionWomanDatabase : RoomDatabase() {
    abstract val colorDao: ColorDao
    abstract val brandDao: BrandDao
    abstract val userDao: UserDao
    abstract val categoryDao: CategoryDao
}
