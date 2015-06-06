package com.example.anna.shedule.application.user.service;


import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.StaticLesson;
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

    public ServerResponse<Object> cancelChange(String changeId) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).cancelChange(changeId, user.getGroupId());
    }

    public ServerResponse<Change> createChange(Change change) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).createChange(change, user.getGroupId());
    }

    public ServerResponse<Change> updateChange(Change change) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).updateChange(change, user.getGroupId());
    }

    public ServerResponse<Note> createNote(Note note) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).createNote(note, user.getGroupId());
    }

    public ServerResponseArray<StaticLesson> getScheduleByCurrentUser() {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).getSchedule(user.getGroupId());
    }

    public ServerResponseArray<Note> getNotes(String lastNoteId) {
        User user = userService.getCurrentUser();
        return getRequestsByUser(user).getNotes(lastNoteId, user.getGroupId());
    }

    private abstract static class Requests {
        public ServerResponseArray<Change> getScheduleChanges(User user, long dateFrom, long dateTo) {
            return ServerResponse.getLogicError(Change.class);
        }

        public ServerResponse<Object> cancelChange(String changeId, String userId) {
            return new ServerResponse<Object>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponse<Change> createChange(Change change, String userId) {
            return new ServerResponse<Change>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponse<Change> updateChange(Change change, String userId) {
            return new ServerResponse<Change>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponse<Note> createNote(Note note, String userId) {
            return new ServerResponse<Note>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponseArray<StaticLesson> getSchedule(String userId) {
            return new ServerResponseArray<StaticLesson>(ServerResponse.LOGIC_ERROR);
        }

        public ServerResponseArray<Note> getNotes(String lastNoteId, String userId) {
            return new ServerResponseArray<Note>(ServerResponse.LOGIC_ERROR);
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
        public ServerResponse<Note> createNote(Note note, String userId) {
            return Server.createNoteByTeacher(note, userId);
        }

        @Override
        public ServerResponseArray<StaticLesson> getSchedule(String userId) {
            return Server.getSchedule();
        }

        @Override
        public ServerResponseArray<Note> getNotes(String lastNoteId, String userId) {
            return (lastNoteId == null)
                    ? Server.getAllNotesByTeacherId(userId)
                    : Server.getNotesByTeacherId(lastNoteId, userId);
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
        public ServerResponse<Object> cancelChange(String changeId, String userId) {
            return Server.cancelChange(changeId, userId);
        }

        @Override
        public ServerResponse<Change> createChange(Change change, String userId) {
            return Server.createChange(change, userId);
        }

        @Override
        public ServerResponse<Change> updateChange(Change change, String userId) {
            return Server.updateChange(change, userId);
        }

        @Override
        public ServerResponse<Note> createNote(Note note, String userId) {
            return Server.createNoteByClassLeader(note, userId);
        }

        @Override
        public ServerResponseArray<StaticLesson> getSchedule(String userId) {
            return Server.getSchedule();
        }

        @Override
        public ServerResponseArray<Note> getNotes(String lastNoteId, String classLeaderId) {
            return (lastNoteId == null)
                    ? Server.getAllNotesByClassLeaderId(classLeaderId)
                    : Server.getNotesByClassLeaderId(lastNoteId, classLeaderId);
        }
    };
}
