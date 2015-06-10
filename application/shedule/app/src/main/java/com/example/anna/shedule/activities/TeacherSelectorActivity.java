package com.example.anna.shedule.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.anna.shedule.R;
import com.example.anna.shedule.TeacherListAdapter;
import com.example.anna.shedule.application.schedule.model.Teacher;
import com.example.anna.shedule.application.schedule.service.TeacherService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.utils.ContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class TeacherSelectorActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_selection);
        ContextUtils.setContext(getApplicationContext());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Set<Teacher> teacherSet = Services.getService(TeacherService.class).getAllTeachers();
        List<Teacher> teachers = new ArrayList<Teacher>(teacherSet);


        TeacherListAdapter adapter = new TeacherListAdapter(this, teachers);
        ListView lv = (ListView) findViewById(R.id.teacher_list);

        Context context = getApplicationContext();
        lv.addFooterView(new View(context), null, true);
        lv.addHeaderView(new View(context), null, true);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Teacher teacher = (Teacher) parent.getItemAtPosition(position);
                intent.putExtra("teacher", teacher);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
