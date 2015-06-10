package com.example.anna.shedule.server;

import com.example.anna.shedule.application.note.dto.CreateNoteRequest;
import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.schedule.dto.CreateChangeByLessonRequest;
import com.example.anna.shedule.application.schedule.dto.CreateNewLessonRequest;
import com.example.anna.shedule.application.schedule.dto.UpdateChangeRequest;
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

    public static ServerResponse<Object> deleteCancel(String changeId) {
        String deletePath = SERVER_ULR + "change/" + changeId;
        ResponseWithStatusCode response = MessageTransfer.delete(deletePath);
        return convertToObjectResponse(response, Object.class);
    }

    public static ServerResponse<Change> createNewLesson(CreateNewLessonRequest change) {
        String changePath = SERVER_ULR + "change";
        ResponseWithStatusCode response = MessageTransfer.post(changePath, change);
        return convertToObjectResponse(response, Change.class);
    }

    public static ServerResponse<Change> createNewChangeToLesson(CreateChangeByLessonRequest change, String lessonId) {
        String changePath = SERVER_ULR + "change/" + lessonId;
        ResponseWithStatusCode response = MessageTransfer.post(changePath, change);
        return convertToObjectResponse(response, Change.class);
    }

    public static ServerResponse<Object> updateChange(UpdateChangeRequest change, String changeId) {
        String changePath = SERVER_ULR + "change/" + changeId;
        ResponseWithStatusCode response = MessageTransfer.put(changePath, change);
        return convertToObjectResponse(response, Object.class);
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

    public static ServerResponse<Note> createNoteToChange(CreateNoteRequest request, String changeId) {
        String notePath = SERVER_ULR + "note/change/" + changeId;
        ResponseWithStatusCode response = MessageTransfer.post(notePath, request);
        return convertToObjectResponse(response, Note.class);
    }

    public static ServerResponse<Note> createNoteToLesson(CreateNoteRequest request, String lessonId) {
        String notePath = SERVER_ULR + "note/lesson/" + lessonId;
        ResponseWithStatusCode response = MessageTransfer.post(notePath, request);
        return convertToObjectResponse(response, Note.class);
    }
}
