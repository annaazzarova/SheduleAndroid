package com.example.anna.shedule;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.anna.shedule.application.note.service.NoteService;
import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.application.schedule.service.ScheduleService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.model.UserType;
import com.example.anna.shedule.application.user.service.UserService;
import com.example.anna.shedule.lessons_list.LessonAdapter;
import com.example.anna.shedule.lessons_list.LessonData;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anna on 31.05.2015.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    ListView lv;
    Context context;
    SharedPreferences sPref;
    private UserService user_service;
    User main_activity_user;

    public List<Lesson> lessons;
    final ScheduleService scheduleService = Services.getService(ScheduleService.class);
    final NoteService noteService = Services.getService(NoteService.class);


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
        lessons = scheduleService.getSchedule(2015, 5, mPage+1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_1, container, false);
        context=getActivity();
        user_service = Services.getService(UserService.class);
        main_activity_user = user_service.getCurrentUser();


        lv=(ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new LessonAdapter(getActivity(), lessons));
        return view;
    }
}