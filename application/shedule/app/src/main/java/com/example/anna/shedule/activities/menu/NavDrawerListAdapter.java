package com.example.anna.shedule.activities.menu;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anna.shedule.R;

import java.util.ArrayList;

import lombok.Data;

class NavDrawerListAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<NavDrawerItem> mNavDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        super(context, R.layout.drawer_list_item, navDrawerItems);
        this.mContext = context;
        this.mNavDrawerItems = navDrawerItems;
    }

    @Data
    private class DrawerItemViewHolder {
        private TextView title;
        private ImageView icon;

        public DrawerItemViewHolder(View view) {
            this.title =(TextView) view.findViewById(R.id.drawer_item_title);
            this.icon = (ImageView) view.findViewById(R.id.drawer_item_icon);
        }

        public void setDrawerItemValues(NavDrawerItem item) {
            title.setText(item.getTitle());
            icon.setImageResource(item.getIcon());
            Resources res = getContext().getResources();
            if (item.isSelected()) {
                int selectedItemColor = res.getColor(R.color.selected_menu_item);
                icon.setColorFilter(selectedItemColor);
                title.setTextColor(selectedItemColor);
            } else {
                icon.setColorFilter(res.getColor(R.color.menu_icon_color));
                title.setTextColor(res.getColor(android.R.color.black));
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getViewWithHolder(convertView, parent);
        DrawerItemViewHolder viewHolder = (DrawerItemViewHolder) view.getTag();
        NavDrawerItem item = mNavDrawerItems.get(position);
        viewHolder.setDrawerItemValues(item);
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
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.drawer_list_item, parent, false);
    }

    private DrawerItemViewHolder initializeHolder(View view) {
        DrawerItemViewHolder holder = new DrawerItemViewHolder(view);
        view.setTag(holder);
        return holder;
    }

    @Override
    public int getCount() {
        return mNavDrawerItems.size();
    }
}

