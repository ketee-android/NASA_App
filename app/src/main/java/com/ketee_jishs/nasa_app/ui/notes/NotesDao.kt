package com.ketee_jishs.nasa_app.ui.notes

import androidx.room.*

@Dao
interface NotesDao {
    @Query("SELECT * FROM NotesEntity")
    fun all(): List<NotesEntity>

    @Query("SELECT * FROM NotesEntity WHERE noteHeader LIKE :noteHeader")
    fun getDataByWord(noteHeader: String): List<NotesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: NotesEntity)

    @Update
    fun update(entity: NotesEntity)

    @Delete
    fun delete(entity: NotesEntity)
}