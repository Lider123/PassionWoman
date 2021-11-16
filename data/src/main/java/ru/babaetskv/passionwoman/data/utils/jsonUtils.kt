package ru.babaetskv.passionwoman.data.utils

import org.json.JSONArray
import org.json.JSONObject

fun Collection<JSONObject>.toJsonArray(): JSONArray = JSONArray().also { array ->
    forEach {
        array.put(it)
    }
}
