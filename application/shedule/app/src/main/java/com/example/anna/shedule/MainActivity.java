package com.example.anna.shedule;

import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.anna.shedule.tabs.SampleFragmentPagerAdapter;
import com.example.anna.shedule.tabs.SlidingTabLayout;
import com.example.anna.shedule.application.schedule.model.StaticLesson;
import com.example.anna.shedule.application.schedule.model.WeekPeriodicity;
import com.example.anna.shedule.application.schedule.service.StaticLessonsService;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.utils.ContextUtils;

import java.util.List;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity implements
        DayList.OnFragmentInteractionListener{
    
    // Declaring Your View and Variables

    Toolbar toolbar;
    public String[] Titles= new String[] {"", "", "", "", "", ""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextUtils.setContext(getApplicationContext());

        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK);
        int firstdate = date - day_of_week +1;
        for (int i = 0; i != 6; ++i){
            Titles[i] = String.valueOf(firstdate++);
        }

        Titles[0] = String.valueOf(date);
        Titles[1] = String.valueOf(day_of_week);
        Titles[2] = String.valueOf(firstdate);
        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this, Titles));

        // Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        // Set custom tab layout
        slidingTabLayout.setCustomTabView(R.layout.custom_tab_view, 0);
        // Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        // Customize tab color
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        slidingTabLayout.setViewPager(viewPager);


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
