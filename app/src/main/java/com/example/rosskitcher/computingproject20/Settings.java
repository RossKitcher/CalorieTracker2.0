package com.example.rosskitcher.computingproject20;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;



public class Settings extends Fragment {

    private static final String TAG = "Settings";

    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        getActivity().setTitle("Settings"); // set title

        Button clearButton = view.findViewById(R.id.clearData);
        //when clear data is clicked
        clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "'Clear Data' button pressed");

                    // remove all items from sharedpreferences
                    SharedPreferences preferences = getActivity().getSharedPreferences("foodItems", 0);
                    preferences.edit().clear().apply();
                    preferences.edit().remove("amount").apply();
                    preferences = getActivity().getSharedPreferences("calories", 0);
                    preferences.edit().putInt("tracker", 2500).apply();
                    Toast.makeText(getActivity(), "Data cleared", Toast.LENGTH_SHORT).show(); // notify user that it is cleared
            }
        });



        Button clearHistory = view.findViewById(R.id.clearHistory);
        // when clear history is clicked
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "'Clear History' button pressed");

                // remove all sharedpreferences by getting amount of dates, then iterating from 1 to amountOfDates
                SharedPreferences dates = getActivity().getSharedPreferences("dates", 0);
                int amountOfDates = dates.getInt("amountOfDates", 0);
                int x;
                if (amountOfDates != 0) {
                    for (x = 1; x <= amountOfDates; x++) {
                        // every date is removed
                        String item = dates.getString(String.valueOf(x), null);
                        SharedPreferences history = getActivity().getSharedPreferences(item, 0);
                        history.edit().clear().apply();
                    }
                }
                dates.edit().clear().apply();
                dates.edit().remove("amountOfDates").apply();
                Toast.makeText(getActivity(), "History cleared", Toast.LENGTH_SHORT).show();


            }
        });

        Button openCalc = (Button) view.findViewById(R.id.openCalc);
        // when open calculator is clicked
        openCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "'Calorie Goal' button pressed");

                // opens new activity
                Intent intent = new Intent(getActivity(), CalorieCalculator.class);
                startActivity(intent);
            }
        });


        return view;
    }

}
