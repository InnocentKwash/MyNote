package com.example.mynotes;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final  String NOTE_POSITION = "com.example.noteapp.note position";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsnewnote;
    private EditText mNoteTitle;
    private EditText mNoteBody;
    private Spinner mCourseslist;
    private int mNotePosition;
    private boolean mIsCancelling;
    private MainActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel =viewModelProvider.get(MainActivityViewModel.class);
        if(mViewModel.misnewlyCeated && savedInstanceState != null)
            mViewModel.RestoreState(savedInstanceState);
        mViewModel.misnewlyCeated = false;

        mCourseslist = findViewById(R.id.CoursesSpinner);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        ArrayAdapter<CourseInfo> adaptercourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adaptercourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourseslist.setAdapter(adaptercourses);
        readDisplayStateValues();
        saveOriginalNoteValues();

        mNoteTitle = findViewById(R.id.NoteTitle);
        mNoteBody = findViewById(R.id.NoteBody);
        if(!mIsnewnote)
            displayNote(mCourseslist, mNoteTitle, mNoteBody);
    }

    private void saveOriginalNoteValues() {
        if(mIsnewnote){
            return;
        }
        mViewModel.mOriginalCourseid = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();
        mViewModel.mOriginalNoteText = mNote.getText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){
            if(mIsnewnote) {
                DataManager.getInstance().removeNote(mNotePosition);
            }
            else{
                storePreviousNoteValues();
            }
        }else {
            saveNote();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null){
            mViewModel.saveState(outState);
        }
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.mOriginalCourseid);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteTitle);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mCourseslist.getSelectedItem());
        mNote.setTitle(mNoteTitle.getText().toString());
        mNote.setText(mNoteBody.getText().toString());
    }

    private void displayNote(Spinner courseslist, EditText noteTitle, EditText noteBody) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseindex = courses.indexOf(mNote.getCourse());
        courseslist.setSelection(courseindex);
        noteTitle.setText(mNote.getTitle());
        noteBody.setText(mNote.getText());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsnewnote = position == POSITION_NOT_SET;
        if(mIsnewnote){
            createNewNote();

        }else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Sendmail) {
            sendEmail();
            return true;
        }
        else if(id == R.id.action_Cancel){
            mIsCancelling = true;
            finish();
        }
        else if(id == R.id.action_Next){
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_Next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size() - 1;
        item.setEnabled(mNotePosition < lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {
        saveNote();
        ++mNotePosition;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
        saveOriginalNoteValues();

        displayNote(mCourseslist, mNoteTitle, mNoteBody);

        invalidateOptionsMenu();
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) mCourseslist.getSelectedItem();
        String subject = mNoteTitle.getText().toString();
        String text = "this is it\"" + course.getTitle() + "\"\n" + mNote.getText();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("rfc/2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}