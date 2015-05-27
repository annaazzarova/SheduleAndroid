package com.example.anna.shedule;

import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.anna.shedule.application.schedule.model.StaticLesson;
import com.example.anna.shedule.application.schedule.model.WeekPeriodicity;
import com.example.anna.shedule.application.schedule.service.StaticLessonsService;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.utils.ContextUtils;

import java.util.List;


public class MainActivity extends ActionBarActivity implements
        DayList.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextUtils.setContext(getApplicationContext());

        //Database.dropDatabase();

        final UserService userService = new UserService();

        if (userService.isLogin()) {

            final StaticLessonsService staticLessonsService = new StaticLessonsService();

            List<StaticLesson> lessons1 = staticLessonsService.getLessons(WeekPeriodicity.RED, 1);
            List<StaticLesson> lessons33 = staticLessonsService.getLessons(WeekPeriodicity.RED, 5);
            List<StaticLesson> lessons34 = staticLessonsService.getLessons(WeekPeriodicity.BLUE, 5);

        } else {

            userService.loginTeacher("Нехорошкова Л.Г.", "нехорошкова", new UserService.LoginListener() {
                @Override
                public void onSuccess(User user) {
                    new Thread(new Runnable() {

                        final StaticLessonsService staticLessonsService = new StaticLessonsService();
                        @Override
                        public void run() {

                            staticLessonsService.updateLessons();
                            Log.e("", "");
                            List<StaticLesson> lessons = staticLessonsService.getAllLessons();
                            Log.e("", "");
                        }
                    }).start();
                }

                @Override
                public void onError(int errorCode, String message) {
                }
            });
        }

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
