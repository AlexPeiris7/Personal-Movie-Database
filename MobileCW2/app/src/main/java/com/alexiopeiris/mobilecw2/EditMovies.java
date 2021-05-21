package com.alexiopeiris.mobilecw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;

public class EditMovies extends AppCompatActivity {

    String[] movies;
    ListView listView;
    public static final String item = "movie";
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);

        //hiding the action bar
        getSupportActionBar().hide();

        databaseHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        setMovieArray();
        setListView();

    }

    public void setListView() {
        //setting up the list view with the movies retrieved from database
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, movies);
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        final Intent intent = new Intent(this, EditSelectedMovie.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = (String) listView.getItemAtPosition(position);
                //passing the movie name to the edit selected movie activity
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });
    }

    ;

    public void setMovieArray() {

        Cursor data = databaseHelper.getData();
        // even tho using a treeset might be a better option as it sorts automatically,
        // treeset wont store duplicated values which when we take movies there are bound to be duplicates
//        Collection<String> moviesNames =
//                new TreeSet<String>(Collator.getInstance());
        ArrayList<String> moviesNames = new ArrayList<>();
        while (data.moveToNext()) {
            // index 1 is movie names
            moviesNames.add(data.getString(1));
        }
        //sorting the movies list
        //collator is like a comparator
        Collections.sort(moviesNames, Collator.getInstance());
        //creating a temp array to store movie names of the size of the arraylist
        String[] tempArray = new String[moviesNames.size()];

        //adding movie names from arraylist to array
        for (int x = 0; x < moviesNames.size(); x++) {
            tempArray[x] = moviesNames.get(x);
        }

        //setting temp array to actual movie array
        movies = tempArray;

    }
}