package com.example.mynotes;

import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

public class MainActivityViewModel extends ViewModel {
    public static final String ORIGINAL_COURSE_ID = "com.example.noteapp.ORIGINAL_COURSE_ID";
    public static final String ORIGINAL_COURSE_TITLE = "com.example.noteapp.ORIGINAL_COURSE_TITLE";
    public static final String ORIGINAL_COURSE_TEXT= "com.example.noteapp.ORIGINAL_COURSE_TEXT";
    public String mOriginalCourseid;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;
    public boolean misnewlyCeated = true;

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_COURSE_ID,mOriginalCourseid);
        outState.putString(ORIGINAL_COURSE_TITLE,mOriginalNoteTitle);
        outState.putString(ORIGINAL_COURSE_TEXT,mOriginalNoteText);

    }
    public void RestoreState(Bundle inState){
        mOriginalCourseid = inState.getString(ORIGINAL_COURSE_ID);
        mOriginalNoteTitle = inState.getString(ORIGINAL_COURSE_TITLE);
        mOriginalNoteText = inState.getString(ORIGINAL_COURSE_TEXT);
    }
}
