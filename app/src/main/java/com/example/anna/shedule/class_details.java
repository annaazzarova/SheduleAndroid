package com.example.anna.shedule;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class class_details extends ActionBarActivity {

    Spinner teacher_spinner;
    Spinner housing_spinner;
    Spinner time_spinner;
    Spinner type_spinner;
    Spinner status_spinner;

    EditText title_edit;
    EditText audience_edit;

    MenuItem save_button;
    MenuItem edit_button;

    Button add_mark_button;

    String[] teachers;
    String[] housings;
    String[] times;
    String[] types;
    String[] statuses;
    String title;
    String audience;

    Menu currentMenu;

    Drawable spinner_default_background;

    public void sendMessage(View view)
    {
        Intent intent = new Intent(class_details.this, teacher_list.class);
        startActivity(intent);
    }

    public void findDataInputs() {
        teacher_spinner = (Spinner) findViewById(R.id.teacher_input);
        housing_spinner = (Spinner) findViewById(R.id.housing_input);
        time_spinner = (Spinner) findViewById(R.id.time_input);
        type_spinner = (Spinner) findViewById(R.id.type_input);
        status_spinner = (Spinner) findViewById(R.id.status_input);

        title_edit = (EditText) findViewById(R.id.title_input);
        audience_edit = (EditText) findViewById(R.id.audience_input);

        add_mark_button = (Button) findViewById(R.id.add_mark);

        //spinnerBackground = teacher_spinner.getBackground();

        spinner_default_background = teacher_spinner.getBackground();
    }

    public void findMenuItens() {
        save_button = currentMenu.findItem(R.id.action_save);
        edit_button = currentMenu.findItem(R.id.action_edit);
    }

    public void setDefaultViewsData(String[] teachers, String[] housings,
                                    String[] times, String[] types, String[] statuses) {
        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teachers);
        teacher_spinner.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, housings);
        housing_spinner.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, times);
        time_spinner.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        type_spinner.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statuses);
        status_spinner.setAdapter(adapter);
    }

    public void setLessonDetails(String title, String audience) {
        title_edit.setText(title);
        audience_edit.setText(audience);

        // teacher_spinner.text
    }

    public void onLessonCreateCreate() {
        save_button.setVisible(true);
        edit_button.setVisible(false);
        add_mark_button.setVisibility(View.GONE);
        setTitle(getResources().getString(R.string.lesson_create_page_title));

        title_edit.setEnabled(true);
        audience_edit.setEnabled(true);

        teacher_spinner.setClickable(true);
        housing_spinner.setClickable(true);
        time_spinner.setClickable(true);
        type_spinner.setClickable(true);
        status_spinner.setClickable(true);

        if(Build.VERSION.SDK_INT >= 16)
        {
            teacher_spinner.setBackground(spinner_default_background);
            housing_spinner.setBackground(spinner_default_background);
            time_spinner.setBackground(spinner_default_background);
            type_spinner.setBackground(spinner_default_background);
            status_spinner.setBackground(spinner_default_background);
        }
        else
        {
            teacher_spinner.setBackgroundDrawable(spinner_default_background);
            housing_spinner.setBackgroundDrawable(spinner_default_background);
            time_spinner.setBackgroundDrawable(spinner_default_background);
            type_spinner.setBackgroundDrawable(spinner_default_background);
            status_spinner.setBackgroundDrawable(spinner_default_background);
        }
    }

    public void onLessonEditCreate() {
        save_button.setVisible(true);
        edit_button.setVisible(false);
        add_mark_button.setVisibility(View.GONE);
        setTitle(getResources().getString(R.string.lesson_edit_page_title));

        title_edit.setEnabled(true);
        audience_edit.setEnabled(true);

        teacher_spinner.setClickable(true);
        housing_spinner.setClickable(true);
        time_spinner.setClickable(true);
        type_spinner.setClickable(true);
        status_spinner.setClickable(true);

        if(Build.VERSION.SDK_INT >= 16)
        {
            teacher_spinner.setBackground(spinner_default_background);
            housing_spinner.setBackground(spinner_default_background);
            time_spinner.setBackground(spinner_default_background);
            type_spinner.setBackground(spinner_default_background);
            status_spinner.setBackground(spinner_default_background);
        }
        else
        {
            teacher_spinner.setBackgroundDrawable(spinner_default_background);
            housing_spinner.setBackgroundDrawable(spinner_default_background);
            time_spinner.setBackgroundDrawable(spinner_default_background);
            type_spinner.setBackgroundDrawable(spinner_default_background);
            status_spinner.setBackgroundDrawable(spinner_default_background);
        }
    }

    public void onLessonShowCreate() {
        save_button.setVisible(false);
        edit_button.setVisible(true);
        add_mark_button.setVisibility(View.VISIBLE);
        setTitle(getResources().getString(R.string.lesson_details_page_title));

        title_edit.setEnabled(false);
        title_edit.setText("Длинное Имя Преподавателя");
        title_edit.setTextColor(Color.parseColor("#000000"));

        audience_edit.setEnabled(false);
        audience_edit.setText("123б");
        audience_edit.setTextColor(Color.parseColor("#000000"));

        teacher_spinner.setBackgroundColor(Color.TRANSPARENT);
        teacher_spinner.setClickable(false);
        housing_spinner.setBackgroundColor(Color.TRANSPARENT);
        housing_spinner.setClickable(false);
        time_spinner.setBackgroundColor(Color.TRANSPARENT);
        time_spinner.setClickable(false);
        type_spinner.setBackgroundColor(Color.TRANSPARENT);
        type_spinner.setClickable(false);
        status_spinner.setBackgroundColor(Color.TRANSPARENT);
        status_spinner.setClickable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Resources res = getResources();

        title = "";
        audience = "";

        teachers = new String[] {
                "Teacher1", "Teacher2", "Teacher3", "Teacher4", "Teacher5", "Teacher6"
        };

        housings = new String[] {
                res.getString(R.string.lesson_housing_first), res.getString(R.string.lesson_housing_second),
                res.getString(R.string.lesson_housing_third), res.getString(R.string.lesson_housing_fourth),
                res.getString(R.string.lesson_housing_fifth)
        };

        times = new String[] {
                res.getString(R.string.lesson_time_first), res.getString(R.string.lesson_time_second),
                res.getString(R.string.lesson_time_third), res.getString(R.string.lesson_time_fourth),
                res.getString(R.string.lesson_time_fifth), res.getString(R.string.lesson_time_sixth),
                res.getString(R.string.lesson_time_seventh)
        };

        types = new String[] {
                res.getString(R.string.lesson_type_practice), res.getString(R.string.lesson_type_lection)
        };

        statuses = new String[] {
                res.getString(R.string.lesson_status_normal), res.getString(R.string.lesson_status_cancelled)
        };

        findDataInputs();
        setDefaultViewsData(teachers, housings, times, types, statuses);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        currentMenu = menu;
        findMenuItens();
        onLessonShowCreate();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            onLessonEditCreate();
            return true;
        }

        if (id == R.id.action_save) {
            onLessonShowCreate();
            return true;
        }

        if (id == R.id.teacher_input) {
            onLessonEditCreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
