package com.example.anna.shedule.application.schedule.service;

import com.example.anna.shedule.application.database.Database;
import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.application.schedule.model.WeekPeriodicity;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.server.Server;
import com.example.anna.shedule.server.dto.LessonDTO;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;

import java.util.ArrayList;
import java.util.List;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class StaticLessonService {

    public List<Lesson> getLessons(WeekPeriodicity periodicity, int dayOfWeek) {
        String query = "SELECT * FROM " + Lesson.TABLE_NAME
                + " WHERE dayOfWeek='" + dayOfWeek
                + "' AND ='" + periodicity.getId() + "'";

        return getDbInstance().getByQuery(Lesson.class, query);
    }

    public boolean updateLessons() {
        ServerResponseArray<LessonDTO> response = getScheduleByCurrentUser();
        boolean isSuccess = response.isSuccess();
        if (isSuccess) {
            updateLessonsInDb(response.getResponse());
        }
        return isSuccess;
    }

    private void updateLessonsInDb(List<LessonDTO> lessonsDto) {
        Database db = getDbInstance();
        db.dropAllElements(Lesson.TABLE_NAME);
        saveLessons(lessonsDto);
    }

    private List<Lesson> saveLessons(List<LessonDTO> dtoItems) {
        List<Lesson> lessons = new ArrayList<>(dtoItems.size());
        for (LessonDTO dto: dtoItems) {
            //todo FIX ME
            Lesson lesson = new Lesson(dto);
            lessons.add(lesson);
        }
        return lessons;
    }

    private ServerResponseArray<LessonDTO> getScheduleByCurrentUser() {
        User user = new UserService().getCurrentUser();
        return getScheduleByUser(user);
    }

    private ServerResponseArray<LessonDTO> getScheduleByUser(User user) {
        if (user == null) {
            return ServerResponse.getLogicError(LessonDTO.class);
        }

        switch (user.getType()) {
            case TEACHER:
                return Server.getScheduleByTeacherId(user.getExtendedId());
            case STUDENT:
            case CLASS_LEADER:
                return Server.getScheduleByGroupId(user.getExtendedId());
            default:
                return ServerResponse.getLogicError(LessonDTO.class);
        }
    }

    public void updateChanges() {

    }


}
