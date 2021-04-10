package com.ketee_jishs.nasa_app.ui.notes

sealed class NotesAppState {
    data class Success(val notesData: MutableList<NotesData>) : NotesAppState()
    data class Error(val error: Throwable) : NotesAppState()
}