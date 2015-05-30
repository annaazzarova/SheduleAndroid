package com.example.anna.shedule.activities.menu;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class NavDrawerItem {

    private String title;
    private int icon;
    private boolean isSelected;

    public NavDrawerItem(String title, int icon, boolean isSelected) {
        this.title = title;
        this.icon = icon;
        this.isSelected = isSelected;
    }

}