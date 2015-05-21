package com.example.anna.shedule.service;

import android.app.IntentService;
import android.content.Intent;

import com.example.anna.shedule.service.receivers.AlarmReceiver;
import com.example.anna.shedule.utils.ContextUtils;

public class ScheduleService extends IntentService {

    public ScheduleService() {
        this("Schedule service");
    }

    public ScheduleService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ContextUtils.setContext(getApplicationContext());
        android.os.Debug.waitForDebugger();
        synchronize();
        AlarmReceiver.completeWakefulIntent(intent);
    }

    private void synchronize() {

    }
}

