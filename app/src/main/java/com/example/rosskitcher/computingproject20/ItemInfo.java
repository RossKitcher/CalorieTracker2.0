package com.example.rosskitcher.computingproject20;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemInfo extends AppCompatActivity {

    // declaration of variable
    private String itemName;
    private DatabaseAccess databaseAccess;


    // initialise activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        databaseAccess = DatabaseAccess.getInstance(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseAccess.open();
        // gets intent from previous activity and also gets value that was sent
        Intent intent = getIntent();
        String item = intent.getStringExtra("title");
        itemName = item; // for use by different method
        setTitle(item + " (100g)"); // sets title of the activity
        // calls methods
        setNutrients(getNutrients(item));
        setCalories(getCalories(item));
    }

    // inflates menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // closes database connection
    @Override
    protected void onStop() {
        super.onStop();
        databaseAccess.close();
    }


    // handles when menu button is clicked
    public void addItem(MenuItem menuItem) {

        // creates Set to store item name and calorie count
        Set<String> foodInfo = new HashSet<>();
        foodInfo.add(itemName);
        foodInfo.add(getCalories(itemName));

        //get shared preferences
        SharedPreferences pref = this.getSharedPreferences("foodItems", 0);
        SharedPreferences.Editor editor = pref.edit();

        // changes amount of items as new item has been added
        Integer amountOfItems;
        amountOfItems = pref.getInt("amount", 0);
        amountOfItems += 1;
        editor.putInt("amount", amountOfItems);

        editor.putStringSet(Integer.toString(amountOfItems), foodInfo); // store foodInfo
        editor.apply();
        Toast.makeText(this,"Added", Toast.LENGTH_SHORT).show(); // shows user that item has been added

    }


    // gets amount of calories and returns string
    public String getCalories(String item) {
        List info = databaseAccess.getItemCal(item);
        return (String) info.get(0);
    }

    // gets all nutrients
    public Map<String, String> getNutrients(String item) {

        // calls method from DatabaseAccess, returns list of nutrients
        List info = databaseAccess.getNutrients(item);


        // defines HashMap and uses it to store all nutrients with their respective values
        Map<String, String> nutrients = new HashMap<String, String>();
        nutrients.put("Fats", (String) info.get(0));
        nutrients.put("Protein", (String) info.get(1));
        nutrients.put("Carbohydrates", (String) info.get(2));
        nutrients.put("Sugars", (String) info.get(3));
        nutrients.put("Fibre", (String) info.get(4));
        nutrients.put("Vitamin A", (String) info.get(5));
        nutrients.put("Vitamin C", (String) info.get(6));
        nutrients.put("Vitamin D", (String) info.get(7));
        nutrients.put("Vitamin B6", (String) info.get(8));
        nutrients.put("Vitamin B12", (String) info.get(9));
        nutrients.put("Iron", (String) info.get(10));
        nutrients.put("Magnesium", (String) info.get(11));
        nutrients.put("Potassium", (String) info.get(12));
        nutrients.put("Calcium", (String) info.get(13));
        nutrients.put("Sodium", (String) info.get(14));

        return nutrients;

    }

    // sets calories
    public void setCalories(String calories) {
        TextView showCalories = (TextView) findViewById(R.id.showCalories);
        showCalories.setText(calories);
    }

    // sets all nutrients by setting the text from a textview to the correct value
    @SuppressLint("SetTextI18n")
    public void setNutrients(Map<String, String> nutrients) {

        // recommended daily intake for each micro nutrient
        // double to prevent numberformatexception
        double VIT_A_INTAKE = 5000;
        double VIT_B6_INTAKE = 1.3;
        double VIT_B12_INTAKE = 2.4;
        double VIT_C_INTAKE = 90;
        double VIT_D_INTAKE = 200;
        double IRON_INTAKE = 20;
        double MAGNESIUM_INTAKE = 400;
        double POTASSIUM_INTAKE = 4700;
        double CALCIUM_INTAKE = 1000;
        double SODIUM_INTAKE = 2300;


        // initialise textview
        TextView showProtein = (TextView) findViewById(R.id.showProtein);
        if (nutrients.get("Protein").isEmpty()) // if no value returned
            showProtein.setText("0g"); // set to 0g
        else {
            showProtein.setText(nutrients.get("Protein") + "g"); // set to value queried
        }

        TextView showCarbs = (TextView) findViewById(R.id.showCarbs);
        if (nutrients.get("Carbohydrates").isEmpty())
            showCarbs.setText("0g");
        else {
            showCarbs.setText(nutrients.get("Carbohydrates") + "g");
        }

        TextView showFat = (TextView) findViewById(R.id.showFat);
        if (nutrients.get("Fats").isEmpty())
            showFat.setText("0g");
        else {
            showFat.setText(nutrients.get("Fats") + "g");
        }

        TextView showSugars = (TextView) findViewById(R.id.showSugars);
        if (nutrients.get("Sugars").isEmpty())
            showSugars.setText("0g");
        else {
            showSugars.setText(nutrients.get("Sugars") + "g");
        }

        TextView showFibre = (TextView) findViewById(R.id.showFibre);
        if (nutrients.get("Fibre").isEmpty())
            showFibre.setText("0g");
        else {
            showFibre.setText(nutrients.get("Fibre") + "g");
        }

        TextView showVitaminA = (TextView) findViewById(R.id.showVitaminA);
        if (nutrients.get("Vitamin A").isEmpty())
            showVitaminA.setText("N/A");
        else {
            // work out percentage of daily amount, then convert to whole number
            int percentageA = (int) (((Double.parseDouble(nutrients.get("Vitamin A")))/VIT_A_INTAKE)*100);
            showVitaminA.setText(String.valueOf(percentageA) + "%"); // set text
        }

        TextView showVitaminC = (TextView) findViewById(R.id.showVitaminC);
        if (nutrients.get("Vitamin C").isEmpty())
            showVitaminC.setText("N/A");
        else {
            int percentageC = (int) (((Double.parseDouble(nutrients.get("Vitamin C")))/VIT_C_INTAKE)*100);
            showVitaminC.setText(String.valueOf(percentageC) + "%");
        }

        TextView showVitaminD = (TextView) findViewById(R.id.showVitaminD);
        if (nutrients.get("Vitamin D").isEmpty())
            showVitaminD.setText("N/A");
        else {
            int percentageD = (int) (((Double.parseDouble(nutrients.get("Vitamin D")))/VIT_D_INTAKE)*100);
            showVitaminD.setText(String.valueOf(percentageD) + "%");
        }

        TextView showVitaminB6 = (TextView) findViewById(R.id.showVitaminB6);
        if (nutrients.get("Vitamin B6").isEmpty())
            showVitaminB6.setText("N/A");
        else {
            int percentageB6 = (int) (((Double.parseDouble(nutrients.get("Vitamin B6"))) / VIT_B6_INTAKE) * 100);
            showVitaminB6.setText(String.valueOf(percentageB6) + "%");
        }

        TextView showVitaminB12 = (TextView) findViewById(R.id.showVitaminB12);
        if (nutrients.get("Vitamin B12").isEmpty())
            showVitaminB12.setText("N/A");
        else {
            int percentageB12 = (int) (((Double.parseDouble(nutrients.get("Vitamin B12")))/VIT_B12_INTAKE)*100);
            showVitaminB12.setText(String.valueOf(percentageB12) + "%");
        }

        TextView showIron = (TextView) findViewById(R.id.showIron);
        if (nutrients.get("Iron").isEmpty())
            showIron.setText("N/A");
        else {
            int percentageIron = (int) ((Double.parseDouble((nutrients.get("Iron")))/IRON_INTAKE)*100);
            showIron.setText(String.valueOf(percentageIron) + "%");
        }

        TextView showMagnesium = (TextView) findViewById(R.id.showMagnesium);
        if (nutrients.get("Magnesium").isEmpty())
            showMagnesium.setText("N/A");
        else {
            int percentageMag = (int) (((Double.parseDouble(nutrients.get("Magnesium")))/MAGNESIUM_INTAKE)*100);
            showMagnesium.setText(String.valueOf(percentageMag) + "%");
        }

        TextView showPotassium = (TextView) findViewById(R.id.showPotassium);
        if (nutrients.get("Potassium").isEmpty())
            showPotassium.setText("N/A");
        else {
            int percentagePot = (int) (((Double.parseDouble(nutrients.get("Potassium")))/POTASSIUM_INTAKE)*100);
            showPotassium.setText(String.valueOf(percentagePot) + "%");
        }

        TextView showCalcium = (TextView) findViewById(R.id.showCalcium);
        if (nutrients.get("Calcium").isEmpty())
            showCalcium.setText("N/A");
        else {
            int percentageCalcium = (int) (((Double.parseDouble(nutrients.get("Calcium")))/CALCIUM_INTAKE)*100);
            showCalcium.setText(String.valueOf(percentageCalcium) + "%");
        }

        TextView showSodium = (TextView) findViewById(R.id.showSodium);
        if (nutrients.get("Sodium").isEmpty())
            showSodium.setText("N/A");
        else {
            int percentageSodium = (int) (((Double.parseDouble(nutrients.get("Sodium")))/SODIUM_INTAKE)*100);
            showSodium.setText(String.valueOf(percentageSodium) + "%");
        }
    }

}
