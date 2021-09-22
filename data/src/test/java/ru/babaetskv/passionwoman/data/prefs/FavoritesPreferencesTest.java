package ru.babaetskv.passionwoman.data.prefs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.babaetskv.passionwoman.data.preferences.FavoritesPreferencesImpl;
import ru.babaetskv.passionwoman.domain.preferences.FavoritesPreferences;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class FavoritesPreferencesTest {
    private final FavoritesPreferences prefs = new FavoritesPreferencesImpl();

    @Before
    public void beforeTest() {
        String[] initialValues = {"2", "4", "1"};
        prefs.setFavoriteIds(initialValues);
        assertArrayEquals(initialValues, prefs.getFavoriteIds().toArray());
    }

    @Test
    public void set_SampleArray_isCorrect() {
        String[] ids = {"7", "1", "5"};
        prefs.setFavoriteIds(ids);
        assertArrayEquals(ids, prefs.getFavoriteIds().toArray());
    }

    @Test
    public void put_SampleValue_isCorrect() {
        String newId = "7";
        prefs.putFavoriteId(newId);
        String[] expected = {"7", "2", "4", "1"};
        assertArrayEquals(expected, prefs.getFavoriteIds().toArray());
    }

    @Test
    public void clear_isCorrect() {
        prefs.clear();
        assertTrue(prefs.getFavoriteIds().isEmpty());
    }

    @Test
    public void isFavorite_containedValue_returnsTrue() {
        assertTrue(prefs.isFavorite("1"));
    }

    @Test
    public void isFavorite_notContainedValue_returnsFalse() {
        assertFalse(prefs.isFavorite("11"));
    }

    @Test
    public void deleteFavoriteId_containedElement_isCorrect() {
        prefs.deleteFavoriteId("4");
        String[] expected = {"2", "1"};
        assertArrayEquals(expected, prefs.getFavoriteIds().toArray());
    }

    @Test
    public void deleteFavoriteId_notContainedElement_isCorrect() {
        prefs.deleteFavoriteId("10");
        String[] expected ={"2", "4", "1"};
        assertArrayEquals(expected, prefs.getFavoriteIds().toArray());
    }
}
