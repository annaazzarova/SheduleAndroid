package com.example.anna.shedule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.anna.shedule.application.schedule.service.GroupService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.utils.ContextUtils;


public class MainActivityNew extends ActionBarActivity implements View.OnClickListener {
    Button btnStudent = null;
    Button btnTeacher = null;
    Button btnSteward = null;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_new);

        final ProgressDialog prog1 = new ProgressDialog(MainActivityNew.this);
        prog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog1.setMessage("wait please");
        prog1.setIndeterminate(true); // выдать значек ожидания
        prog1.setCancelable(true);
        prog1.show();

        ContextUtils.setContext(getApplicationContext());
        final GroupService groupService = Services.getService(GroupService.class);
        groupService.update(new GroupService.GroupListener() {
            @Override
            public void onSuccess() {
                prog1.cancel();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Проверьте соединение с интернетом!", Toast.LENGTH_LONG).show();
                finish();
            }
        });


        btnStudent = (Button) findViewById(R.id.btnStudent);
        btnTeacher = (Button) findViewById(R.id.btnTeacher);
        btnSteward = (Button) findViewById(R.id.btnSteward);

        btnStudent.setOnClickListener(this);
        btnSteward.setOnClickListener(this);
        btnTeacher.setOnClickListener(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStudent:
                intent = new Intent(MainActivityNew.this, com.example.anna.shedule.login.LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTeacher:
                intent = new Intent(MainActivityNew.this, com.example.anna.shedule.login.LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSteward:
                intent = new Intent(MainActivityNew.this, com.example.anna.shedule.login.LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
