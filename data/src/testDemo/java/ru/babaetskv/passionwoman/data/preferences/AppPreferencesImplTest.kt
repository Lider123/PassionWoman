package ru.babaetskv.passionwoman.data.preferences

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AppPreferencesImplTest {
    private val prefs = AppPreferencesImpl()

    @Test
    fun onboardingShowed_returnsFalse_whenInitialized() {
        val result = prefs.onboardingShowed

        assertFalse(result)
    }

    @Test
    fun onboardingShowed_returnsTrue_whenSetTrue() {
        prefs.onboardingShowed = true

        val result = prefs.onboardingShowed

        assertTrue(result)
    }
}
