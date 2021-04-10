package com.ketee_jishs.nasa_app.ui.notes

class LocalRepositoryImpl (private val localDataSource: NotesDao) : LocalRepository {
    override fun getAllNotes(): MutableList<NotesData> {
        return convertEntityToNotes(localDataSource.all())
    }

    override fun saveEntity(notesData: NotesData) {
        localDataSource.insert(convertNotesToEntity(notesData))
    }

    private fun convertEntityToNotes(entityList: List<NotesEntity>): MutableList<NotesData> {
        return entityList.map {
            NotesData(
                it.noteHeader,
                it.noteBody
            )
        } as MutableList<NotesData>
    }

    private fun convertNotesToEntity(notesData: NotesData): NotesEntity {
        return NotesEntity(
            0,
            notesData.noteHeader,
            notesData.noteBody
        )
    }
}