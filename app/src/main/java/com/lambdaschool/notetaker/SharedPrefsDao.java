package com.lambdaschool.notetaker;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class SharedPrefsDao {

    private static final String KEY_IDS = "key_ids";
    private static final String KEY_ID_PREFIX = "key_id_";
    private static final String NEXT_KEY_ID = "key_next_id";

    private static String getIdsString() {
        String keyIds = "";
        if (MainActivity.preferences != null) {
            keyIds = MainActivity.preferences.getString(KEY_IDS, "");
        }
        return keyIds;
    }

    private static String[] getAllIds() {
        //keys are stored as CSV string
        final String[] ids = getIdsString().split(",");
        return ids;
    }

    public static ArrayList<Note> getAllNotes() {
        String[] ids = getAllIds();
        ArrayList<Note> notes = new ArrayList<>(ids.length);
        for (String id: ids) {
            if (!id.equals(""))
            notes.add(getNote(id));
        }
        return notes;
    }

    private static Note getNote(String id) {
        Note note = null;
        if (MainActivity.preferences != null) {
            final String noteString = MainActivity.preferences.getString(KEY_ID_PREFIX + id, "");
            note = new Note(noteString);
        }
        return note;
    }

    private static int getNextId() {
        int currentId = 0;
        if (MainActivity.preferences != null) {
            currentId = MainActivity.preferences.getInt(NEXT_KEY_ID, 0);
            int nextId = currentId + 1;
            SharedPreferences.Editor editor = MainActivity.preferences.edit();
            editor.putInt(NEXT_KEY_ID, nextId);
            editor.apply();
        }
        return currentId;
    }


    public static void setNote(Note note) {
        if (note.getId() == Note.NO_ID) {
            note.setId(getNextId());
        }
        String[] ids = getAllIds();
        boolean exists = false;
        for (String id : ids) {
            if (!id.equals("")) {
                if (note.getId() == Integer.parseInt(id)) {
                    exists = true;
                    break;
                }
            }
        }

        if (!exists) {
            addId(note.getId());
        }
        addNote(note);
    }

    private static void addNote(Note note) {
        SharedPreferences.Editor editor = MainActivity.preferences.edit();
        editor.putString(KEY_ID_PREFIX + note.getId(),note.toCSVString());
        editor.apply();
    }

    private static void addId(int id) {
        String idsString = getIdsString();
        idsString = idsString + "," + id;
        SharedPreferences.Editor editor = MainActivity.preferences.edit();
        editor.putString(KEY_IDS, idsString);
        editor.apply();
    }

}
