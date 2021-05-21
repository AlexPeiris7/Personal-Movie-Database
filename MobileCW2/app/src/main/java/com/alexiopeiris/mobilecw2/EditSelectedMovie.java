package com.alexiopeiris.mobilecw2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;

public class EditSelectedMovie extends AppCompatActivity {

    RatingBar rating;
    String movieName;
    DatabaseHelper databaseHelper;
    Movie movie;
    private EditText title;
    private EditText year;
    private EditText director;
    private EditText actors;
    private EditText review;
    private EditText favourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_selected_movie);
        rating = findViewById(R.id.ratingBar);

        //hiding the action bar
        getSupportActionBar().hide();

        //bundle to get the movie name passed from previous activity
        Bundle bundle = getIntent().getExtras();
        movieName = bundle.getString("movie");
        databaseHelper = new DatabaseHelper(this);
        title = findViewById(R.id.titleET);
        year = findViewById(R.id.yearET);
        director = findViewById(R.id.directorET);
        actors = findViewById(R.id.actorsET);
        review = findViewById(R.id.reviewET);
        favourite = findViewById(R.id.favouriteET);

        setMovieDetailsFromDB();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setMovieDetailsFromDB(){
        Cursor data = databaseHelper.getOneMovieData(movieName);

        //settting edit text with data from database
        while(data.moveToNext()){
            title.setText(data.getString(1));
            year.setText(data.getString(2));
            director.setText(data.getString(3));
            actors.setText(data.getString(4));
            rating.setRating(Integer.parseInt(data.getString(5)));
            review.setText(data.getString(6));
            favourite.setText(data.getString(7));
        }
    }

    //method to check if the inputs are correct
    //i.e year should be between 1895 and 2021
    // ratings should be between 1 and 10
    public boolean checkInputs(){
        Log.i("METHOD CALLED","checkInputs()");

        //getting data from edittext
        String titleString = title.getText().toString();
        String yearString = year.getText().toString();
        String directorString = director.getText().toString();
        String actorsString = actors.getText().toString();
        String ratingString = String.valueOf(rating.getRating());
        String favouriteString = actors.getText().toString();
        String reviewString = review.getText().toString();

        //checking if inputs are empty because edittext returns empty string if nothin is typed
        if(
                titleString.isEmpty() ||
                        titleString.isEmpty() ||
                        yearString.isEmpty() ||
                        directorString.isEmpty() ||
                        actorsString.isEmpty()||
                        ratingString.isEmpty() ||
                        reviewString.isEmpty() ||
                        favouriteString.isEmpty()

        ){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Every field must be filled!",
                    Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        //parsing year and rating from string to int before checking if they are eligible
        int yearInteger = Integer.parseInt(year.getText().toString());
        int ratingInteger = (int)(rating.getRating());
        if(
                yearInteger >= 1895 &&
                        yearInteger <= 2021
        ){/*do nothing and continue with check*/}
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Year must be between 1895 and 2021!",
                    Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        if(
                ratingInteger >= 1 &&
                        ratingInteger <= 10
        ){/*do nothing and continue with check*/}
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Rating must be between 1 and 10!",
                    Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        //creating new Movie Object after checking that everything is correct
        movie = new Movie(titleString,yearInteger,directorString,actorsString,ratingInteger,reviewString,favouriteString);

        //if it comes here after checking everything it means that the inputs were correct
        return true;
    }
    public void saveChanges(View view) {
        Log.i("METHOD CALLED","saveChanges()");
        //if year and ratings are not between the range return
        if(!checkInputs())
            return;

        Log.i("DATABASE","Updating data to database");

        //passin previous movie name taken from previous activity(not updated)
        //to change the database and update with eventual new name
        boolean insertData =
                databaseHelper.updateData(movieName,
                        movie.getTitle(),
                        movie.getYear(),
                        movie.getDirector(),
                        movie.getActors(),
                        movie.getRating(),
                        movie.getReview(),
                        movie.getFavourite());

        if(insertData){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Movie has been updated in database.",
                    Toast.LENGTH_LONG);
            toast.show();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Something went  wrong when adding movie to database...",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        Intent intent = new Intent(this,EditMovies.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void deleteMovie(View view) {
        if(databaseHelper.deleteEntry(movieName)){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Movie has been deleted in database.",
                    Toast.LENGTH_LONG);
            toast.show();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Something went  wrong when adding movie to database...",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        Intent intent = new Intent(this,EditMovies.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}