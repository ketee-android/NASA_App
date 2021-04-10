package com.ketee_jishs.nasa_app.ui.notes

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotesEntity::class], version = 1, exportSchema = true)
abstract class NotesDataBase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}