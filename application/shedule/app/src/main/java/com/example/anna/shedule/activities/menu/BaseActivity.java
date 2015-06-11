package com.example.anna.shedule.activities.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.example.anna.shedule.MainActivity;
import com.example.anna.shedule.R;
import com.example.anna.shedule.SettingsActivity;
import com.example.anna.shedule.activities.CreateNoteLayout;
import com.example.anna.shedule.activities.NotesListActivity;
import com.example.anna.shedule.application.note.service.NoteService;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLeft;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private Integer selectedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedPos = determineSelectedPos();
        setContentView(R.layout.base_activity);
        moveDrawerToTop();
        initActionBar() ;

        String[] navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        navDrawerItems = initDrawerItems(navMenuTitles, navMenuIcons);
        initDrawer();
    }

    private Integer determineSelectedPos() {
        int i = 0;
        int pos = -1;
        Class<?> activity;
        while((activity = getActivityByMenuPos(i)) != null) {
            if (this.getClass().equals(activity)) {
                pos = i;
                break;
            }
            i++;
        }
        return pos;
    }

    public ArrayList<NavDrawerItem> initDrawerItems(String[] navMenuTitles, TypedArray navMenuIcons) {
        ArrayList<NavDrawerItem> items = new ArrayList<NavDrawerItem>();
        for (int i = 0; i < navMenuTitles.length; i++) {
            NavDrawerItem item = new NavDrawerItem(
                    navMenuTitles[i],
                    navMenuIcons.getResourceId(i, -1),
                    selectedPos == i);
            items.add(item);
        }
        return items;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void moveDrawerToTop() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.drawer_layout, null); // "null" is important.
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) drawer.findViewById(R.id.content_frame);
        container.addView(child, 0);

        View menuLayout = drawer.findViewById(R.id.left_drawer);
        menuLayout.setPadding(0, getStatusBarHeight(), 0, 0);
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}// catch click on empty field
        });

        // Make the drawer replace the first child
        decor.addView(drawer);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        mDrawerToggle.syncState();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mDrawerLeft = (LinearLayout) findViewById(R.id.left_drawer);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        mDrawerLeft.setMinimumWidth(width-getStatusBarHeight());


        ListView mDrawerList = (ListView)findViewById(R.id.drawer_list);
        mDrawerLayout.setDrawerListener(createDrawerToggle());
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private DrawerLayout.DrawerListener createDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int state) {
            }
        };
        return mDrawerToggle;
    }


    private Class<?> getActivityByMenuPos(int position) {
        switch (position) {
            case 0:
                return MainActivity.class;
            case 1:
                return NotesListActivity.class;
            case 2:
                return SettingsActivity.class;
            case 3:
                return null;
            default:
                return null;
        }
    }

    private void displayView(int position) {
        if (position != selectedPos) {
            Class<?> selectedActivity = getActivityByMenuPos(position);
            goToActivity(selectedActivity);
        }
        // update selected item and title, then close the drawer
        mDrawerLayout.closeDrawer(mDrawerLeft);
    }

    public void goToActivity(Class activityClass){
        if (activityClass != null) {
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}

