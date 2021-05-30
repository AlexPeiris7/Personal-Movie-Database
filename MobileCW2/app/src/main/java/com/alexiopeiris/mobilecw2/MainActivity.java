package com.alexiopeiris.mobilecw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hiding the action bar
        getSupportActionBar().hide();

    }

    public void goToRegisterMovie(View view) {
        Intent intent = new Intent(this,RegisterMovie.class);
        startActivity(intent);
    }

    public void goToDisplayMovies(View view) {
        Intent intent = new Intent(this,DisplayMovies.class);
        startActivity(intent);
    }

    public void goToFavourites(View view) {
        Intent intent = new Intent(this,Favourites.class);
        startActivity(intent);
    }

    public void goToEditMovies(View view) {
        Intent intent = new Intent(this,EditMovies.class);
        startActivity(intent);
    }

    public void goToSearch(View view) {
        Intent intent = new Intent(this,Search.class);
        startActivity(intent);
    }

    public void goToRatings(View view) {
        Intent intent = new Intent(this,Ratings.class);
        startActivity(intent);
    }
}