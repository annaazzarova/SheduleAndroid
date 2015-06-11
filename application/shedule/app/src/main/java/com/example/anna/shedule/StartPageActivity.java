package com.example.anna.shedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.anna.shedule.application.schedule.service.GroupService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.login.GroupSelectionActivity;
import com.example.anna.shedule.login.LoginActivity;
import com.example.anna.shedule.server.Server;
import com.example.anna.shedule.utils.ContextUtils;


public class StartPageActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_new);
        ContextUtils.setContext(getApplicationContext());

        boolean isLogin = Services.getService(UserService.class).isLogin();
        if (isLogin) {
            startScheduleActivity();
            return;
        }


        boolean groupExists = Services.getService(GroupService.class).getGroups().size() > 0;

        if (!groupExists) {
            loadGroups();
        }

        Button btnStudent = (Button) findViewById(R.id.btnStudent);
        Button btnTeacher = (Button) findViewById(R.id.btnTeacher);
        Button btnSteward = (Button) findViewById(R.id.btnSteward);

        btnStudent.setOnClickListener(this);
        btnSteward.setOnClickListener(this);
        btnTeacher.setOnClickListener(this);
    }

    private void startScheduleActivity() {
        Intent intent = new Intent(StartPageActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loadGroups() {
        final ProgressDialog prog = new ProgressDialog(StartPageActivity.this);
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.setMessage(getString(R.string.pleas_wait));
        prog.setIndeterminate(true); // выдать значек ожидания
        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);
        prog.show();

        ContextUtils.setContext(getApplicationContext());
        final GroupService groupService = Services.getService(GroupService.class);
        groupService.update(new GroupService.GroupListener() {
            @Override
            public void onSuccess() {
                prog.dismiss();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Проверьте соединение с интернетом!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnStudent:
                intent = new Intent(StartPageActivity.this, GroupSelectionActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTeacher:
                intent = new Intent(StartPageActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSteward:
                intent = new Intent(StartPageActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
