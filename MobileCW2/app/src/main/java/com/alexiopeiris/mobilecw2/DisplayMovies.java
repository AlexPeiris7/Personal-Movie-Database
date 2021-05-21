package com.alexiopeiris.mobilecw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class DisplayMovies extends AppCompatActivity {

    String [] movies;
    ListView listView;
    public static final String item = "movie";
    DatabaseHelper databaseHelper;
    //arraylist that stores added movies to favourites
    ArrayList<String> toFavourites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        //hiding the action bar
        getSupportActionBar().hide();

        databaseHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        setMovieArray();
        setListView();
    }
    public void setListView(){
        //setting up the list view with the movies retrieved from database
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, movies);
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = (String) listView.getItemAtPosition(position);
                toFavourites.add(movie);
            }
        });
    };

    public void setMovieArray(){

        Cursor data = databaseHelper.getData();
        // even tho using a treeset might be a better option as it sorts automatically,
        // treeset wont store duplicated values which when we take movies there are bound to be duplicates
//        Collection<String> moviesNames =
//                new TreeSet<String>(Collator.getInstance());
        ArrayList<String> moviesNames = new ArrayList<>();
        while(data.moveToNext()){
            // index 1 is movie names
            moviesNames.add(data.getString(1));
        }
        //sorting the movies list
        //collator is like a comparator
        Collections.sort(moviesNames, Collator.getInstance());
        //creating a temp array to store movie names of the size of the arraylist
        String[] tempArray = new String [moviesNames.size()];

        //adding movie names from arraylist to array
        for(int x = 0 ; x<moviesNames.size(); x++){
            tempArray[x] = moviesNames.get(x);
        }

        //setting temp array to actual movie array
        movies = tempArray;

    }

    public void addToFavourites(View view) {
        String favourite = "Favourite";
        //add movies to favourites
        for(String movie : toFavourites){
            databaseHelper.updateFavourite(movie, favourite);
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                "Movies has been added to favourites!",
                Toast.LENGTH_LONG);
        toast.show();
    }
}