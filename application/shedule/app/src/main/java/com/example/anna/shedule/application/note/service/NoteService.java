package com.example.anna.shedule.application.note.service;

import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.schedule.model.Lesson;
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

    private List<Note> getNotesByLesson(String lessonId, String changeId, long startOfDay) {
        String queryByLesson = getQueryForSelectingNoteByLesson(lessonId, changeId);
        String query = "SELECT * FROM " + Note.TABLE_NAME + " WHERE " + queryByLesson
                + " AND " + "date >= '" + startOfDay + "'"
                + " AND  date <= '" + (startOfDay + DateUtils.DAY) + "' "
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

    public List<Lesson> mapNotesOnLessons(List<Lesson> lessons, int year, int month, int day) {
        long startOfDay = DateUtils.startOfDay(year, month, day);
        for (Lesson lesson: lessons) {
            List<Note> notes = getNotesByLesson(lesson, startOfDay);
            lesson.setNotes(notes);
        }
        return lessons;
    }

    private List<Note> getNotesByLesson(Lesson lesson, long startOfDay) {
        String lessonId = lesson.getLessonId();
        String changeId = lesson.getChangeId();
        return getNotesByLesson(lessonId, changeId, startOfDay);
    }
}
