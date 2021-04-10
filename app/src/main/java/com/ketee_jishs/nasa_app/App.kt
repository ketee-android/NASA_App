package com.ketee_jishs.nasa_app

import android.app.Application
import androidx.room.Room
import com.ketee_jishs.nasa_app.ui.notes.NotesDao
import com.ketee_jishs.nasa_app.ui.notes.NotesDataBase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private var db: NotesDataBase? = null
        private const val DB_NAME = "Notes.db"

        fun getNotesDao(): NotesDao {
            if (db == null) {
                synchronized(NotesDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            NotesDataBase::class.java,
                            DB_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return db!!.notesDao()
        }
    }
}