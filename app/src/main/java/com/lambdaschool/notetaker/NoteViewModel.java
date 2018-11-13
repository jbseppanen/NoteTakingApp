package com.lambdaschool.notetaker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

public class NoteViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Note>> noteList;
    private NoteRepository repo;

    public LiveData<ArrayList<Note>> getNotesList() {
        if (noteList == null) {
            loadList();

        }
        return noteList;
    }


    private void loadList() {
        repo = new NoteRepository();
        noteList = repo.getNotes();
    }

    public void addNote(Note note) {
        if(noteList != null) {
            noteList.setValue(repo.addNote(note));
        }
    }
}
