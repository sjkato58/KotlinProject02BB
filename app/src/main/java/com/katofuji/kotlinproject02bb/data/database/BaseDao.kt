package com.katofuji.kotlinproject02bb.data.database

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: T): Long

    @Update
    fun update(item: T)

    @Delete
    fun delete(item: T)

    @Transaction
    fun upsert(item: T) {
        if (insert(item) == -1L) update(item)
    }
}