package com.example.anna.shedule.application.user.service;


import com.example.anna.shedule.application.note.dto.CreateNoteRequest;
import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.schedule.dto.CreateChangeByLessonRequest;
import com.example.anna.shedule.application.schedule.dto.CreateNewLessonRequest;
import com.example.anna.shedule.application.schedule.dto.UpdateChangeRequest;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.helper.StaticLesson;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.server.Server;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RequestFactory {

    private UserService userService = Services.getService(UserService.class);

    public Requests getRequestsByUser(User user) {
        if (user == null) {
            return INVALID_USER_REQUESTS;
        }
        switch (user.getType()) {
            case TEACHER: return TEACHER_REQUESTS;
            case STUDENT: return STUDENT_REQUESTS;
            case CLASS_LEADER: return CLASS_LEADER_REQUESTS;
            default: return INVALID_USER_REQUESTS;
        }
    }

    public ServerResponseArray<Change> getScheduleChanges(long dateFrom, long dateTo) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).getScheduleChanges(user, dateFrom, dateTo);
    }

    public ServerResponse<Object> deleteCancel(String changeId) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).deleteCancel(changeId);
    }

    public ServerResponse<Change> createNewLesson(Change change) {
        User user = userService.getCurrentUser();
        CreateNewLessonRequest request = new CreateNewLessonRequest(change);
        return getRequestsByUser(user).createNewLesson(request);
    }

    public ServerResponse<Object> updateChange(Change change) {
        User user = userService.getCurrentUser();
        UpdateChangeRequest request = new UpdateChangeRequest(change);
        return getRequestsByUser(user).updateChange(request, change.getChangeId());
    }

    public ServerResponseArray<StaticLesson> getScheduleByCurrentUser() {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).getSchedule(user.getGroupId());
    }

    public ServerResponseArray<Note> getNotes(String lastNoteId) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).getNotes(lastNoteId, user.getGroupId());
    }

    public ServerResponse<Note> createNoteToLesson(CreateNoteRequest request, String lessonId) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).createNoteToLesson(request, lessonId);
    }


    public ServerResponse<Note> createNoteToChange(CreateNoteRequest request, String changeId) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).createNoteToChange(request, changeId);
    }

    public ServerResponse<Change> changeExistsLesson(Change change) {
        User user = userService.getCurrentUser();
        CreateChangeByLessonRequest request = new CreateChangeByLessonRequest(change);
        return getRequestsByUser(user).createNewChangeToLesson(request, change.getLessonId());
    }


    private abstract static class Requests {
        public ServerResponseArray<Change> getScheduleChanges(User user, long dateFrom, long dateTo) {
            return ServerResponse.getLogicError(Change.class);
        }

        public ServerResponse<Object> deleteCancel(String changeId) {
            return new ServerResponse<Object>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponse<Change> createNewLesson(CreateNewLessonRequest change) {
            return new ServerResponse<Change>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponse<Object> updateChange(UpdateChangeRequest request, String changeId) {
            return new ServerResponse<Object>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponseArray<StaticLesson> getSchedule(String userId) {
            return new ServerResponseArray<StaticLesson>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponseArray<Note> getNotes(String lastNoteId, String userId) {
            return new ServerResponseArray<Note>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponse<Note> createNoteToLesson(CreateNoteRequest request, String lessonId) {
            return new ServerResponse<Note>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponse<Note> createNoteToChange(CreateNoteRequest request, String changeId) {
            return new ServerResponse<Note>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponse<Change> createNewChangeToLesson(CreateChangeByLessonRequest request, String lessonId) {
            return new ServerResponse<Change>(ServerResponse.LOGIC_ERROR);
        }
    }

    private static Requests INVALID_USER_REQUESTS = new Requests() {
    };

    private static Requests TEACHER_REQUESTS = new Requests() {
        @Override
        public ServerResponseArray<Change> getScheduleChanges(User user, long dateFrom, long dateTo) {
            return Server.getScheduleChanges(dateFrom, dateTo);
        }

        @Override
        public ServerResponseArray<StaticLesson> getSchedule(String userId) {
            return Server.getSchedule();
        }

        @Override
        public ServerResponseArray<Note> getNotes(String lastNoteId, String userId) {
            return (lastNoteId == null)
                    ? Server.getAllNotes()
                    : Server.getNotes(lastNoteId);
        }

        @Override
        public ServerResponse<Note> createNoteToLesson(CreateNoteRequest request, String lessonId) {
            return Server.createNoteToLesson(request, lessonId);
        }

        @Override
        public ServerResponse<Note> createNoteToChange(CreateNoteRequest request, String changeId) {
            return Server.createNoteToChange(request, changeId);
        }
    };

    private static Requests STUDENT_REQUESTS = new Requests() {
        @Override
        public ServerResponseArray<Change> getScheduleChanges(User user, long dateFrom, long dateTo) {
            return Server.getScheduleChangesByStudent(dateFrom, dateTo, user.getGroupId());
        }

        @Override
        public ServerResponseArray<StaticLesson> getSchedule(String groupId) {
            return Server.getScheduleByGroupId(groupId);
        }

        @Override
        public ServerResponseArray<Note> getNotes(String lastNoteId, String groupId) {
            return (lastNoteId == null)
                    ? Server.getAllNotesByGroupId(groupId)
                    : Server.getNotesByGroupId(lastNoteId, groupId);
        }
    };

    private static Requests CLASS_LEADER_REQUESTS = new Requests() {
        @Override
        public ServerResponseArray<Change> getScheduleChanges(User user, long dateFrom, long dateTo) {
            return Server.getScheduleChanges(dateFrom, dateTo);
        }

        @Override
        public ServerResponse<Object> deleteCancel(String changeId) {
            return Server.deleteCancel(changeId);
        }

        @Override
        public ServerResponse<Change> createNewLesson(CreateNewLessonRequest change) {
            return Server.createNewLesson(change);
        }

        @Override
        public ServerResponse<Object> updateChange(UpdateChangeRequest change, String changeId) {
            return Server.updateChange(change, changeId);
        }

        public ServerResponse<Change> createNewChangeToLesson(CreateChangeByLessonRequest request, String lessonId) {
            return Server.createNewChangeToLesson(request, lessonId);
        }

        @Override
        public ServerResponseArray<StaticLesson> getSchedule(String userId) {
            return Server.getSchedule();
        }

        @Override
        public ServerResponseArray<Note> getNotes(String lastNoteId, String classLeaderId) {
            return (lastNoteId == null)
                    ? Server.getAllNotes()
                    : Server.getNotes(lastNoteId);
        }

        @Override
        public ServerResponse<Note> createNoteToLesson(CreateNoteRequest request, String lessonId) {
            return Server.createNoteToLesson(request, lessonId);
        }

        @Override
        public ServerResponse<Note> createNoteToChange(CreateNoteRequest request, String changeId) {
            return Server.createNoteToChange(request, changeId);
        }
    };
}
