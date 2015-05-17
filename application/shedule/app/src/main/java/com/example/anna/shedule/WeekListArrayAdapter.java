package com.example.anna.shedule;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WeekListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public WeekListArrayAdapter(Context context, String[] values) {
        super(context, R.layout.left_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.left_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(values[position]);
        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Open_Sans_Condensed/OpenSans-CondBold.ttf"));

        String s = values[position];

        return rowView;
    }
}