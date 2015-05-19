package com.example.anna.shedule.server;

import com.example.anna.shedule.server.cookies.CookieManager;
import com.example.anna.shedule.server.dto.LessonDTO;
import com.example.anna.shedule.server.dto.LoginDTO;
import com.example.anna.shedule.server.dto.request.LoginRequest;
import com.example.anna.shedule.server.utils.ResponseWithStatusCode;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;
import com.example.anna.shedule.server.utils.MessageTransfer;

import static com.example.anna.shedule.server.utils.ServerResponseCreator.*;

public class Service {

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

    public static ServerResponseArray<LessonDTO> getScheduleByGroupId(String groupId) {
        String loginPath = SERVER_ULR + "api/schedule/?group=" + groupId;
        ResponseWithStatusCode response = MessageTransfer.get(loginPath);
        return convertToArrayResponse(response, LessonDTO.class);
    }
}
