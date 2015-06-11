package com.example.anna.shedule.tabs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.CalendarView;

import com.example.anna.shedule.PageFragment;

import java.util.Calendar;

/**
 * Created by Anna on 31.05.2015.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 6;
    private String tabTitles[] = new String[] {"", "", "", "", "", ""};
    private String Titles[] = new String[]{"", "", "", "", "", ""};
    private Context context;


    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        int firstdate = date - day_of_week +1;
        for (int i = 0; i != 6; ++i){
            Titles[i] = String.valueOf(firstdate++);
        }
        this.tabTitles = Titles;
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        PageFragment pg = new PageFragment().newInstance(Integer.parseInt(Titles[position],10)-1);
        return pg;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}