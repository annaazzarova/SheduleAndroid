package com.example.anna.shedule;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;


public class GroupSelectionActivity extends ActionBarActivity {

    String[] faculty = {"ФИиВТ", "ЛПФ", "ММФ"};
    String[] specialty = {"ПС", "ИВТ", "БИ", "ТЛДПп", "СМ", "АИ", "МС", "МТМ"};
    String[] course = {"I", "II", "III", "IV", "V"};
    String[] groups = {"11", "12", "21", "22", "31", "32", "41", "42", "51", "52"};
    Map<Integer, String[]> container = new HashMap<Integer, String[]>();
    //  Map<Integer, Spinner> spinners = new HashMap<Integer, Spinner>();
    Map<Integer, ArrayAdapter<String>> adapters = new HashMap<Integer, ArrayAdapter<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_selection);

        container.put(0, copyPartArray(specialty, 0, 3));
        container.put(1, copyPartArray(specialty, 4, 2));
        container.put(2, copyPartArray(specialty, 6, 2));
        /*
        #####################################
         */
        // адаптер
        initializeAdapter(R.id.facultySpiner, faculty, adapters.get(0));
    }

    private String[] copyPartArray(String[] a, int start, int len) {
        if (a == null)
            return null;
        if (start > a.length)
            return null;
        if (len > a.length)
            return null;
        String[] r = new String[len];
        System.arraycopy(a, start, r, 0, len);
        return r;
    }

    private void initializeAdapter(int id, String[] array, ArrayAdapter<String> adapter) {

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = (Spinner) findViewById(id);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String item = spinner.getSelectedItem().toString();
                int idSp = spinner.getId();
                //Поэтапная инидиализация спиннеров и соответствующих адаптеров
                if (idSp == R.id.facultySpiner) {
                    initializeAdapter(R.id.specialtySpiner, container.get(position), adapters.get(1));
                } else if (idSp == R.id.specialtySpiner) {
                    initializeAdapter(R.id.courseSpiner, course, adapters.get(2));
                } else if (idSp == R.id.courseSpiner) {
                    initializeAdapter(R.id.groupSpiner, groups, adapters.get(3));
                } else if (idSp == R.id.groupSpiner) {
                    // Toast.makeText(getApplicationContext(), "Ваш выбор: " + item, Toast.LENGTH_SHORT).show();
                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //spinners.put(id, spinner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
