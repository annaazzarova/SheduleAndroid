package com.example.anna.shedule.lessons_list;

/**
 * Created by Anna on 29.05.2015.
 */
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anna.shedule.R;

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
        final View rowView;
        rowView = inflater.inflate(R.layout.card_layout, null);
        TextView tw_lesson = (TextView) rowView.findViewById(R.id.textLesson);
        TextView tw_group = (TextView) rowView.findViewById(R.id.textGroup);
        TextView tw_type = (TextView) rowView.findViewById(R.id.textType);
        TextView tw_start = (TextView) rowView.findViewById(R.id.startTime);
        TextView tw_end = (TextView) rowView.findViewById(R.id.endTime);
        tw_start.setText(les.getStart());
        tw_end.setText(les.getEnd());
        tw_type.setText(les.getType());
        tw_lesson.setText(les.getLesson());
        tw_group.setText(les.getGroup());
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(arg0.getContext());
                dialog.setContentView(R.layout.dialog_layout);
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialogButton.setOnClickListener(new OnClickListener() {
                //    @Override
                //    public void onClick(View v) {
                //        dialog.dismiss();
                //    }
                //});

                dialog.show();
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked", Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}