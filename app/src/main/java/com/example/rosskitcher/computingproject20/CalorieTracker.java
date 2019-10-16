package com.example.rosskitcher.computingproject20;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CalorieTracker extends Fragment {

    // variable declarations
    private SharedPreferences calorieGoal;
    private SharedPreferences foodData;
    private SharedPreferences counter;
    private TableLayout tableLayout;
    private SharedPreferences dates;
    private TextView tracker;
    private TextView textCalorie;
    private static final String TAG = "CalorieTracker";

    public CalorieTracker() {
        // Required empty public constructor
    }


    // Creates and returns the view hierarchy associated with this fragment
    @Override // Tells the compiler that this method will be an override of a superclass method
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Calorie Tracker");
        // Inflate the layout for this fragment
        ConstraintLayout view = (ConstraintLayout)inflater.inflate(R.layout.fragment_calorie_tracker, container, false);

        // get both shared preferences
        calorieGoal = this.getActivity().getSharedPreferences("calorieGoal", 0);
        foodData = this.getActivity().getSharedPreferences("foodItems", 0);
        counter = this.getActivity().getSharedPreferences("calories", 0);
        dates = this.getActivity().getSharedPreferences("dates", 0);


        Button searchButton = (Button) view.findViewById(R.id.searchFood);
        // OnClickListener will detect when the button is pressed and run the indented code when this event happens
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button 'SEARCH' pressed");

                // Starts new activity, SearchFood
                Intent intent = new Intent(getActivity(), SearchFood.class);
                startActivity(intent);
            }
        });

        Button scanButton = (Button) view.findViewById(R.id.openScanner);
        // Launches scanning activity
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button 'SCAN' pressed");
                IntentIntegrator integrator = new IntentIntegrator(getActivity()); // Instantiates the class IntentIntegrator
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES); // Only allows UPC codes
                integrator.setPrompt("Scan a barcode"); // Sets text prompt
                integrator.setCameraId(0); // Use a specific camera of the device
                integrator.setBeepEnabled(false); // Disables sound effect
                integrator.initiateScan(); // Opens scanner
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.saveToday);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = dates.getInt("amountOfDates", 0);

                Log.i(TAG, "Button 'SAVE' pressed");
                Calendar cal = Calendar.getInstance();
                Integer yy = cal.get(Calendar.YEAR);
                Integer mm = cal.get(Calendar.MONTH);
                Integer dd = cal.get(Calendar.DAY_OF_MONTH);
                String todayDate = Integer.toString(dd) + "." + Integer.toString(mm + 1) + "." + Integer.toString(yy);
                Log.i(TAG, "Today's date: " + todayDate);
                amount += 1;
                dates.edit().putString(String.valueOf(amount), todayDate).apply();
                dates.edit().putInt("amountOfDates", amount).apply();
                Log.i("Activity", "Before loop: " + String.valueOf(amount));
                saveItems(todayDate);


            }
        });



        //initialise table layout
        tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);
        // initialise textviews
        textCalorie = (TextView) view.findViewById(R.id.textCalorie);
        tracker = (TextView) view.findViewById(R.id.calorieCounter);


        return view; // layout is returned to then be shown
    }



    // add necessary views
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume(){

        TextView goal = (TextView) getActivity().findViewById(R.id.calorieGoal);
        String displayText = "Goal: " + String.valueOf(calorieGoal.getInt("calorieGoal", 2500));
        Log.i(TAG, "Current goal: " + displayText);
        goal.setText(displayText);

        // removes all views inside table layout apart from 1st row
        int count = tableLayout.getChildCount();
        for (int i = 1; i < count; i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }

        Integer calorieTracker = calorieGoal.getInt("calorieGoal", 2500);
        ArrayList<Set<String>> itemData = receiveData(); // receive data from shared preference
        if (itemData != null) {
            for (Set<String> singleSet : itemData) {
                List<String> orderedData = checkContents(singleSet); // orders each pair of item name and calorie count

                calorieTracker -= Integer.parseInt(orderedData.get(1)); // calculates calorie tracker

                addRow(orderedData); // creates view
            }
        }
        counter.edit().putInt("tracker", calorieTracker).apply(); // saves current calorie tracker amount

        // changes look of textview once goal has been reached
        if (counter.getInt("tracker", 0) <= 0){
            // sets textview
            tracker.setTextColor(Color.GREEN);
            tracker.setText(String.valueOf(calorieGoal.getInt("calorieGoal", 0) - calorieTracker));

            textCalorie.setText("calories consumed");

        } else {
            // sets textview
            tracker.setText(Integer.toString(counter.getInt("tracker", 0)));
            textCalorie.setText("calories remaining");
        }

        super.onResume(); // calls super method
    }

    // receives data from shared preference
    public ArrayList<Set<String>> receiveData() {
        ArrayList<Set<String>> allValues = new ArrayList<>();
        int amountOfItems = foodData.getInt("amount", 0); // retreives amount of items that have been added

        // if more than one item has been added...
        if (amountOfItems != 0) {
            int x;

            // iterate through each item and save it to an ArrayList
            for (x = 1; x <= amountOfItems; x++) {
                if (foodData.getStringSet(Integer.toString(x), null) == null){}
                else {
                    allValues.add(foodData.getStringSet(Integer.toString(x), null));

                }
            }
        }
        return allValues; // return values
    }

    // purpose of this method is to convert values from an unordered Set to an
    // ordered Array so we know which value is the calories and item name
    public List<String> checkContents(Set<String> itemData) {
        String itemName = null; // string initialisation using null as it is a local variable
        String itemCalories = null;
        List<String> orderedContents = new ArrayList<>();
        String[] data = itemData.toArray(new String[itemData.size()]);

        // checks which value is an integer using parseInt
        // thus ordering them accordingly
        try {
            Integer.parseInt(data[0]);
            itemCalories = data[0];
            itemName = data[1];
        } catch (NumberFormatException e) {
            itemName = data[0];
            itemCalories= data[1];
        }
        orderedContents.add(itemName);
        orderedContents.add(itemCalories);
        return orderedContents; // return items with item name first, item calories second. (in a list)
    }


    // add a row to the tablelayout
    public void addRow(final List<String> itemData){

        // get instances
        final TableRow tableRow = new TableRow(getActivity());
        TextView foodName = new TextView(getActivity());
        TextView calories = new TextView(getActivity());
        ImageView remove = new ImageView(getActivity());

        remove.setImageResource(R.drawable.ic_clear_black_24dp); // uses image from drawable

        // now create the view
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                TableRow.LayoutParams.FILL_PARENT, 1)); // sets column width and height

        foodName.setSingleLine(false); // allows column to use multiple lines if name is too long
        final String[] itemArray = itemData.toArray(new String[itemData.size()]); // convert List to array
        foodName.setText(itemArray[0]); // sets first text view
        calories.setText(itemArray[1]); // sets second text view
        calories.setTextColor(-65281); // sets color to grey
        calories.setTextSize(20); // size of text

        // add views
        tableRow.addView(foodName);
        tableRow.addView(calories);
        tableRow.addView(remove);
        // add row
        tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT, 1f));
        remove.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                tableLayout.removeView(tableRow); // remove view

                // find item inside sharedpreferences and now remove it from there
                Set<String> findSet = new HashSet<>();
                findSet.add(itemArray[0]);
                findSet.add(itemArray[1]);
                Log.i(TAG, "Item '" + itemArray[0] + "' removed");

                // updates calorie goal
                Integer updatedAmount = counter.getInt("tracker", 0);

                // sets textview to different look if calorie amount surpasses / goes back under goal
                if (Integer.parseInt((String) tracker.getText()) > calorieGoal.getInt("calorieGoal", 2500)) {
                    updatedAmount += Integer.parseInt(itemArray[1]);
                    if ((calorieGoal.getInt("calorieGoal", 0) - updatedAmount) < calorieGoal.getInt("calorieGoal", 2500)) {
                        tracker.setText(String.valueOf(updatedAmount));
                        tracker.setTextColor(getResources().getColor(R.color.colorAccent));
                        textCalorie.setText("calories remaining");
                    } else {
                        tracker.setText(String.valueOf(calorieGoal.getInt("calorieGoal", 0) - updatedAmount));
                        textCalorie.setText("calories consumed");
                    }
                } else {
                    updatedAmount += Integer.parseInt(itemArray[1]);
                    tracker.setText(String.valueOf(updatedAmount));
                }


                counter.edit().putInt("tracker", updatedAmount).apply(); // saves current calorie tracker amount

                int amount = foodData.getInt("amount", 0);
                int x;
                Boolean oneRemoved = false; // this variable prevents the removal of multiple views of the same item name
                for (x = 1; x<= amount; x ++) {
                    Set<String> findData = foodData.getStringSet(String.valueOf(x), null);
                    if (findData == null) {}

                    else if (findData.equals(findSet) && !oneRemoved) { // find correct item
                        oneRemoved = true;
                        foodData.edit().remove(String.valueOf(x)).apply(); // remove from sharedpreferences
                    }
                }


            }

        });

    }

    public void saveItems(String todayDate) {
        SharedPreferences saveToday = getActivity().getSharedPreferences(todayDate, 0);
        saveToday.edit().putInt("amountOfItems", 0).apply();
        ArrayList<Set<String>> itemData = receiveData(); // receive data from shared preference
        int x = 1; // variable x counts amount of items
        if (itemData != null) {
            for (Set<String> singleItem : itemData) { // for each item in the Set
                saveToday.edit().putStringSet(String.valueOf(x), singleItem).apply();
                x += 1;

            }
            x -= 1;
            saveToday.edit().putInt("amountOfItems", x).apply();
        }
    }

}
