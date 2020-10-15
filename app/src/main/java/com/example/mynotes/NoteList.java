package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

public class NoteList extends AppCompatActivity {
    private RecyclerClass mNoteRecyclerClass;

    //private ArrayAdapter<NoteInfo> mAdapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(NoteList.this, MainActivity.class));
            }
        });
        initializeDisplayContent();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
       // mAdapterNotes.notifyDataSetChanged();
        mNoteRecyclerClass.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
      // final ListView listNotes = findViewById(R.id.listnotes);
      //  List notes = DataManager.getInstance().getNotes();
        //mAdapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        //listNotes.setAdapter(mAdapterNotes);

       // listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
          //  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //    Intent intent = new Intent(NoteList.this, MainActivity.class);
               // NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
              //  intent.putExtra(MainActivity.NOTE_POSITION, position);
                //startActivity(intent);
    //        }
      //  });
        final RecyclerView recyclernotes = (RecyclerView) findViewById(R.id.list_notes);
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        recyclernotes.setLayoutManager(notesLayoutManager);
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerClass = new RecyclerClass(this, notes);
        recyclernotes.setAdapter(mNoteRecyclerClass);
    }
}