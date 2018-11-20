package com.lambdaschool.notetaker;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Equalizer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int LAYOUT_SPAN_COUNT = 2;
    public static SharedPreferences preferences;

    private Context context;
    private Activity activity;
    //    private LinearLayout listLayout;
    private NoteViewModel viewModel;

    private int currentTheme;

    private StaggeredGridLayoutManager layoutManager;
    private RecyclerView listView;
    public NoteListAdapter listAdapter;
    //List Adaptor


    public static final int EDIT_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        preferences = this.getPreferences(Context.MODE_PRIVATE);

//        notes = new ArrayList<>();
        context = this;
        activity = this;
//        listLayout = findViewById(R.id.list_layout);

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SettingsActivity.class);
                startActivity(intent);
            }
        });

        viewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        final Observer<ArrayList<Note>> observer = new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Note> notes) {
                if (notes != null) {
//                    refreshListView(notes);
                    listAdapter = new NoteListAdapter(notes, activity);
                    listView.setAdapter(listAdapter);

                }

            }
        };
        viewModel.getNotesList().observe(this, observer);


        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditActivity.class);
                Note newNote = new Note(Note.NO_ID);

                intent.putExtra(EditActivity.EDIT_NOTE_KEY, newNote);
                startActivityForResult(intent, EDIT_REQUEST_CODE);

                /*notes.add(System.currentTimeMillis());
                int noteIndex = notes.size() - 1;
                listLayout.addView(getDefaultTextView(notes.get(noteIndex).toString()));
                Log.i(getLocalClassName(), notes.toString());*/
            }
        });

        listView = findViewById(R.id.note_recycler_view);
        layoutManager = new StaggeredGridLayoutManager(LAYOUT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        //create our list adaptor and attach
    }

    @Override
    public void setTheme(int resid) {
        currentTheme = resid;
        super.setTheme(resid);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!ThemeUtils.checkTheme(activity, currentTheme)) {
            ThemeUtils.refreshActivity(activity);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private TextView getDefaultTextView(final Note note) {
        TextView textView = new TextView(context);
        textView.setText(note.getTitle());
        textView.setTextSize(24);
        textView.setPadding(10, 10, 10, 10);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra(EditActivity.EDIT_NOTE_KEY, note);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });

        return textView;
    }

    private void refreshListView(ArrayList<Note> notes) {
/*        listLayout.removeAllViews();
        for (Note note : notes) {
            listLayout.addView(getDefaultTextView(note));
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_REQUEST_CODE) {
                if (data != null) {
                    Note returnedNote = (Note) data.getSerializableExtra(EditActivity.EDIT_NOTE_KEY);

/*                    boolean foundNote = false;
                    for(int i = 0; i < notes.size(); ++i) {
                        if(notes.get(i).getId() == returnedNote.getId()) {
                            // this created a bug with an infinite loop, with each loop,
                            // an element is inserted into the beginning of the arraylist
//                            notes.add(i, returnedNote);
                            notes.set(i, returnedNote);
                            foundNote = true;
                        }
                    }
                    if(!foundNote) {
                        notes.add(returnedNote);
                    }
                    refreshListView();*/
                    viewModel.addNote(returnedNote);
                }
            }
        }
    }
}