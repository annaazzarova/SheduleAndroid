package com.example.anna.shedule;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created by Anna on 07.05.2015.
 */
public class CustomExpandCard extends CardExpand {

    //Use your resource ID for your inner layout
    public CustomExpandCard(Context context) {
        super(context, R.layout.card_expand_content);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;
        ImageButton bt1 = (ImageButton) view.findViewById(R.id.imageButton);
        ImageButton bt2 = (ImageButton) view.findViewById(R.id.imageButton2);
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "click", Toast.LENGTH_LONG).show();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getContext(), "click", Toast.LENGTH_LONG).show();
            }
        });
    }
}