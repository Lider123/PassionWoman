package ru.babaetskv.passionwoman.data.prefs

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.*
import org.mockito.kotlin.*
import ru.babaetskv.passionwoman.data.MockProvider

import ru.babaetskv.passionwoman.data.preferences.PreferencesProviderImpl
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences

@RunWith(JUnit4::class)
class FavoritesPreferencesTest {
    private var prefsMap = HashMap<String, String>()
    private val stubSharedPreferencesEditor: SharedPreferences.Editor =
        MockProvider.provideSharedPreferencesEditorMock(prefsMap)
    private val stubSharedPreferences: SharedPreferences =
        MockProvider.provideSharedPreferencesMock(prefsMap, stubSharedPreferencesEditor)
    private val stubContext: Context = mock {
        on {
            applicationContext
        } doReturn this.mock
        on {
            getSharedPreferences(anyString(), anyInt())
        } doReturn stubSharedPreferences
    }
    private val prefs: FavoritesPreferences =
        PreferencesProviderImpl(stubContext).provideFavoritesPreferences()

    @Before
    fun beforeTest() {
        val initialValues = arrayOf("2", "4", "1")
        prefs.setFavoriteIds(*initialValues)
        assertArrayEquals(initialValues, prefs.getFavoriteIds().toTypedArray())
    }

    @Test
    fun set_SampleArray_isCorrect() {
        val ids = arrayOf("7", "1", "5")
        prefs.setFavoriteIds(*ids)
        assertArrayEquals(ids, prefs.getFavoriteIds().toTypedArray())
    }

    @Test
    fun put_SampleValueToEmpty_isCorrect() {
        prefs.reset()
        val newId = "7"
        prefs.putFavoriteId(newId)
        val expected = arrayOf(newId)
        assertArrayEquals(expected, prefs.getFavoriteIds().toTypedArray())
    }

    @Test
    fun put_SampleValue_isCorrect() {
        val newId = "7"
        prefs.putFavoriteId(newId)
        val expected = arrayOf("7", "2", "4", "1")
        assertArrayEquals(expected, prefs.getFavoriteIds().toTypedArray())
    }

    @Test
    fun reset_isCorrect() {
        prefs.reset()
        assertTrue(prefs.getFavoriteIds().isEmpty())
    }

    @Test
    fun isFavorite_containedValue_returnsTrue() {
        assertTrue(prefs.isFavorite("1"))
    }

    @Test
    fun isFavorite_notContainedValue_returnsFalse() {
        assertFalse(prefs.isFavorite("11"))
    }

    @Test
    fun deleteFavoriteId_containedElement_isCorrect() {
        prefs.deleteFavoriteId("4")
        val expected = arrayOf("2", "1")
        assertArrayEquals(expected, prefs.getFavoriteIds().toTypedArray())
    }

    @Test
    fun deleteFavoriteId_notContainedElement_isCorrect() {
        prefs.deleteFavoriteId("10")
        val expected = arrayOf("2", "4", "1")
        assertArrayEquals(expected, prefs.getFavoriteIds().toTypedArray())
    }
}
