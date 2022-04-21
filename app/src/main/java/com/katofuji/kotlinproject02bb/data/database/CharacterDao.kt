package com.katofuji.kotlinproject02bb.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katofuji.kotlinproject02bb.data.characterlist.datamodels.CharacterModel

@Dao
interface CharacterDao: BaseDao<CharacterModel> {

    @Query("SELECT * FROM charactermodels")
    fun getAllCharacters(): List<CharacterModel>

    @Query("SELECT * FROM charactermodels WHERE charid LIKE :characterId")
    fun getCharacterById(characterId: Int): CharacterModel?
}