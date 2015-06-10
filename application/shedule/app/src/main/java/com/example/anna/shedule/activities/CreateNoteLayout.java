package com.example.anna.shedule.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.shedule.R;
import com.example.anna.shedule.application.note.service.NoteService;
import com.example.anna.shedule.application.services.Services;

public class CreateNoteLayout extends AppCompatActivity {

    private TextView noteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note_layout);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        noteText = (TextView) findViewById(R.id.note_text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                createNote();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void finish() {
        hideSoftKeyboard();
        super.finish();
    }

    public void createNote() {
        String note = noteText.getText().toString();
        if (note.isEmpty()) {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_note_hint,
                    Toast.LENGTH_SHORT).show();
        } else {
            createNote(note);
        }
    }

    private void createNote(String text) {
        Bundle extra = getIntent().getExtras();
        String lessonId = extra.getString("lessonId");
        String changeId = extra.getString("changeId");
        long startOfDay = extra.getLong("startOfDay");

        final ProgressDialog prog = createProgressDialog();

        NoteService.CreateNote note = new NoteService.CreateNote(text, lessonId, changeId, startOfDay);

        NoteService noteService = Services.getService(NoteService.class);
        noteService.createNote(text, note, new NoteService.NoteCreateListener() {
            @Override
            public void onSuccess() {
                prog.dismiss();
                finish();
            }

            @Override
            public void onError() {
                prog.dismiss();
                Toast
                    .makeText(getApplicationContext(), R.string.error_create_note, Toast.LENGTH_SHORT)
                    .show();
            }
        });
    }

    private ProgressDialog createProgressDialog() {
        final ProgressDialog prog = new ProgressDialog(CreateNoteLayout.this);
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setMessage(getString(R.string.pleas_wait));
        prog.setIndeterminate(true);
        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);
        prog.show();
        return prog;
    }

}
