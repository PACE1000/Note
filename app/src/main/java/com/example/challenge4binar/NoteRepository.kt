package com.example.challenge4binar

import android.util.Log
import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun update(note:Note){
        noteDao.update(note)
    }

    suspend fun delete (note:Note){
        noteDao.delete(note)
    }

    fun checkWorking(){
        Log.d("NoteViewModel_Check", "checkWorking: ")
    }
}