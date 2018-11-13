package com.lambdaschool.notetaker;

import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class NoteRepository {
//    private ArrayList<Note> notes;

/*    public NoteRepository() {
        this.notes = new ArrayList<>();

    }*/

    public MutableLiveData<ArrayList<Note>> getNotes() {
        MutableLiveData<ArrayList<Note>> liveDataList = new MutableLiveData<>();
        liveDataList.setValue(SharedPrefsDao.getAllNotes());
        return liveDataList;
    }

    public ArrayList<Note> addNote(Note note) {
        SharedPrefsDao.setNote(note);
        return SharedPrefsDao.getAllNotes();
    }
}
