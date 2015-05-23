package com.example.anna.shedule.application.schedule.service;

import com.example.anna.shedule.application.database.Database;
import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.application.schedule.model.WeekPeriodicity;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.server.Server;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class StaticLessonService {

    private GroupService groupService;

    public StaticLessonService() {
        this.groupService = new GroupService();
    }

    public List<Lesson> getLessons(WeekPeriodicity periodicity, int dayOfWeek) {
        String query = "SELECT * FROM " + Lesson.TABLE_NAME
                + " WHERE dayOfWeek='" + dayOfWeek
                + "' AND " + getPeriodicityCondition(periodicity);

        return getLessonsByQuery(query);
    }

    private String getPeriodicityCondition(WeekPeriodicity periodicity) {
        return "(weekPeriodicity='" + periodicity.getId() + "' OR weekPeriodicity='" + WeekPeriodicity.BOTH.getId() + "')";
    }

    private List<Lesson> getLessonsByQuery(String query) {
        List<Lesson> lessons = getDbInstance().getByQuery(Lesson.class, query);
        groupService.mapGroupsOnLessons(lessons);
        return lessons;
    }

    public boolean updateLessons() {
        ServerResponseArray<Lesson> response = getScheduleByCurrentUser();
        boolean isSuccess = response.isSuccess();
        if (isSuccess) {
            updateLessonsInDb(response.getResponse());
        }
        return isSuccess;
    }

    private void updateLessonsInDb(List<Lesson> lessons) {
        Database db = getDbInstance();
        db.dropAllElements(Lesson.TABLE_NAME);
        db.save(lessons);
    }

    private ServerResponseArray<Lesson> getScheduleByCurrentUser() {
        User user = new UserService().getCurrentUser();
        return getScheduleByUser(user);
    }

    private ServerResponseArray<Lesson> getScheduleByUser(User user) {
        if (user == null) {
            return ServerResponse.getLogicError(Lesson.class);
        }

        switch (user.getType()) {
            case TEACHER:
                return Server.getScheduleByTeacherId(user.getExtendedId());
            case STUDENT:
            case CLASS_LEADER:
                return Server.getScheduleByGroupId(user.getExtendedId());
            default:
                return ServerResponse.getLogicError(Lesson.class);
        }
    }

    public void updateChanges() {

    }

    public List<Lesson> getAllLessons() {
        String query = "SELECT * FROM " + Lesson.TABLE_NAME;
        return getLessonsByQuery(query);
    }
}
