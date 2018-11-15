package com.lambdaschool.notetaker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
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
    private NoteViewModel viewModel;

    private StaggeredGridLayoutManager layoutManager;
    private RecyclerView listView;
    public NoteListAdapter listAdapter;


    public static final int EDIT_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = this.getPreferences(Context.MODE_PRIVATE);

        context = this;
        activity = this;

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
            }
        });

        listView = findViewById(R.id.note_recycler_view);
        layoutManager = new StaggeredGridLayoutManager(LAYOUT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_REQUEST_CODE) {
                if (data != null) {
                    Note returnedNote = (Note) data.getSerializableExtra(EditActivity.EDIT_NOTE_KEY);
                    viewModel.addNote(returnedNote);
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    String channelId = getPackageName() + ".new_note";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = "New Note Channel";
                        String description = "New note created!";
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                        channel.setDescription(description);

                        notificationManager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                            .setPriority(NotificationManager.IMPORTANCE_HIGH)
                            .setContentTitle(returnedNote.getTitle())
                            .setContentText(returnedNote.getContent())
                            .setColor(context.getResources().getColor(R.color.colorPrimary))
                            .setSmallIcon(android.R.drawable.ic_dialog_alert);
                    notificationManager.notify(1, builder.build());


                }
            }
        }
    }
}