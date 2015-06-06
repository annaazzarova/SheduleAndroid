package com.example.anna.shedule.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.shedule.R;

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
        }
        return super.onOptionsItemSelected(item);
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

    private void createNote(String note) {
        finish();
    }

}
