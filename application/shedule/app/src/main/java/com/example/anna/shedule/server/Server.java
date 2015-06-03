package com.example.anna.shedule.server;

import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.StaticLesson;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.server.dto.request.LoginRequest;
import com.example.anna.shedule.server.utils.ResponseWithStatusCode;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;
import com.example.anna.shedule.server.utils.MessageTransfer;

import static com.example.anna.shedule.server.utils.ServerResponseCreator.*;

public class Server {

    public static final String SERVER_ULR = "http://45.55.200.41:8080/";

    public static ServerResponse<User> login(String username, String password) {
        String loginPath = SERVER_ULR + "login";
        LoginRequest data = new LoginRequest(username, password);
        ResponseWithStatusCode response = MessageTransfer.post(loginPath, data);
        return convertToObjectResponse(response, User.class);
    }

    public static void logout() {
        MessageTransfer.clearCookies();
    }

    public static ServerResponseArray<StaticLesson> getScheduleByGroupId(String groupId) {
        String schedulePath = SERVER_ULR + "lesson/group/" + groupId;
        return getScheduleByPath(schedulePath);
    }

    public static ServerResponseArray<StaticLesson> getScheduleByTeacherId(String teacherId) {
        String schedulePath = SERVER_ULR + "lesson";
        return getScheduleByPath(schedulePath);
    }

    private static ServerResponseArray<StaticLesson> getScheduleByPath(String schedulePath) {
        ResponseWithStatusCode response = MessageTransfer.get(schedulePath);
        return convertToArrayResponse(response, StaticLesson.class);
    }

    public static ServerResponseArray<Change> getScheduleChangesByStudent(long fromDate, long toDate, String groupId) {
        // todo implement me
        return new ServerResponseArray<Change>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponseArray<Change> getScheduleChangesByTeacher(long dateFrom, long dateTo, String teacherId) {
        // todo implement me
        return new ServerResponseArray<Change>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponseArray<Change> getScheduleChangesByClassLeader(long fromDate, long toDate, String classLeaderId) {
        // todo implement me
        return new ServerResponseArray<Change>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponse<Object> cancelChange(String changeId, String classLeaderId) {
        // todo implement me
        return new ServerResponse<Object>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponse<Change> createChange(Change change, String classLeaderId) {
        // todo implement me
        return new ServerResponse<Change>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponse<Change> updateChange(Change change, String userId) {
        // todo implement me
        return new ServerResponse<Change>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponse<Note> createNoteByTeacher(Note note, String teacherId) {
        // todo implement me
        return new ServerResponse<Note>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponse<Note> createNoteByClassLeader(Note note, String classLeaderId) {
        // todo implement me
        return new ServerResponse<Note>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponseArray<StaticLesson> getScheduleByClassLeaderId(String classLeaderId) {
        // todo implement me
        return new ServerResponseArray<StaticLesson>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponseArray<Note> getAllNotesByClassLeaderId(String classLeaderId) {
        // todo implement me
        return new ServerResponseArray<Note>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponseArray<Note> getNotesByClassLeaderId(String lastNoteId, String classLeaderId) {
        // todo implement me
        return new ServerResponseArray<Note>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponseArray<Note> getNotesByGroupId(String lastNoteId, String groupId) {
        // todo implement me
        return new ServerResponseArray<Note>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponseArray<Note> getAllNotesByGroupId(String groupId) {
        // todo implement me
        return new ServerResponseArray<Note>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponseArray<Note> getAllNotesByTeacherId(String userId) {
        // todo implement me
        return new ServerResponseArray<Note>(ServerResponse.NO_CONNECTION_ERROR);
    }

    public static ServerResponseArray<Note> getNotesByTeacherId(String lastNoteId, String userId) {
        // todo implement me
        return new ServerResponseArray<Note>(ServerResponse.NO_CONNECTION_ERROR);
    }
}
