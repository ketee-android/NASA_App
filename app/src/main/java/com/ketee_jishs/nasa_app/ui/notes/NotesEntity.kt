package com.ketee_jishs.nasa_app.ui.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NotesEntity(
    @PrimaryKey (autoGenerate = true)
    var id: Long,
    var noteHeader: String,
    var noteBody: String
)