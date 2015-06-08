package com.example.anna.shedule.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.note.service.NoteService;
import com.example.anna.shedule.application.schedule.service.GroupService;
import com.example.anna.shedule.application.schedule.service.ScheduleService;
import com.example.anna.shedule.application.services.Services;
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

    public static void enable() {
        Context context = ContextUtils.getContext();
        new AlarmReceiver().setAlarm(context);
    }

    public static void disable() {
        Context context = ContextUtils.getContext();
        new AlarmReceiver().cancelAlarm(context);
    }

    private void synchronize() {
        Services.getService(GroupService.class).update();
        Services.getService(NoteService.class).update();
        Services.getService(ScheduleService.class).update();
        Services.clear();
    }
}
