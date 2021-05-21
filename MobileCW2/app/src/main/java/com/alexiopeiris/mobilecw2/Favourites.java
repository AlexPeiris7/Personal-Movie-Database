package com.alexiopeiris.mobilecw2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;

public class Favourites extends AppCompatActivity {

    String [] favouriteMovies;
    ListView listView;
    public static final String item = "movie";
    DatabaseHelper databaseHelper;
    //arraylist that stores removed movies from favourites
    ArrayList<String> removedFavourites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        //hiding the action bar
        getSupportActionBar().hide();

        databaseHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        setFavMovieArray();
        setListView();

    }

    public void setListView(){
        //setting up the list view with the movies retrieved from database
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, favouriteMovies);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for(int x = 0; x<favouriteMovies.length ; x++){
            int pos = x;
            listView.setItemChecked(pos,true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = (String) listView.getItemAtPosition(position);
                removedFavourites.add(movie);
                System.out.println(position);
            }
        });
    };

    public void setFavMovieArray(){

        Cursor data = databaseHelper.getData();
        // even tho using a treeset might be a better option as it sorts automatically,
        // treeset wont store duplicated values which when we take movies there are bound to be duplicates
//        Collection<String> moviesNames =
//                new TreeSet<String>(Collator.getInstance());
        ArrayList<String> favMoviesNames = new ArrayList<>();
        while(data.moveToNext()){
            //if movie is a favourite add to fav list
            if(data.getString(7).equals("Favourite")){
                // index 1 is movie names
                favMoviesNames.add(data.getString(1));
            }
        }
        //sorting the movies list
        //collator is like a comparator
        Collections.sort(favMoviesNames, Collator.getInstance());
        //creating a temp array to store movie names of the size of the arraylist
        String[] tempArray = new String [favMoviesNames.size()];

        //adding movie names from arraylist to array
        for(int x = 0 ; x<favMoviesNames.size(); x++){
            tempArray[x] = favMoviesNames.get(x);
        }

        //setting temp array to actual movie array
        favouriteMovies = tempArray;

    }

    public void removeFavourites(View view){
        String favourite = "Not Favourite";
        //removing movies from favourites
        for(String movie : removedFavourites){
            databaseHelper.updateFavourite(movie, favourite);
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                "Movies has been removed from favourites!",
                Toast.LENGTH_LONG);
        toast.show();

        //re setting the list view
        setFavMovieArray();
        setListView();
    }
}