package com.example.anna.shedule;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.anna.shedule.application.schedule.model.Teacher;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Nikita on 10.06.2015.
 */
public class TeacherListAdapter extends ArrayAdapter<Teacher> {
    private final Activity context;
    private final List<Teacher> values;

    public TeacherListAdapter(Activity context, List<Teacher> values) {
        super(context, R.layout.teacher_list_element, values);
        this.values = values;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getViewWithHolder(convertView, parent);
        TeacherViewHolder  viewHolder = (TeacherViewHolder) view.getTag();
        Teacher teacher = values.get(position);
        viewHolder.setValues(teacher);
        return view;
    }

    private View getViewWithHolder(View convertView, ViewGroup parent) {
        if (convertView == null) {
            View view = createNewView(parent);
            initializeHolder(view);
            return view;
        } else {
            return convertView;
        }
    }

    private View createNewView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.teacher_list_element, parent, false);
    }

    private TeacherViewHolder initializeHolder(View view) {
        TeacherViewHolder holder = new TeacherViewHolder(view);
        view.setTag(holder);
        return holder;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Data
    @NoArgsConstructor
    private static class TeacherViewHolder {
        private TextView txtTitle;
        private TextView firstChar;

        public TeacherViewHolder(View view) {
            this.txtTitle = (TextView) view.findViewById(R.id.teacher_name);
            this.firstChar = (TextView) view.findViewById(R.id.teacher_icon);
        }

        public void setValues(Teacher teacher) {
            txtTitle.setText(teacher.getName());
            firstChar.setText(getFirstLetter(teacher));
        }

        private static String getFirstLetter(Teacher teacher) {
            String name = teacher.getName();
            return "" + name.charAt(0);
        }
    }
}