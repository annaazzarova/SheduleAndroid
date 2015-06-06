package com.example.anna.shedule.application.note.service;

import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.user.service.RequestFactory;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;
import com.example.anna.shedule.utils.DateUtils;

import java.util.List;

import lombok.NoArgsConstructor;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

@NoArgsConstructor
public class NoteService {

    private RequestFactory requests = new RequestFactory();

    public Note createNote(Note note) {
        ServerResponse<Note> response = requests.createNote(note);
        return (response.isSuccess())
                ? getDbInstance().save(note)
                : null;
    }

    public boolean update() {
        String lastNoteId = getLastNoteId();
        ServerResponseArray<Note> response = requests.getNotes(lastNoteId);
        if (response.isSuccess()) {
            getDbInstance().save(response.getResponse());
        }
        return response.isSuccess();
    }

    public List<Note> getNotesByLessonId(String lessonId) {
        String query = "SELECT * FROM " + Note.TABLE_NAME + " WHERE lessonId='" + lessonId + "'";
        return getDbInstance().getByQuery(Note.class, query);
    }

    public List<Note> getAllNotes(int offset, int limit) {
        String query = "SELECT * FROM " + Note.TABLE_NAME + " LIMIT " + limit + " OFFSET " + offset;
        return getDbInstance().getByQuery(Note.class, query);
    }

    public String getLastNoteId() {
        String query = "SELECT * FROM " + Note.TABLE_NAME + " ORDER BY noteId DESC LIMIT 1";
        Note note = getDbInstance().getOneByQuery(Note.class, query);
        return note == null ? null : note.getNoteId();
    }

    public List<Note> getNotesByLesson(String lessonId, String changeId, int year, int month, int day) {
        long startOfLessonDay = DateUtils.startOfDay(year, month, day);
        String queryByLesson = getQueryForSelectingNoteByLesson(lessonId, changeId);
        String query = "SELECT * FROM " + Note.TABLE_NAME + " WHERE " + queryByLesson
                + " AND " + "date >= '" + startOfLessonDay + "'"
                + " AND  date <= '" + (startOfLessonDay + DateUtils.DAY) + "' "
                + " ORDER BY noteId DESC";
        return getDbInstance().getByQuery(Note.class, query);
    }

    private String getQueryForSelectingNoteByLesson(String lessonId, String changeId) {
        if (lessonId == null) {
            return " changeId='" + changeId + "' ";
        } else if (changeId == null) {
            return " lessonId='" + lessonId + "' ";
        } else {
            return " (lessonId='" + lessonId + "' OR changeId='" + changeId + "') ";
        }
    }

    public List<Note> getNotes() {
        return getAllNotes(0, 100);
    }
}
