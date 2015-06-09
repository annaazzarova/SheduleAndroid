package com.example.anna.shedule.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.anna.shedule.application.database.Database;
import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.note.service.NoteService;
import com.example.anna.shedule.application.schedule.model.Change;
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
        DataState stateBefore = getDataState();
        updateAllServices();
        DataState stateAfter = getDataState();
        notifyAboutChanges(stateBefore, stateAfter);
    }

    private void notifyAboutChanges(DataState stateBefore, DataState stateAfter) {
        List<String> newNoteIds = getNoteChanges(stateBefore, stateAfter);
        List<Change> updatedChanges = getChangedChanges(stateBefore, stateAfter);

        if (newNoteIds.size() > 0) {
            showNewNotesNotify();
        }

        if (updatedChanges.size() > 0) {
            showLessonChangesNotify();
        }
    }

    private void showNewNotesNotify() {
        //TODO show me mew
    }

    private void showLessonChangesNotify() {
        //TODO show me mew
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
        List<String> removedChanges = getRemovedChanges(oldChanges, newChanges);
        List<String> addedChanges = getAddedChanges(oldChanges, newChanges);
        List<String> changedChanges = getUpdatedChanges(oldChanges, newChanges);

        List<String> allChanges = new ArrayList<>();
        allChanges.addAll(addedChanges);
        allChanges.addAll(removedChanges);
        allChanges.addAll(changedChanges);
        return allChanges;
    }

    private List<String> getUpdatedChanges(Map<String, Change> oldChanges, Map<String, Change> newChanges) {
        List<String> updated = new ArrayList<>();
        for (String change: oldChanges.keySet()) {
            Change other = newChanges.get(change);
            if (other != null && !other.equals(oldChanges.get(change))) {
                updated.add(change);
            }
        }
        return updated;
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
