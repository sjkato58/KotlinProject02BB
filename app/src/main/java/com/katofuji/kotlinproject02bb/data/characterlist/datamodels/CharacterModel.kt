package com.katofuji.kotlinproject02bb.data.characterlist.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "charactermodels")
data class CharacterModel(
    @PrimaryKey
    var charid : Int = -1,
    @ColumnInfo(name = "name")
    var name : String = "",
    @ColumnInfo(name = "birthday")
    var birthday : String = "",
    @ColumnInfo(name = "occupation")
    var occupation : List<String>? = null,
    @ColumnInfo(name = "imageurl")
    var img : String = "",
    @ColumnInfo(name = "status")
    var status : String = "",
    @ColumnInfo(name = "nickname")
    var nickname : String = "",
    @ColumnInfo(name = "appearance")
    var appearance : List<Int>? = null,
    @ColumnInfo(name = "portrayed")
    var portrayed : String = "",
    @ColumnInfo(name = "category")
    var category : String = "",
    @ColumnInfo(name = "bettercallsaulappearance")
    var bettercallsaulappearance : List<Int>? = null
)
