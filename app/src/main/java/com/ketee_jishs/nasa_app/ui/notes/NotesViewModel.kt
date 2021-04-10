package com.ketee_jishs.nasa_app.ui.notes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ketee_jishs.nasa_app.App.Companion.getNotesDao

class NotesViewModel(
    val notesLiveData: MutableLiveData<NotesAppState> = MutableLiveData(),
    private val notesRepository: LocalRepository = LocalRepositoryImpl(getNotesDao())
) : ViewModel() {

    fun getAllNotes() {
        notesLiveData.value = NotesAppState.Success(notesRepository.getAllNotes())
    }

    fun saveNoteToDB(notesData: NotesData) {
        notesRepository.saveEntity(notesData)
    }
}