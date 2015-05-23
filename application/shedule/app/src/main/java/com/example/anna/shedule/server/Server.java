package com.example.anna.shedule.server;

import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.server.dto.LoginDTO;
import com.example.anna.shedule.server.dto.request.LoginRequest;
import com.example.anna.shedule.server.utils.ResponseWithStatusCode;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;
import com.example.anna.shedule.server.utils.MessageTransfer;

import static com.example.anna.shedule.server.utils.ServerResponseCreator.*;

public class Server {

    public static final String SERVER_ULR = "http://45.55.200.41:3000/";

    public static ServerResponse<LoginDTO> login(String username, String password) {
        String loginPath = SERVER_ULR + "login";
        LoginRequest data = new LoginRequest(username, password);
        ResponseWithStatusCode response = MessageTransfer.post(loginPath, data);
        return convertToObjectResponse(response, LoginDTO.class);
    }

    public static void logout() {
        MessageTransfer.clearCookies();
    }

    public static ServerResponseArray<Lesson> getScheduleByGroupId(String groupId) {
        String schedulePath = SERVER_ULR + "api/schedule/?group=" + groupId;
        return getScheduleByPath(schedulePath);
    }

    public static ServerResponseArray<Lesson> getScheduleByTeacherId(String teacherId) {
        String schedulePath = SERVER_ULR + "api/schedule?teacher=" + teacherId;
        return getScheduleByPath(schedulePath);
    }

    private static ServerResponseArray<Lesson> getScheduleByPath(String schedulePath) {
        ResponseWithStatusCode response = MessageTransfer.get(schedulePath);
        return convertToArrayResponse(response, Lesson.class);
    }
}
