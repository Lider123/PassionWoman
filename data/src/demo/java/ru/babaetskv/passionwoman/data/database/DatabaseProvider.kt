package ru.babaetskv.passionwoman.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private const val DATABASE_FILENAME = "demo_db_editor/passionwoman.db"
    private const val DATABASE_NAME = "passionwoman"

    private var database: PassionWomanDatabase? = null

    private fun createDatabase(context: Context): PassionWomanDatabase =
        Room.databaseBuilder(context, PassionWomanDatabase::class.java, DATABASE_NAME)
            .createFromAsset(DATABASE_FILENAME)
            .build()
            .also {
                database = it
            }

    fun provideDatabase(context: Context): PassionWomanDatabase =
        database ?: run {
            // TODO: save to preferences if database was initialised
            context.deleteDatabase(DATABASE_NAME)
            createDatabase(context)
        }

}
