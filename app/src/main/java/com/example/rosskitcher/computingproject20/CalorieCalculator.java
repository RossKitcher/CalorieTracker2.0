package com.example.rosskitcher.computingproject20;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CalorieCalculator extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_calculator);

        //set title
        setTitle("Calorie Calculator");

        // variable declarations
        // declared final as they are accessed by inner class
        final EditText editAge = (EditText) findViewById(R.id.editAge);
        final EditText editKG = (EditText) findViewById(R.id.editWeight);
        final EditText editCentimeters = (EditText) findViewById(R.id.editHeight);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerGender);
        final EditText customValue = (EditText) findViewById(R.id.customValue);

        final SharedPreferences preferences = getSharedPreferences("calorieGoal", 0);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);




        // calculate when button is pressed
        Button calculate = (Button) findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer calories = 0;

                // get input from EditText's and Spinner
                String age = editAge.getText().toString();
                String weight = editKG.getText().toString();
                String height = editCentimeters.getText().toString();
                String gender = spinner.getSelectedItem().toString();

                String checkAge;
                String checkWeight;
                String checkHeight;
                Boolean error = false;

                // to avoid nullpointerexception
                if (age.isEmpty()) {
                    checkAge = "Age, ";
                    error = true;
                } else {
                    checkAge = "";
                }
                if (weight.isEmpty()) {
                    checkWeight = "Weight, ";
                    error = true;
                } else {
                    checkWeight = "";
                }
                if (height.isEmpty()) {
                    checkHeight = "Height, ";
                    error = true;
                } else {
                    checkHeight = "";
                }

                // if all values have been entered
                if (!error) {

                    //gender separated as different equation for each
                    if (gender.equals("Male")) {
                        // BMR equation
                        Double BMR = (10 * (Integer.parseInt(weight)) + (6.25 * (Integer.parseInt(height)))
                                - (5 * (Integer.parseInt(age))) + 5);

                        calories = BMR.intValue();
                    } else if (gender.equals("Female")) {
                        // BMR equation
                        Double BMR = (10 * (Integer.parseInt(weight)) + (6.25 * (Integer.parseInt(height)))
                                - (5 * (Integer.parseInt(age))) + -161);

                        calories = BMR.intValue();
                    }

                    // save new goal
                    preferences.edit().putInt("calorieGoal", calories).apply();

                    // display message to user
                    Toast.makeText(getApplicationContext(), "New goal is: " + String.valueOf(calories), Toast.LENGTH_LONG).show();

                } else {
                    // if the user has missed a value
                    Toast.makeText(getApplicationContext(),
                            "Please enter the following values: " + checkAge + checkHeight + checkWeight,
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        // custom value
        Button saveCustom = (Button) findViewById(R.id.saveCustom);
        saveCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get input from EditText
                String custom = customValue.getText().toString();

                // to avoid nullpointerexception
                if (custom.isEmpty()) {
                    // if user forgot to enter a value
                    Toast.makeText(getApplicationContext(), "Please enter a value", Toast.LENGTH_LONG).show();
                } else {

                    // save new goal
                    preferences.edit().putInt("calorieGoal", Integer.parseInt(custom)).apply();

                    // display message to user
                    Toast.makeText(getApplicationContext(), "New goal is: " + String.valueOf(custom), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
