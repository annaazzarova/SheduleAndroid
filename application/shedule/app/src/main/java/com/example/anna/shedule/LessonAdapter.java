package com.example.anna.shedule;

/**
 * Created by Anna on 29.05.2015.
 */
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LessonAdapter extends BaseAdapter{
    Context context;

    ArrayList<LessonData> objects;
    private static LayoutInflater inflater=null;
    public LessonAdapter(FragmentActivity mainActivity, ArrayList<LessonData> lessons) {
        // TODO Auto-generated constructor stub
        objects = lessons;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    LessonData getLesson(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LessonData les = getLesson(position);
        View rowView;
        rowView = inflater.inflate(R.layout.card_layout, null);
        TextView tw_lesson = (TextView) rowView.findViewById(R.id.textLesson);
        TextView tw_group = (TextView) rowView.findViewById(R.id.textGroup);
        tw_lesson.setText(les.getLesson());
        tw_group.setText(les.getGroup());
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked", Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}