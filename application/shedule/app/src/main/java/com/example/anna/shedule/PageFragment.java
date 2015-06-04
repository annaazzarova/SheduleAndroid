package com.example.anna.shedule;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.anna.shedule.lessons_list.LessonAdapter;
import com.example.anna.shedule.lessons_list.LessonData;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by Anna on 31.05.2015.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    ListView lv;
    Context context;
    public ArrayList<LessonData> lessons = new ArrayList<LessonData>();
    SharedPreferences sPref;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        lessons.clear();
        for (int i = 0; i <= mPage; i++) {
            lessons.add(new LessonData(i+1, "Парадигмы" + String.valueOf(i), "ПС-33", "лекция", "I/355", "blue", 4));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_1, container, false);
        context=getActivity();

        boolean isSuper = true;
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        if (!isSuper) {
            fab.setVisibility(View.GONE);
        }
        lv=(ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new LessonAdapter(getActivity(), lessons));
        return view;
    }
}