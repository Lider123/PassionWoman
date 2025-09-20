package ru.babaetskv.passionwoman.data.database

import android.content.Context
import androidx.room.Room
import ru.babaetskv.passionwoman.domain.preferences.AppPreferences

object DatabaseProvider {
    private const val DATABASE_FILENAME = "demo_db_editor/passionwoman.db"
    private const val DATABASE_NAME = "passionwoman"

    private var database: PassionWomanDatabase? = null

    private fun createDatabase(context: Context, appPrefs: AppPreferences): PassionWomanDatabase =
        Room.databaseBuilder(context, PassionWomanDatabase::class.java, DATABASE_NAME)
            .also {
                if (!appPrefs.databaseInitialized) it.createFromAsset(DATABASE_FILENAME)
            }
            .build()
            .also {
                database = it
                appPrefs.databaseInitialized = true
            }

    fun provideDatabase(context: Context, appPrefs: AppPreferences): PassionWomanDatabase =
        database ?: run {
            createDatabase(context, appPrefs)
        }
}
