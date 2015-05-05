package com.example.anna.shedule_v2;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        TextView textView_name;
        TextView textView_teacher;
        TextView textView_place;
        TextView textView_start_time;
        TextView textView_end_time;

        public DataObjectHolder(View itemView) {
            super(itemView);
            textView_name = (TextView) itemView.findViewById(R.id.textView_name);
            textView_teacher = (TextView) itemView.findViewById(R.id.textView_teacher);
            textView_place = (TextView) itemView.findViewById(R.id.textView_place);
            textView_start_time = (TextView) itemView.findViewById(R.id.textView_start_time);
            textView_end_time = (TextView) itemView.findViewById(R.id.textView_end_time);
            Log.i(LOG_TAG, "Adding Listener");

            Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Open_Sans_Condensed/OpenSans-CondBold.ttf");
            this.textView_name.setTypeface(font);

            Typeface font2 = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Open_Sans_Condensed/OpenSans-CondLight.ttf");
            this.textView_teacher.setTypeface(font2);
            this.textView_place.setTypeface(font2);
            this.textView_start_time.setTypeface(font2);
            this.textView_end_time.setTypeface(font2);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<DataObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_blank, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.textView_name.setText(mDataset.get(position).getM_name());
        holder.textView_teacher.setText(mDataset.get(position).getM_teacher());
        holder.textView_place.setText(mDataset.get(position).getM_place());
        holder.textView_start_time.setText(mDataset.get(position).getM_date_start_time());
        holder.textView_end_time.setText(mDataset.get(position).getM_date_end_time());
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}