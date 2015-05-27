package com.example.anna.shedule.service;

import android.app.IntentService;
import android.content.Intent;

import com.example.anna.shedule.service.receivers.AlarmReceiver;
import com.example.anna.shedule.utils.ContextUtils;

public class ScheduleIntentService extends IntentService {

    public ScheduleIntentService() {
        this("Schedule service");
    }

    public ScheduleIntentService(String name) {
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

