package com.example.anna.shedule.lessons_list;

/**
 * Created by Anna on 29.05.2015.
 */
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.anna.shedule.application.note.service.NoteService;
import com.example.anna.shedule.application.schedule.model.Lesson;
import com.example.anna.shedule.application.schedule.model.helper.LessonStatus;
import com.example.anna.shedule.application.schedule.model.helper.LessonType;
import com.example.anna.shedule.application.schedule.service.ScheduleService;
import com.example.anna.shedule.application.services.Services;
import com.example.anna.shedule.application.user.model.User;
import com.example.anna.shedule.application.user.model.UserType;
import com.example.anna.shedule.application.user.service.UserService;
import com.github.clans.fab.FloatingActionButton;
import com.example.anna.shedule.activities.CreateNoteLayout;
import com.example.anna.shedule.application.note.service.NoteService;

import java.util.ArrayList;
import java.util.List;

public class LessonAdapter extends BaseAdapter{
    Context context;

    List<Lesson> lessons;

    private UserService user_service;
    User main_activity_user;
    public class LessonHolder {

    }

    private static LayoutInflater inflater=null;
    public LessonAdapter(FragmentActivity mainActivity, List<Lesson> lessons1) {
        // TODO Auto-generated constructor stub
        lessons = lessons1;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lessons.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final View rowView;
        rowView = inflater.inflate(R.layout.card_layout, null);
        user_service = Services.getService(UserService.class);
        main_activity_user = user_service.getCurrentUser();

        View canceled =  rowView.findViewById(R.id.label_cancel);
        if (lessons.get(position).getStatus() == LessonStatus.CANCELED) {
            canceled.setVisibility(View.VISIBLE);
        }
        View hasNotes =  rowView.findViewById(R.id.ic_notes);
        if (lessons.get(position).hasNotes()) {
            hasNotes.setVisibility(View.VISIBLE);
        }
        TextView tw_lesson = (TextView) rowView.findViewById(R.id.textLesson);
        TextView tw_group = (TextView) rowView.findViewById(R.id.textGroup);
        TextView tw_type = (TextView) rowView.findViewById(R.id.textType);
        TextView tw_start = (TextView) rowView.findViewById(R.id.startTime);
        TextView tw_end = (TextView) rowView.findViewById(R.id.endTime);
        tw_start.setText(lessons.get(position).getTime().getStartTime());
        tw_end.setText(lessons.get(position).getTime().getEndTime());

        if (lessons.get(position).getType() == LessonType.LECTURE){
            tw_type.setText("Лекция");
        }
        else {
            tw_type.setText("Практика");
        }
        tw_lesson.setText(lessons.get(position).getTitle());

        if (main_activity_user.getType() == UserType.TEACHER) {
            tw_group.setText(lessons.get(position).getHull() + " " + lessons.get(position).getAuditory() + " / " + lessons.get(position).getGroupsAsString());
        }
        else {
            tw_group.setText(lessons.get(position).getHull() + " " + lessons.get(position).getAuditory() + " / " + lessons.get(position).getTeacherName());
        }
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(arg0.getContext());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_layout);
                dialog.findViewById(R.id.add_note_button).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CreateNoteLayout.class);
                        intent.putExtra("changeId", ""); //TODO TO DO DO DO
                        intent.putExtra("lessonId", "");
                        intent.putExtra("startOfDay", (long) 0);
                        dialog.cancel();
                        context.startActivity(intent);
                    }
                });

                dialog.findViewById(R.id.edit_button).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, com.example.anna.shedule.activities.activity_lesson_details.class);
                        intent.putExtra("changeId", ""); //TODO TO DO DO DO
                        intent.putExtra("lessonId", "");
                        intent.putExtra("startOfDay", (long) 0);
                        dialog.cancel();
                        context.startActivity(intent);
                    }
                });
                dialog.show();
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked", Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}