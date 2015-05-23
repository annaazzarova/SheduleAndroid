package com.example.anna.shedule;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardExpandableListAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import it.gmariotti.cardslib.library.view.CardView;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DayList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DayList extends Fragment {

    String[] names = {
            "Русский",
            "Математика",
            "ООП",
            "Проектирование игр",
            "Управление программными продуктами"
    };

    String[] teachers = {
            "Иванова А.А.",
            "Журавлева И.В",
            "Малов А.Н",
            "Морозов М.Н",
            "Нехорошкова Л.Г"
    };

    String[] places = {
            "450 III",
            "521 I",
            "333 IV",
            "429 III",
            "112 II"
    };

    int[] numbers = {
            2,
            3,
            4,
            5,
            6
    };

    private static String LOG_TAG = "CardViewActivity";
    private OnFragmentInteractionListener mListener;

    public DayList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.right_recycle_list, container, false);

        Bundle bundle = this.getArguments();
        int length = bundle.getInt("someInt");

        ArrayList<Card> cards = new ArrayList<Card>();

        for (int i = 0; i < length; i++) {
            // Create a Card
            //Create a Card
            Card card = new CustomCard(getActivity());
            // Create a CardHeader
            //CardHeader header = new CardHeader(getActivity());
            // Add Header to card
            //header.setTitle("Управление программными проектами");
            //header.setButtonExpandVisible(true);

            //card.addCardHeader(header);
            //This provide a simple (and useless) expand area
            CustomExpandCard expand = new CustomExpandCard(getActivity());
            //Add expand to card
            card.addCardExpand(expand);
            card.setCardElevation(0);

            ViewToClickToExpand viewToClickToExpand =
                    ViewToClickToExpand.builder()
                            .highlightView(true)
                            .setupCardElement(ViewToClickToExpand.CardElementUI.CARD);
            card.setViewToClickToExpand(viewToClickToExpand);

            cards.add(card);
        }


        CardArrayRecyclerViewAdapter mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getActivity(), cards);

        //Staggered grid view
        CardRecyclerView mRecyclerView = (CardRecyclerView) rootView.findViewById(R.id.carddemo_recyclerview);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Set the empty view
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
