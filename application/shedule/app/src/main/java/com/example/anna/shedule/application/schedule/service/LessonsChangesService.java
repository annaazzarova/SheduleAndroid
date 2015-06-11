package com.example.anna.shedule.application.schedule.service;


import android.util.Log;

import com.example.anna.shedule.application.database.Database;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.ChangeAction;
import com.example.anna.shedule.application.schedule.model.helper.LessonStatus;
import com.example.anna.shedule.application.schedule.model.helper.WeekPeriodicity;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.service.RequestFactory;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;
import com.example.anna.shedule.utils.DateUtils;
import com.example.anna.shedule.utils.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class LessonsChangesService {

    public static final int DAYS_OFFSET = 90;

    private RequestFactory requests = Services.getService(RequestFactory.class);

    public interface ChangeListener{
        void onSuccess();
        void onError();
    }

    public boolean update() {
        long currentTime = System.currentTimeMillis();
        long timeOffset = TimeUnit.DAYS.toMillis(DAYS_OFFSET);

        long fromDate = currentTime - timeOffset;
        long toDate = currentTime + timeOffset;

        ServerResponseArray<Change> response = requests.getScheduleChanges(fromDate, toDate);
        if (response.isSuccess()) {
            replaceChanges(response.getResponse());
        }
        return response.isSuccess();
    }

    public List<Change> getChanges(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        long timeInMillis = DateUtils.middleOfDay(year, month, day);
        return getChanges(timeInMillis);
    }

    public boolean deleteChange(Change change) {
        ServerResponse<Object> response = requests.deleteCancel(change.getChangeId());
        if (response.isSuccess()) {
            deleteByChangeId(change);
        }
        return response.isSuccess();
    }

    private void deleteByChangeId(Change change) {
        getDbInstance().delete(Change.TABLE_NAME, "changeId='" + change.getChangeId() + "'");
    }

    public boolean createNewLesson(Change change, int year, int month, int day) {
        setDefaultValues(change, year, month, day);

        boolean isValid = checkNewLessonStructure(change);
        if (!isValid) {
            return false;
        }

        ServerResponse<Change> response = requests.createNewLesson(change);
        if (response.isSuccess()) {
            Change newChange = response.getResponse();
            getDbInstance().save(newChange);
        }

        return response.isSuccess();
    }

    public boolean changeExistsLesson(Change change, int year, int month, int day) {
        setDefaultValues(change, year, month, day);

        boolean isValid = checkChangeExistsLessonStructure(change);
        if (!isValid) {
            return false;
        }

        ServerResponse<Change> response = requests.changeExistsLesson(change);
        if (response.isSuccess()) {
            Change newChange = response.getResponse();
            getDbInstance().save(newChange);
        }

        return response.isSuccess();
    }

    private boolean checkChangeExistsLessonStructure(Change change) {
        if (change.getGroupIds() != null) {
            Log.e("change", "Groups can't be changed");
            return false;
        }

        if (change.getTeacherId() != null) {
            Log.e("change", "Teacher can't be changed");
            return false;
        }

        return true;
    }

    public boolean updateExistsChange(Change change, int year, int month, int day) {
        setDefaultValues(change, year, month, day);

        boolean isValid = checkUpdateChangeStructure(change);
        if (!isValid) {
            return false;
        }

        ServerResponse<Object> response = requests.updateChange(change);
        if (response.isSuccess()) {
            getDbInstance().save(change);
        }
        return response.isSuccess();
    }

    private Change setDefaultValues(Change change, int year, int month, int day) {
        long startOfDay = DateUtils.startOfDay(year, month, day);
        int dayOfWeek = DateUtils.dayOfWeek(year, month, day);

        change.setDateFrom(new Date(startOfDay));
        change.setDateTo(new Date(startOfDay + DateUtils.DAY));
        change.setWeekPeriodicity(WeekPeriodicity.BOTH);
        change.setDayOfWeek(dayOfWeek);
        return change;
    }

    private boolean checkUpdateChangeStructure(Change change) {
        boolean isRelatedWithLesson = change.getLessonId() != null && !change.getLessonId().isEmpty();
        boolean isCanceled = change.getStatus() != null && change.getStatus() == LessonStatus.CANCELED;
        if (!isRelatedWithLesson && isCanceled) {
            Log.e("change", "Cancel status can have only changes related with lesson(use delete method)");
            return false;
        }

        if (change.getId() <= 0 || change.getChangeId() == null) {
            Log.e("change", "Can update only already exists changes in db");
            return false;
        }

        return true;
    }

    private boolean checkNewLessonStructure(Change change) {
        if (change.getStatus() == null) {
            change.setStatus(LessonStatus.NORMAL);
        } else if (change.getStatus() == LessonStatus.CANCELED) {
            Log.e("change", "Can't create lesson with canceled status");
            return false;
        }

        if (change.getLessonId() != null) {
            Log.e("change", "Can't create new lesson related with other");
            return false;
        }

        if (change.getDateFrom() == null || change.getDateTo() == null) {
            Log.e("change", "New lesson change must have duration");
            return false;
        }

        if (change.getTime() == null || change.getWeekPeriodicity() == null
                || change.getDayOfWeek() < 0 || change.getDayOfWeek() > 7) {
            Log.e("change", "New lesson change must have date");
            return false;
        }

        if (change.getTeacherId() == null) {
            Log.e("change", "New lesson must have teacher");
            return false;
        }

        if (change.getGroupIds() == null || StringUtils.split(change.getGroupIds(), ',').size() != 1) {
            Log.e("change", "New lesson must one group");
            return false;
        }

        return true;
    }

    private List<Change> getChanges(long time) {
        String query = "SELECT * FROM " + Change.TABLE_NAME
                + " WHERE (dateFrom <= '" + time + "' AND '" + time + "' <= dateTo) ";
        return getDbInstance().getByQuery(Change.class, query);
    }

    private List<Change> replaceChanges(List<Change> changes) {
        Database db = getDbInstance();
        db.dropAllElements(Change.TABLE_NAME);
        return db.save(changes);
    }

    public void doAction(final ChangeAction action, final Change change, final int year,
                         final int month, final int day, final ChangeListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = doAction(action, change, year, month, day);
                if (isSuccess) {
                    listener.onSuccess();
                } else {
                    listener.onError();
                }
            }
        }).start();
    }

    public boolean doAction(ChangeAction action, Change change, int year, int month, int day) {
        boolean isSuccess;
        switch (action) {
            case CREATE_BY_EXISTS:
                isSuccess = changeExistsLesson(change, year, month, day);
                break;
            case CREATE_NEW:
                isSuccess = createNewLesson(change, year, month, day);
                break;
            case DELETE:
                isSuccess = deleteChange(change);
                break;
            case UPDATE:
                isSuccess = updateExistsChange(change, year, month, day);
                break;
            default:
                isSuccess = false;
        }
        return isSuccess;
    }
}
