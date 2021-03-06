package com.example.anna.shedule.application.user.service;


import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.server.Server;
import com.example.anna.shedule.server.dto.response.ServerResponse;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class UserService {

    private User user;

    public interface LoginListener {
        void onSuccess(User user);
        void onError(int errorCode, String message);
    }

    public User getCurrentUser() {
        if (user == null) {
            user = getUserFromDb();
        }
        return user;
    }

    public User loginStudent(String groupId) {
        if (isLogin()) {
            logout();
        }

        User newUser = new User();
        newUser.setGroupId(groupId);
        return user = getDbInstance().save(newUser);
    }

    public boolean isLogin() {
        return (user != null
                || getDbInstance().getNumberOfRecordsInTable(User.TABLE_NAME) != 0);
    }

    public void logout() {
        user = null;
        getDbInstance().dropAllElements(User.TABLE_NAME);
        Server.logout();
    }

    public User getUserFromDb() {
        String selectUserQuery = "SELECT * FROM " + User.TABLE_NAME + " LIMIT 1";
        return getDbInstance().getOneByQuery(User.class, selectUserQuery);
    }

    public void login(final String username, final String password, final LoginListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerResponse<User> response = Server.login(username, password);
                if (response.isSuccess()) {
                    login(response.getResponse(), listener);
                } else {
                    listener.onError(response.getCode(), response.getMessage());
                }
            }
        }).start();
    }

    private void login(User user, LoginListener listener) {
        user = getDbInstance().save(user);
        listener.onSuccess(user);
    }
}
