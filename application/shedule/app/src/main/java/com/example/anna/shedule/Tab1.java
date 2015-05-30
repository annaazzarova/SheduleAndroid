package com.example.anna.shedule;

/**
 * Created by Anna on 28.05.2015.
 */
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab1 extends Fragment {

    ListView lv;
    Context context;
    ArrayList<LessonData> lessons = new ArrayList<LessonData>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.tab_1,container,false);

        for (int i = 1; i <= 6; i++) {
            lessons.add(new LessonData(i, "Парадигмы2", "ПС-33", "лекция", "I 355"));
        }
        context=getActivity();

        lv=(ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new LessonAdapter(getActivity(), lessons));


        return view;
    }
}