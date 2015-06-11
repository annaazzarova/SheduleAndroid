package com.example.anna.shedule;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Nikita on 10.06.2015.
 */
public class TeacherListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] names;

    public TeacherListAdapter(Activity context, String[] names ) {
        super(context, R.layout.teacher_list_element, names);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.names = names;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.teacher_list_element, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.name);
        TextView firstChar = (TextView) rowView.findViewById(R.id.nameChar);

        txtTitle.setText(names[position]);
        firstChar.setText("" + names[position].charAt(0));
        return rowView;
    }
}