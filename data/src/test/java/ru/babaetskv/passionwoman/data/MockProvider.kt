package ru.babaetskv.passionwoman.data

import android.content.SharedPreferences
import org.mockito.ArgumentMatchers.*
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

object MockProvider {

    fun provideSharedPreferencesEditorMock(
        prefsMap: MutableMap<String, String>
    ): SharedPreferences.Editor = mock {
        on {
            putString(anyString(), anyString())
        } doAnswer {
            val key = it.getArgument<String>(0)
            val value = it.getArgument<String>(1)
            prefsMap[key] = value
            this.mock
        }
    }

    fun provideSharedPreferencesMock(
        prefsMap: MutableMap<String, String>,
        editorMock: SharedPreferences.Editor
    ): SharedPreferences = mock {
        on {
            edit()
        } doReturn editorMock
        on {
            getString(anyString(), anyString())
        } doAnswer {
            val key = it.getArgument<String>(0)
            val defValue = it.getArgument<String>(1)
            prefsMap.getOrDefault(key, defValue)
        }
    }
}
