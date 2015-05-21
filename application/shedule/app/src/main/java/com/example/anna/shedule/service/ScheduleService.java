package com.example.anna.shedule.service;

import android.app.IntentService;
import android.content.Intent;

import com.example.anna.shedule.R;
import com.example.anna.shedule.service.receivers.AlarmReceiver;

public class ScheduleService extends IntentService {

    public ScheduleService() {
        this("Schedule service");
    }

    public ScheduleService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        android.os.Debug.waitForDebugger();
        AlarmReceiver.completeWakefulIntent(intent);
    }
}

