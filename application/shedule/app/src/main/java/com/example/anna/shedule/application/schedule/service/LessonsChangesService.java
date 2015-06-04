package com.example.anna.shedule.application.schedule.service;


import com.example.anna.shedule.application.database.Database;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.service.RequestFactory;
import com.example.anna.shedule.server.dto.response.ServerResponse;
import com.example.anna.shedule.server.dto.response.ServerResponseArray;
import com.example.anna.shedule.utils.DateUtils;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.anna.shedule.application.database.Database.getDbInstance;

public class LessonsChangesService {

    public static final int DAYS_OFFSET = 90;

    private RequestFactory requests = Services.getService(RequestFactory.class);

    public boolean updateChanges() {
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

    public boolean cancelChange(Change change) {
        ServerResponse<Object> response = requests.cancelChange(change.getChangeId());
        if (response.isSuccess()) {
            getDbInstance().delete(change);
        }
        return response.isSuccess();
    }

    public Change updateChange(Change change) {
        ServerResponse<Change> response = requests.updateChange(change);
        if (response.isSuccess()) {
            Change updatedChange = response.getResponse();
            updatedChange.setId(change.getId());
            return getDbInstance().save(updatedChange);
        } else {
            return null;
        }
    }

    public Change createChange(Change change) {
        ServerResponse<Change> response = requests.createChange(change);
        if (response.isSuccess()) {
            return getDbInstance().save(response.getResponse());
        } else {
            return null;
        }
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

}
