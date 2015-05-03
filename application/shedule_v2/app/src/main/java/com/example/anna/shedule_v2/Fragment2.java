package com.example.anna.shedule_v2;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;

public class Fragment2 extends Fragment {


    String getStringFromAssetFile()
    {
        AssetManager am = getActivity().getAssets();
        InputStream is = am.open("test.txt");
        String s = is.toString();
        is.close();
        return s;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getActivity().getAssets().open("files/3.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.fragment2, container, false);
        TextView output = (TextView) view.findViewById(R.id.textView);

        String strJson = "{ \"Employee\" :[{\"id\":\"101\",\"name\":\"Sonoo Jaiswal\",\"salary\":\"50000\"},{\"id\":\"102\",\"name\":\"Vimal Jaiswal\",\"salary\":\"60000\"}] }";

        String data = "";

        try {
            JSONObject obj1 = new JSONObject(loadJSONFromAsset());
            String s = "123" + obj1;
            output.setText(s);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            // Create the root JSONObject from the JSON string.
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("Employee");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.optString("id").toString());
                String name = jsonObject.optString("name").toString();
                float salary = Float.parseFloat(jsonObject.optString("salary").toString());

                data += "Node" + i + " : \n id= " + id + " \n Name= " + name + " \n Salary= " + salary + " \n ";
            }
            //output.setText(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}