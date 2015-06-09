package com.example.anna.shedule.application.login.service;

import com.example.anna.shedule.application.login.model.LoginError;
import com.example.anna.shedule.application.login.model.LoginProgress;
import com.example.anna.shedule.application.note.service.NoteService;
import com.example.anna.shedule.application.schedule.service.GroupService;
import com.example.anna.shedule.application.schedule.service.LessonsChangesService;
import com.example.anna.shedule.application.schedule.service.ScheduleService;
import com.example.anna.shedule.application.schedule.service.StaticLessonsService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.service.ScheduleIntentService;

public class LoginService {

    private final UserService userService;
    private final LessonsChangesService lessonsChangesService;
    private final StaticLessonsService staticLessonsService;
    private final GroupService groupService;
    private final NoteService noteService;


    public LoginService() {
        this.lessonsChangesService = Services.getService(LessonsChangesService.class);
        this.staticLessonsService = Services.getService(StaticLessonsService.class);
        this.groupService = Services.getService(GroupService.class);
        this.noteService = Services.getService(NoteService.class);
        this.userService = Services.getService(UserService.class);
    }

    public interface LoginListener {
        void onSuccess(User user);
        void onError(LoginError loginError);
        void onProgress(LoginProgress progress);
    }

    public boolean isLogin() {
        return userService.isLogin();
    }

    public void logout() {
        userService.logout();
        ScheduleIntentService.disable();
    }

    public synchronized void loginAsStudent(final String groupId, final LoginListener loginListener) {
        loginListener.onProgress(LoginProgress.AUTHORIZATION);
        new Thread(new Runnable() {
            @Override
            public void run() {
                userService.loginStudent(groupId);
                loadLessons(loginListener);
            }
        }).start();
    }

    public synchronized void login(String username, String password, final LoginListener loginListener) {
        loginListener.onProgress(LoginProgress.AUTHORIZATION);
        userService.login(username, password, new UserService.LoginListener() {
            @Override
            public void onSuccess(User user) {
                loadLessons(loginListener);
            }

            @Override
            public void onError(int errorCode, String message) {
                LoginError error = (errorCode == ServerResponse.NO_CONNECTION_ERROR)
                    ? LoginError.NO_CONNECTION_ERROR
                    : LoginError.INVALID_USERNAME_OR_PASSWORD;
                loginListener.onError(error);
            }
        });
    }

    private void loadLessons(LoginListener listener) {
        listener.onProgress(LoginProgress.LOAD_GROUPS);

        if (groupService.update()) {
            listener.onProgress(LoginProgress.LOAD_LESSON);
        } else {
            listener.onError(LoginError.DOWNLOAD_DATA_ERRO);
            return;
        }

        if (staticLessonsService.update()) {
            listener.onProgress(LoginProgress.LOAD_CHANGES);
        } else {
            listener.onError(LoginError.DOWNLOAD_DATA_ERRO);
            return;
        }

        if (lessonsChangesService.update()) {
            listener.onProgress(LoginProgress.LOAD_NOTES);
        } else {
            listener.onError(LoginError.DOWNLOAD_DATA_ERRO);
            return;
        }

        if (noteService.update()) {
            listener.onProgress(LoginProgress.DONE);
        } else {
            listener.onError(LoginError.DOWNLOAD_DATA_ERRO);
            return;
        }

        ScheduleIntentService.enable();
        listener.onSuccess(userService.getCurrentUser());
    }
}
