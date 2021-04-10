package com.ketee_jishs.nasa_app.ui.notes

interface LocalRepository {
    fun getAllNotes(): MutableList<NotesData>
    fun saveEntity(notesData: NotesData)
}