package com.example.anna.shedule.application.note.service;

import com.example.anna.shedule.application.note.dto.CreateNoteRequest;
import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.application.user.service.RequestFactory;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;
import com.example.anna.shedule.utils.DateUtils;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

@NoArgsConstructor
public class NoteService {

    private RequestFactory requests = new RequestFactory();

    @Data
    @NoArgsConstructor
    public static class CreateNote {
        private String text;
        private String changeId;
        private String lessonId;
        private long startOfDay;

        public CreateNote(String text, String lessonId, String changeId, long startOfDay) {
            this.text = text;
            this.lessonId = lessonId;
            this.changeId = changeId;
            this.startOfDay = startOfDay;
        }

        public boolean isRelatedToChange() {
            return changeId != null;
        }
    }

    public interface NoteCreateListener {
        void onSuccess();
        void onError();
    }

    public boolean update() {
        String lastNoteId = null; //todo fix ME ?? getLastNoteId();
        ServerResponseArray<Note> response = requests.getNotes(lastNoteId);
        if (response.isSuccess()) {
            getDbInstance().dropAllElements(Note.TABLE_NAME);
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

    public List<Lesson> mapNotesOnLessons(List<Lesson> lessons) {
        for (Lesson lesson: lessons) {
            List<Note> notes = getNotesByLesson(lesson);
            lesson.setNotes(notes);
        }
        return lessons;
    }

    public void createNote(final String text, final CreateNote note, final NoteCreateListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = createNote(text, note);
                if (isSuccess) {
                    listener.onSuccess();
                } else {
                    listener.onError();
                }
            }
        }).start();
    }

    public boolean createNote(String text, CreateNote note) {
        ServerResponse<Note> request = createNoteOnServer(text, note);
        if (request.isSuccess()) {
            getDbInstance().save(request.getResponse());
        }
        return request.isSuccess();
    }

    private ServerResponse<Note> createNoteOnServer(String text, CreateNote note) {
        long date = note.getStartOfDay() + DateUtils.DAY;
        CreateNoteRequest noteRequest = new CreateNoteRequest(text, date);
        return (note.isRelatedToChange())
                ? requests.createNoteToChange(noteRequest, note.getChangeId())
                : requests.createNoteToLesson(noteRequest, note.getLessonId());

    }

    private List<Note> getNotesByLesson(Lesson lesson) {
        String lessonId = lesson.getLessonId();
        String changeId = lesson.getChangeId();
        return getNotesByLesson(lessonId, changeId, lesson.getStartOfLessonDay());
    }
}
