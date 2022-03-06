package ru.babaetskv.passionwoman.data.utils

import org.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class JsonUtilsTest {
    private val jsonMock1 = JSONObject(mapOf("field" to "value1"))
    private val jsonMock2 = JSONObject(mapOf("field" to "value2"))

    @Test
    fun toJsonArray_sizeTheSame_whenConverted() {
        val data = listOf(jsonMock1, jsonMock2)

        val actual = data.toJsonArray()
        Assert.assertEquals(data.size, actual.length())
    }

    @Test
    fun toJsonArray_itemsTheSame_whenConverted() {
        val data = listOf(jsonMock1, jsonMock2)

        val actual = data.toJsonArray()
        data.forEachIndexed { index, jsonObject ->
            Assert.assertEquals(jsonObject, actual.get(index))
        }
    }
}
