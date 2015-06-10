package com.example.anna.shedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.anna.shedule.activities.menu.BaseActivity;
import com.example.anna.shedule.application.login.service.LoginService;
import com.example.anna.shedule.application.settings.SettingsService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.utils.ContextUtils;


public class SettingsActivity extends BaseActivity {

    private LoginService loginService;
    private SettingsService settingsService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        ContextUtils.setContext(getApplicationContext());

        loginService = Services.getService(LoginService.class);
        settingsService = Services.getService(SettingsService.class);

        initButtons();
    }

    private void initButtons() {
        final View pushButton = findViewById(R.id.push_button);
        final CheckBox pushCheckbox = (CheckBox) findViewById(R.id.push_checkbox);
        pushCheckbox.setChecked(settingsService.getValueAsBool("push"));

        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean value = pushCheckbox.isChecked();
                pushCheckbox.setChecked(!value);
                settingsService.setValue("push", !value);
            }
        });

        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginService.logout();
                openStartPage();
            }
        });
    }

    private void openStartPage() {
        Intent intent = new Intent(this, StartPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
