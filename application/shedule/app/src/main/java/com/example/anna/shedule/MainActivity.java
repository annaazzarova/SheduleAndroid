package com.example.anna.shedule;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.anna.shedule.application.database.Database;
import com.example.anna.shedule.application.login.model.LoginError;
import com.example.anna.shedule.application.login.model.LoginProgress;
import com.example.anna.shedule.application.login.service.LoginService;
import com.example.anna.shedule.application.note.model.Note;
import com.example.anna.shedule.application.note.service.NoteService;
import com.example.anna.shedule.application.schedule.model.Change;
import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.application.schedule.model.Teacher;
import com.example.anna.shedule.application.schedule.service.GroupService;
import com.example.anna.shedule.application.schedule.service.LessonsChangesService;
import com.example.anna.shedule.application.schedule.service.ScheduleService;
import com.example.anna.shedule.application.schedule.service.StaticLessonsService;
import com.example.anna.shedule.application.schedule.service.TeacherService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.utils.ContextUtils;

import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity implements
        DayList.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextUtils.setContext(getApplicationContext());

        Database.dropDatabase();

        final LoginService loginService  = Services.getService(LoginService.class);

//      loginService.login("нехорошкова л.г.", "NJZR4QB_S", new LoginService.LoginListener() {
        loginService.login("ФИиВТ ПС-31", "41WM2R5cH", new LoginService.LoginListener() {
            @Override
            public void onSuccess(User user) {
                final ScheduleService scheduleService = Services.getService(ScheduleService.class);
                final NoteService noteService = Services.getService(NoteService.class);

                List<Lesson> lessons = scheduleService.getSchedule(2015, 5, 5);

//                boolean isSuccess = noteService.createNote("Note from application!", lessons.get(0));

                Log.e("t", "y");
            }

            @Override
            public void onError(LoginError loginError) {
            }

            @Override
            public void onProgress(LoginProgress progress) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
