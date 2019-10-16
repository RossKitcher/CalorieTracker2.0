package com.example.rosskitcher.computingproject20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.List;

public class SearchFood extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

    // Initialise activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // overrides superclass method
        setContentView(R.layout.activity_search_food); //defines the UI

        ListView listview = (ListView) findViewById(R.id.listView); // allows interaction with the listview

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this); // gets instance of the class
        databaseAccess.open(); // opens database connection
        final List<String> items = databaseAccess.getItems(); // gets names of items and saves them to a list
        databaseAccess.close(); // closes database connection

        // sets items to the listview
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listview.setAdapter(adapter);

        // listens for when an item in list is clicked
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Opens activity, 'ItemInfo'
                Intent intent = new Intent(getBaseContext(),ItemInfo.class);
                intent.putExtra("title", adapter.getItem(position)); // sends name of the item clicked as well
                startActivity(intent);
            }
        });

    }

    // when menu button is pressed (search)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu); //inflates .xml file
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        // Handles when text is inputted into the search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        // returns layout
        return super.onCreateOptionsMenu(menu);
    }


}
