package com.example.anna.shedule;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WeekList extends ListFragment {

    String[] day_of_week ={
            "П",
            "В",
            "С",
            "Ч",
            "П",
            "С"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeekListArrayAdapter adapter = new WeekListArrayAdapter(getActivity(), day_of_week);
        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_column, container, false);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        for (int a = 0; a < l.getChildCount(); a++) {
            if ( a == position ) {
                l.getChildAt(a).findViewById(R.id.label).setBackgroundResource(R.drawable.list_item_active);
                TextView tw = (TextView) l.getChildAt(a).findViewById(R.id.label);
                tw.setTextColor(R.color.color_list_item_active_text);
            }
            else {
                l.getChildAt(a).findViewById(R.id.label).setBackgroundResource(R.drawable.list_item_inactive);
            }
        }

        int lessons = 1;
        if (position == 0) {
            lessons = 2;
        }
        if (position == 1) {
            lessons = 3;
        }
        if (position == 2) {
            lessons = 1;
        }
        if (position == 3) {
            lessons = 2;
        }
        if (position == 4) {
            lessons = 5;
        }
        if (position == 5) {
            lessons = 2;
        }
        if (position == 6) {
            lessons = 0;
        }

        FragmentManager myFragmentManager = getFragmentManager();

        DayList fragment = new DayList();

        Bundle bundle = new Bundle();
        bundle.putInt("someInt", lessons);

        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = myFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment2, fragment);
        fragmentTransaction.commit();


        Toast.makeText(
                getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
    }

}