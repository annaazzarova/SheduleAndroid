package com.example.anna.shedule.server;

import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.Group;
import com.example.anna.shedule.application.schedule.model.helper.StaticLesson;
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

    public static ServerResponseArray<StaticLesson> getSchedule() {
        String schedulePath = SERVER_ULR + "lesson";
        return getScheduleByPath(schedulePath);
    }

    private static ServerResponseArray<StaticLesson> getScheduleByPath(String schedulePath) {
        ResponseWithStatusCode response = MessageTransfer.get(schedulePath);
        return convertToArrayResponse(response, StaticLesson.class);
    }

    public static ServerResponseArray<Change> getScheduleChangesByStudent(long fromDate, long toDate, String groupId) {
        String changePath = SERVER_ULR + "change/group/" + groupId;
        ResponseWithStatusCode response = MessageTransfer.get(changePath);
        return convertToArrayResponse(response, Change.class);
    }

    public static ServerResponseArray<Change> getScheduleChanges(long dateFrom, long dateTo) {
        String changePath = SERVER_ULR + "change";
        ResponseWithStatusCode response = MessageTransfer.get(changePath);
        return convertToArrayResponse(response, Change.class);
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

    public static ServerResponseArray<Note> getNotesByGroupId(String lastNoteId, String groupId) {
        String notePath = SERVER_ULR + "note/group/" + groupId + "?lastNoteId=" + lastNoteId;
        ResponseWithStatusCode response = MessageTransfer.get(notePath);
        return convertToArrayResponse(response, Note.class);
    }

    public static ServerResponseArray<Note> getAllNotesByGroupId(String groupId) {
        String notePath = SERVER_ULR + "note/group/" + groupId;
        ResponseWithStatusCode response = MessageTransfer.get(notePath);
        return convertToArrayResponse(response, Note.class);
    }

    public static ServerResponseArray<Group> getAllGroups() {
        String groupsPath = SERVER_ULR + "group";
        ResponseWithStatusCode response = MessageTransfer.get(groupsPath);
        return convertToArrayResponse(response, Group.class);
    }

    public static ServerResponseArray<Note> getAllNotes() {
        String notePath = SERVER_ULR + "note";
        ResponseWithStatusCode response = MessageTransfer.get(notePath);
        return convertToArrayResponse(response, Note.class);
    }

    public static ServerResponseArray<Note> getNotes(String lastNoteId) {
        String notePath = SERVER_ULR + "note?lastNoteId=" + lastNoteId;
        ResponseWithStatusCode response = MessageTransfer.get(notePath);
        return convertToArrayResponse(response, Note.class);
    }
}
