package com.example.anna.shedule.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anna.shedule.activities.menu.BaseActivity;
import com.example.anna.shedule.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Data;

public class NotesListActivity extends BaseActivity {

    private String classLeader;
    private String[] months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list_activity);
        ListView mNotesList = (ListView) findViewById(R.id.notes_list);

        classLeader = getString(R.string.class_leader);
        months = getResources().getStringArray(R.array.months);

        //fix footer and header divider
        Context context = getApplicationContext();
        mNotesList.addFooterView(new View(context), null, true);
        mNotesList.addHeaderView(new View(context), null, true);

        // specify an adapter (see also next example)
        List<Note> texts = new ArrayList<>();
        texts.add(new Note("Нехорошкова Л.Г.", "Пара Теория управления (13:30) 27 мая отменена.", new Date(System.currentTimeMillis() - 1000 * 60 * 24 * 60 * 2), true));
        texts.add(new Note("Нехорошкова Л.Г.", "Пара ПП (11:30) 15 марта отменена.", new Date(), false));
        texts.add(new Note("Нехорошкова Л.Г.", "Пара Теория управления (13:30) 27 мая отменена.", new Date(), true));
        texts.add(new Note("Нехорошкова Л.Г.", "Пара ПП (11:30) 15 марта отменена мая отменена.", new Date(System.currentTimeMillis() - 1000 * 60 * 24 * 60 * 2), true));
        texts.add(new Note("Нехорошкова Л.Г.", "Пара Теория управления (13:30) 27 мая отменена.", new Date(System.currentTimeMillis() - 1000 * 60 * 24 * 60 * 2), true));
        texts.add(new Note("Нехорошкова Л.Г.", "Пара ПП (11:30) 15 марта отменена мая отменена.", new Date(), true));
        texts.add(new Note("Нехорошкова Л.Г.", "Пара Теория управления (13:30) 27 мая отменена.", new Date(), false));
        texts.add(new Note("Нехорошкова Л.Г.", "Пара ПП (11:30) 15 марта отменена мая отменена.", new Date(), false));


        MyAdapter mAdapter = new MyAdapter(getApplicationContext(), texts);
        mNotesList.setAdapter(mAdapter);
    }

    @Data
    public class NoteViewHolder {

        private TextView icon;
        private TextView initiator;
        private TextView date;
        private TextView text;

        public NoteViewHolder(View view) {
            this.icon = (TextView) view.findViewById(R.id.text_icon);
            this.initiator = (TextView) view.findViewById(R.id.initiator_name);
            this.date = (TextView) view.findViewById(R.id.create_time);
            this.text = (TextView) view.findViewById(R.id.note_text);
        }

        public void setValues(Note note) {
            String owner = getOwner(note);
            initiator.setText(owner);
            date.setText(generateCreateDateText(note.getDate()));
            text.setText(note.getText());

            int iconBackgroundColor = (note.isTeacher())
                    ? R.color.teacher_icon_background
                    : R.color.class_leader_icon_background;
            icon.setText(String.valueOf(getFirstLetter(owner)));
            icon.setBackgroundResource(iconBackgroundColor);
        }

        private long getStartOfCurrentDay() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        }

        private String generateCreateDateText(Date date) {
            if (date == null) return "";
            long startOfCurrentDay = getStartOfCurrentDay();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String dateText;
            if (date.getTime() > startOfCurrentDay) {
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                dateText = hours + ":";
                if (minutes < 10) {
                    dateText += "0";
                }
                dateText += minutes;
            } else {
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                dateText = day + " " + months[month];
            }
            return dateText;
        }

        private char getFirstLetter(String str) {
            char firstCh = (str.isEmpty()) ? ' ' : str.charAt(0);
            return Character.isLetter(firstCh) || Character.isDigit(firstCh) ? firstCh : ' ';

        }

        private String getOwner(Note note) {
            String owner = (note.isTeacher())
                    ? note.getOwner()
                    : classLeader;
            return owner == null ? "" : owner;
        }
    }

    private class MyAdapter extends ArrayAdapter<Note> {

        private List<Note> values;
        Context context;

        public MyAdapter(Context context, List<Note> objects) {
            super(context, R.layout.notes_list_activity, objects);
            this.values = objects;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getViewWithHolder(convertView, parent);
            NoteViewHolder  viewHolder = (NoteViewHolder ) view.getTag();
            Note note = values.get(position);
            viewHolder.setValues(note);
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
            return inflater.inflate(R.layout.notes_item_layout, parent, false);
        }

        private NoteViewHolder initializeHolder(View view) {
            NoteViewHolder holder = new NoteViewHolder(view);
            view.setTag(holder);
            return holder;
        }

        @Override
        public int getCount() {
            return values.size();
        }
    }

    @Data
    private class Note {
        private String owner;
        private String text;
        private Date date;
        private boolean teacher;

        public Note(String owner, String text, Date date, boolean teacher) {
            this.owner = owner;
            this.text = text;
            this.date = date;
            this.teacher = teacher;
        }

    }
}
