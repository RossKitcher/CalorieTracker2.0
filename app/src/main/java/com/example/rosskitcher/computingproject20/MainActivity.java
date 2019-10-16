package com.example.rosskitcher.computingproject20;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // uses Calorie Tracker as homescreen
        FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new CalorieTracker();
        manager.replace(R.id.flFragments, fragment).commit();
        setTitle("Calorie Tracker"); // set title

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // when user presses back button
    @Override
    public void onBackPressed() {
        // makes sure drawer is closed first
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
        if (id == R.id.nav_calorie_tracker) {
            //launches CalorieTracker
            Log.i(TAG, "Menu button 'Calorie Tracker' pressed");
            Fragment fragment = new CalorieTracker();
            manager.replace(R.id.flFragments, fragment, "calorieTracker").addToBackStack(null).commit();


        } else if (id == R.id.nav_history) {
            //launches History
            Log.i(TAG, "Menu button 'History' pressed");
            Fragment fragment = new History();
            manager.replace(R.id.flFragments, fragment, "history").addToBackStack(null).commit();

        } else if (id == R.id.nav_settings) {
            //launches Settings
            Log.i(TAG, "Menu button 'Settings' pressed");
            Fragment fragment = new Settings();
            manager.replace(R.id.flFragments, fragment, "settings").addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Handles result from barcode scan
    // Overrides the method from a superclass
    // cannot handle the output from the fragment, only the activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data); // gets result
        if (result != null) { // handles scan result
            if (result.getContents() == null) { // if user cancels scanning operation
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show(); // output message
            } else {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this); // get instance of DatabaseAccess
                databaseAccess.open();// open DB connection
                String itemName = TextUtils.join(",", databaseAccess.getUPCCodes(result.getContents())); // list to string
                databaseAccess.close(); // close db connection
                if (itemName == null || itemName.isEmpty()) // if returned string is empty
                    Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                else {
                    // launches ItemInfo activity
                    Intent intent = new Intent(getBaseContext(), ItemInfo.class);
                    intent.putExtra("title", itemName); // sends name of the scanned item
                    startActivity(intent);
                }
            }
        } else {
            // keyword super overrides method in superclass, rather than subclass
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
