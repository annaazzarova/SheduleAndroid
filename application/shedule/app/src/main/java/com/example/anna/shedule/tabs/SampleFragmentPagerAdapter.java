package com.example.anna.shedule.tabs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.anna.shedule.PageFragment;

/**
 * Created by Anna on 31.05.2015.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 6;
    private String tabTitles[] = new String[] {"", "", "", "", "", ""};
    private String titles[] = new String[]{};
    private Context context;


    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, String Titles[]) {
        super(fm);
        this.titles = Titles;
        this.tabTitles = Titles;
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        PageFragment pg = new PageFragment().newInstance(Integer.parseInt(titles[position+1],10));
        return pg;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}