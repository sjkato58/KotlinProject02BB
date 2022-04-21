package com.katofuji.kotlinproject02bb.data.database

import androidx.room.TypeConverter
import org.json.JSONArray

class CharacterConverters {

    @TypeConverter
    fun stringListToJson(
        value: List<String>?
    ) = value?.let {
        JSONArray(it).toString()
    }

    @TypeConverter
    fun jsonToStringList(
        value: String?
    ) = value?.let {
        val stringList = mutableListOf<String>()
        val jsonArray = JSONArray(it)
        val size = jsonArray.length()
        for (i in 0 until size) {
            stringList.add(jsonArray.optString(i))
        }
        stringList
    }

    @TypeConverter
    fun integerListToJson(
        value: List<Int>?
    ) = value?.let {
        JSONArray(it).toString()
    }

    @TypeConverter
    fun jsonToIntegerList(
        value: String?
    ) = value?.let {
        val stringList = mutableListOf<Int>()
        val jsonArray = JSONArray(it)
        val size = jsonArray.length()
        for (i in 0 until size) {
            stringList.add(jsonArray.optInt(i))
        }
        stringList
    }
}