package com.example.anna.shedule_v2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyListFragment1 extends ListFragment implements
        DayList.OnFragmentInteractionListener{

    String[] day_of_week ={
            "ПН",
            "ВТ",
            "СР",
            "ЧТ",
            "ПТ",
            "СБ"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getActivity(), day_of_week);
        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listfragment, container, false);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        for (int a = 0; a < l.getChildCount(); a++) {
            if ( a == position ) {
                l.getChildAt(a).findViewById(R.id.label).setBackgroundResource(R.drawable.myrect_active);
            }
            else {
                l.getChildAt(a).findViewById(R.id.label).setBackgroundResource(R.drawable.myrect_default);
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

        android.app.FragmentManager myFragmentManager = getFragmentManager();

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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}