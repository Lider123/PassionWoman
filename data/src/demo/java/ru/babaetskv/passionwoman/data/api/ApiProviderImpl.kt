package ru.babaetskv.passionwoman.data.api

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import ru.babaetskv.passionwoman.data.database.PassionWomanDatabase
import ru.babaetskv.passionwoman.domain.DateTimeConverter

class ApiProviderImpl(
    private val context: Context,
    private val moshi: Moshi,
    private val dateTimeConverter: DateTimeConverter
) : ApiProvider {
    private val database = Room.databaseBuilder(context, PassionWomanDatabase::class.java, "passionwoman")
        .createFromAsset(DATABASE_FILENAME)
        .build()

    override fun provideAuthApi(): AuthApi = AuthApiImpl(context, database, moshi, dateTimeConverter)

    override fun provideCommonApi(): CommonApi = CommonApiImpl(context, database, moshi)

    companion object {
        private const val DATABASE_FILENAME = "passionwoman.db"
    }
}
