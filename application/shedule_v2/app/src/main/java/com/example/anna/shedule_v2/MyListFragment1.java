package com.example.anna.shedule_v2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MyListFragment1 extends ListFragment implements DayList.OnFragmentInteractionListener{

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

        ListAdapter myListAdapter = new ArrayAdapter<String>(
            getActivity(),
            R.layout.list_item,
            R.id.label,
            day_of_week);
        setListAdapter(myListAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listfragment, container, false);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        Toast.makeText(
                getActivity(),
                getListView().getItemAtPosition(position).toString(),
                Toast.LENGTH_LONG).show();
        DayList youFragment = new DayList();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment2, youFragment)
                .addToBackStack("myStack")
                .commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}