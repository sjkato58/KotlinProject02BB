package com.katofuji.kotlinproject02bb.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel

@Database(entities = [CharacterModel::class], version = 1, exportSchema = false)
@TypeConverters(CharacterConverters::class)
abstract class CharacterDataBase: RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao
}