package com.alexiopeiris.mobilecw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("ALL")
public class RegisterMovie extends AppCompatActivity {

    private EditText title;
    private EditText year;
    private EditText director;
    private EditText actors;
    private EditText rating;
    private EditText review;

    Movie movie;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        //hiding the action bar
        getSupportActionBar().hide();

        databaseHelper = new DatabaseHelper(this);
        title = findViewById(R.id.titleET);
        year = findViewById(R.id.yearET);
        director = findViewById(R.id.directorET);
        actors = findViewById(R.id.actorsET);
        rating = findViewById(R.id.ratingET);
        review = findViewById(R.id.reviewET);
    }

    //method to check if the inputs are correct
    //i.e year should be between 1895 and 2021
    // ratings should be between 1 and 10
    public boolean checkInputs(){
        Log.i("METHOD CALLED","checkInputs()");

        String titleString = title.getText().toString();
        String yearString = year.getText().toString();
        String directorString = director.getText().toString();
        String actorsString = actors.getText().toString();
        String ratingString = rating.getText().toString();
        String reviewString = review.getText().toString();

        //checking if inputs are empty because edittext returns empty string if nothin is typed
        if(
               titleString.isEmpty() ||
                       titleString.isEmpty() ||
                       yearString.isEmpty() ||
                       directorString.isEmpty() ||
                       actorsString.isEmpty()||
                       ratingString.isEmpty() ||
                       reviewString.isEmpty()

        ){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Every field must be filled!",
                    Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
        //parsing year and rating from string to int before checking if they are eligible
        int yearInteger = Integer.parseInt(year.getText().toString());
        int ratingInteger = Integer.parseInt(rating.getText().toString());
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
        String favourite = "no";
        movie = new Movie(titleString,yearInteger,directorString,actorsString,ratingInteger,reviewString,favourite);

        //if it comes here after checking everything it means that the inputs were correct
        return true;
    }
    public void saveMovie(View view) {
        Log.i("METHOD CALLED","saveMovie()");
        //if year and ratings are not between the range return
        if(!checkInputs())
            return;

        Log.i("DATABASE","Inserting data to database");

        boolean insertData =
        databaseHelper.insertData(movie.getTitle(),
                movie.getYear(),
                movie.getDirector(),
                movie.getActors(),
                movie.getRating(),
                movie.getReview(),
                movie.getFavourite());

        if(insertData){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Movie has been added to database.",
                    Toast.LENGTH_LONG);
            toast.show();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Something went  wrong when adding movie to database...",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        //deleting inputs from edit texts so that user can add more movies
        title.setText("");
        year.setText("");
        director.setText("");
        actors.setText("");
        rating.setText("");
        review.setText("");
    }
}