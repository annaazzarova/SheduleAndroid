package com.example.anna.shedule;

import android.app.Service;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.anna.shedule.activities.menu.BaseActivity;
import com.example.anna.shedule.application.login.model.LoginError;
import com.example.anna.shedule.application.login.model.LoginProgress;
import com.example.anna.shedule.application.login.service.LoginService;
import com.example.anna.shedule.application.note.service.NoteService;
import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.application.schedule.service.ScheduleService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.model.UserType;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.tabs.SampleFragmentPagerAdapter;
import com.example.anna.shedule.tabs.SlidingTabLayout;
import com.example.anna.shedule.utils.ContextUtils;
import com.github.clans.fab.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends BaseActivity {
    
    // Declaring Your View and Variables

    public String[] Titles= new String[] {"", "", "", "", "", ""};
    private UserService user_service;
    User main_activity_user;
    ViewPager viewPager;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextUtils.setContext(getApplicationContext());
        user_service = Services.getService(UserService.class);
        main_activity_user = user_service.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (main_activity_user.getType() == UserType.CLASS_LEADER) {
            fab.setVisibility(View.VISIBLE);
        }


        // Creating The Toolbar and setting it as the Toolbar for the activity

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        SlidingTabLayout slidingTabLayout2 = (SlidingTabLayout) findViewById(R.id.tabs);

        // Set custom tab layout
        c = Calendar.getInstance();
        int date = c.get(Calendar.DAY_OF_WEEK)-2;
        slidingTabLayout.setCustomTabView(R.layout.custom_tab_view, 0);
        // Center the tabs in the layout
        slidingTabLayout.scrollToTab(4, 0);
        slidingTabLayout.setDistributeEvenly(true);
        // Customize tab color
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        slidingTabLayout.setViewPager(viewPager);

        slidingTabLayout2.setCustomTabView(R.layout.custom_tab_view, 0);
        // Center the tabs in the layout
        slidingTabLayout2.scrollToTab(4, 0);
        slidingTabLayout2.setDistributeEvenly(true);
        // Customize tab color
        slidingTabLayout2.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        slidingTabLayout2.setViewPager(viewPager);

        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void goToday() {
        viewPager.setCurrentItem(c.get(Calendar.DAY_OF_WEEK)-2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.today:
                goToday();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

