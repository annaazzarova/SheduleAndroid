package com.example.anna.shedule;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Anna on 07.05.2015.
 */
public class CustomCard extends Card {

    protected TextView mTitle;
    protected TextView mSecondaryTitle;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public CustomCard(Context context) {
        this(context, R.layout.card_inner_content);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public CustomCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

        //No Header

        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
            }
        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        mTitle = (TextView) parent.findViewById(R.id.textView_name);
        mSecondaryTitle = (TextView) parent.findViewById(R.id.textView_group);


        mSecondaryTitle.setClickable(true);
        if (mTitle!=null);
            //mTitle.setText("123");

        if (mSecondaryTitle!=null);
            //mSecondaryTitle.setText("456");
}}