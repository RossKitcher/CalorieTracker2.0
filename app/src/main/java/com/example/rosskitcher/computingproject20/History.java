package com.example.rosskitcher.computingproject20;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class History extends Fragment {

    private static final String TAG = "History";
    private SharedPreferences dates;

    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_history, container, false);
        getActivity().setTitle("History");


        dates = this.getActivity().getSharedPreferences("dates", 0);

        // get listview
        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        HashMap<String, List<String>> expandableListDetail = getData(); // calls method to get HashMap
        List<String> expandableListTitle = new ArrayList<String>(expandableListDetail.keySet()); // get list of all keys

        // creates instance of ExpandableListAdapter
        ExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(getContext(),
                expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter); // set adapter

        return view;
    }

    // returns HashMap of data
    public HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> data = new LinkedHashMap<>();

        // retrieves amount of items that have been added
        int amountOfDates = dates.getInt("amountOfDates", 0);

        String itemName;
        String itemCalories;


        int y;
        // if any dates have actually been saved
        if (amountOfDates != 0) {
            for (y = 1; y <= amountOfDates; y++) { // iterates once for each date
                List<String> today = new ArrayList<String>();
                int calorieCount = 0;
                String currentDate = dates.getString(String.valueOf(y), ""); // get date corresponding to y value

                // get shared preferences using date retrieved
                SharedPreferences history = this.getActivity().getSharedPreferences(currentDate, 0);
                int amountOfItems = history.getInt("amountOfItems", 0); // get amount of items added

                // if more than one item has been added...
                if (amountOfItems != 0) {

                    int x;
                    // iterate through each item and save it to an ArrayList
                    for (x = 1; x <= amountOfItems; x++) {

                        // get Set of item name and caloric count
                        Set foodSet = (history.getStringSet(Integer.toString(x), null));
                        if (foodSet == null) {} // avoids possible nullpointerexcpetion
                        else {
                            // convert set to array
                            String[] checkOrder = (String[]) foodSet.toArray(new String[foodSet.size()]);

                            // orders contents by finding which value is a number.
                            // number is henceforth the caloric count
                            try {
                                Integer.parseInt(checkOrder[0]);
                                itemCalories = checkOrder[0];
                                itemName = checkOrder[1];
                            } catch (NumberFormatException e) {
                                itemName = checkOrder[0];
                                itemCalories = checkOrder[1];
                            }
                            calorieCount += Integer.parseInt(itemCalories); // calculate total calorie count
                            today.add(itemName + ";     " + itemCalories + " (calories)"); // add to list
                        }
                    }
                    currentDate = currentDate.replaceAll("\\.", "/"); // format dates

                    // put key, value pair into HashMap
                    data.put(currentDate + "       " + (calorieCount + " (calories consumed)"), today);
                }
            }
        } else {
            Toast.makeText(getActivity(), "No saved data!", Toast.LENGTH_LONG).show();

        }

        return data;
    }

}
