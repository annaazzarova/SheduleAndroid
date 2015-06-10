package com.example.anna.shedule.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.anna.shedule.MainActivity;
import com.example.anna.shedule.R;
import com.example.anna.shedule.application.database.Database;
import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.note.service.NoteService;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.helper.LessonStatus;
import com.example.anna.shedule.application.schedule.model.helper.StaticLesson;
import com.example.anna.shedule.application.schedule.service.GroupService;
import com.example.anna.shedule.application.schedule.service.ScheduleService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.service.receivers.AlarmReceiver;
import com.example.anna.shedule.utils.ContextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class ScheduleIntentService extends IntentService {

    public static final int NOTE_NOTIFY = 1;
    public static final int CHANGE_NOTIFY = 2;
    NotificationManager mNotificationManager;

    public ScheduleIntentService() {
        this("Schedule service");
    }

    public ScheduleIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("SERVICE", "Start sync");
        ContextUtils.setContext(getApplicationContext());
        synchronize();
        AlarmReceiver.completeWakefulIntent(intent);
        Log.e("SERVICE", "Done sync");
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
        DataState stateBefore = getDataState();
        updateAllServices();
        DataState stateAfter = getDataState();
        notifyAboutChanges(stateBefore, stateAfter);
    }

    private void notifyAboutChanges(DataState stateBefore, DataState stateAfter) {
        List<String> newNoteIds = getNoteChanges(stateBefore, stateAfter);
        List<Change> updatedChanges = getChangedChanges(stateBefore, stateAfter);

        if (newNoteIds.size() > 0) {
            Note note = getNoteById(newNoteIds.get(0));
            showNewNotesNotify(note);
        }

        if (updatedChanges.size() > 0) {
            Change change = updatedChanges.get(0);
            showLessonChangesNotify(change);
        }
    }

    private Note getNoteById(String id) {
        String query = "SELECT * FROM " + Note.TABLE_NAME + " WHERE noteId = '" + id + "'";
        return getDbInstance().getOneByQuery(Note.class, query);
    }

    private void showNewNotesNotify(Note note) {
        String changeTitle = getString(R.string.note_notify_title);
        sendNotification(note.getText(), changeTitle, CHANGE_NOTIFY, MainActivity.class);
    }

    private void showLessonChangesNotify(Change change) {
        String noteTitle = getString(R.string.change_notify_title);
        String lessonTitle = getTitle(change);
        sendNotification(lessonTitle, noteTitle, NOTE_NOTIFY, MainActivity.class);
    }

    private String getTitle(Change change) {
        String title = "";
        if (change.getTitle() != null) {
            title = change.getTitle();
        } else if (change.getLessonId() != null) {
            String query = "SELECT * FROM " + StaticLesson.TABLE_NAME + " WHERE lessonId='" + change.getLessonId() +"'";
            StaticLesson lesson = getDbInstance().getOneByQuery(StaticLesson.class, query);
            title = lesson.getTitle();
        }
        return title;
    }

    private void sendNotification(String msg, String title, int notifyId, Class<?> activity) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, activity), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_notification_icon)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    private List<Change> getChangedChanges(DataState stateBefore, DataState stateAfter) {
        Map<String, Change> oldChanges = stateBefore.getChanges();
        Map<String, Change> newChanges = stateAfter.getChanges();
        List<String> allChangesAsIds = concatenate(oldChanges, newChanges);

        List<Change> allChanges = new ArrayList<>(allChangesAsIds.size());
        for (String id: allChangesAsIds) {
            Change change = newChanges.get(id);
            if (change == null) {
                change = oldChanges.get(id);
            }

            if (change != null) {
                allChanges.add(change);
            }
        }

        return allChanges;
    }

    private List<String> concatenate(Map<String, Change> oldChanges, Map<String, Change> newChanges) {
//        List<String> removedChanges = getRemovedChanges(oldChanges, newChanges);
        List<String> addedChanges = getAddedChanges(oldChanges, newChanges);
        List<String> changedChanges = getUpdatedChanges(oldChanges, newChanges);

        List<String> allChanges = new ArrayList<>();
        allChanges.addAll(addedChanges);
        allChanges.addAll(changedChanges);
//        allChanges.addAll(removedChanges);
        return allChanges;
    }

    private List<String> getUpdatedChanges(Map<String, Change> oldChanges, Map<String, Change> newChanges) {
        List<String> updated = new ArrayList<>();
        for (String change: oldChanges.keySet()) {
            Change other = newChanges.get(change);
            if (other != null && !other.equals(oldChanges.get(change))) {
                Change ch1 = (oldChanges.get(change));
                if (!isEquals(ch1, other)) {
                    updated.add(change);
                }
            }
        }
        return updated;
    }

    private boolean isEquals(Change ch1, Change ch2) {
        return isEquals(ch1.getTitle(), ch2.getTitle()) && isEquals(ch1.getAuditory(), ch2.getAuditory())
                && isEquals(ch1.getHull(), ch1.getHull()) && isEquals(ch1.getStatus(), ch2.getStatus());
    }

    private boolean isEquals(LessonStatus s1, LessonStatus s2) {
        if (s1 == null) {
            s1 = LessonStatus.NORMAL;
        }
        if (s2 == null) {
            s2 = LessonStatus.NORMAL;
        }
        return  s1 == s2;
    }

    private boolean isEquals(String s1, String s2) {
        if (s1 == null ) {
            s1 = "";
        }
        if (s2 == null) {
            s2 = "";
        }
        return s1.equals(s2);
    }

    private List<String> getAddedChanges(Map<String, Change> oldChanges, Map<String, Change> newChanges) {
        return getRemovedChanges(newChanges, oldChanges);
    }

    private List<String> getRemovedChanges(Map<String, Change> oldChanges, Map<String, Change> newChanges) {
        List<String> removed = new ArrayList<>();
        for (String change: oldChanges.keySet()) {
            if (!newChanges.containsKey(change)) {
                removed.add(change);
            }
        }
        return removed;
    }

    private List<String> getNoteChanges(DataState stateBefore, DataState stateAfter) {
        Set<String> notesBefore = stateBefore.getNoteIds();
        Set<String> notesAfter = stateAfter.getNoteIds();

        List<String> newNotesIds = new ArrayList<>();
        for (String noteId: notesAfter) {
            if (!notesBefore.contains(noteId)) {
                newNotesIds.add(noteId);
            }
        }
        return newNotesIds;
    }

    private void updateAllServices() {
        Services.getService(GroupService.class).update();
        Services.getService(NoteService.class).update();
        Services.getService(ScheduleService.class).update();
        Services.clear();
    }

    private DataState getDataState() {
        Database db = getDbInstance();
        List<Change> changes = db.getAll(Change.class);
        List<Note> notes = db.getAll(Note.class);
        return new DataState(changes, notes);
    }

    @Data
    @NoArgsConstructor
    private class DataState {
        private Map<String, Change> changes;
        private Set<String> noteIds;

        public DataState(List<Change> changes, List<Note> notes) {
            this.changes = toMap(changes);
            this.noteIds = getIdsOnly(notes);
        }

        private Map<String, Change> toMap(List<Change> changes) {
            Map<String, Change> map = new HashMap<>();
            for (Change change: changes) {
                map.put(change.getChangeId(), change);
            }
            return map;
        }


        private Set<String> getIdsOnly(List<Note> all) {
            Set<String> ids = new HashSet<>();
            for (Note note: all) {
                ids.add(note.getNoteId());
            }
            return ids;
        }

    }
}
