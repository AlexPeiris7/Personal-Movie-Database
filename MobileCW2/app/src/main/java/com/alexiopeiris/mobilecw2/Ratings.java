package com.alexiopeiris.mobilecw2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;

public class Ratings extends AppCompatActivity {

    String [] movies;
    ListView listView;
    public static final String item = "movie";
    DatabaseHelper databaseHelper;
    String selectedMovie = new String();
    //StringBuilder for json result
    StringBuilder result;
    //movie id to get rating
    String movieId;
    //image path
    String imageURL;
    //textview to show selected movie name
    TextView movieNameTextView;
    //textview to show selected movie rating
    TextView movieRatingTextView;
    //imageview for movie image
    ImageView imageView;
    //Bitmap of image from url link
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        //hiding the action bar
        getSupportActionBar().hide();

        result = new StringBuilder();
        databaseHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        movieNameTextView = findViewById(R.id.movieName);
        movieRatingTextView = findViewById(R.id.rating);
        imageView = findViewById(R.id.imageView);
        setMovieArray();
        setListView();
    }

    public void setListView(){
        //setting up the list view with the movies retrieved from database
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, movies);
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMovie = (String) listView.getItemAtPosition(position);
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
            //in this activity we will be storing title and year in the same string and display
            //so that we can directly get this for the api call
            String title_year = "";
            // index 1 is movie names
            title_year = title_year + data.getString(1) + " ";
            // index 2 is movie year
            title_year = title_year + data.getString(2);

            moviesNames.add(title_year);
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

    //getResult to get ID
    public void getResult(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (selectedMovie.isEmpty()) {
                    Log.d("Movies","No movies selected");
                    return;
                }
                String apiKey = "k_9v4mqwhz";
                String query = "https://imdb-api.com/en/API/SearchTitle/"+apiKey+"/";//+movieName;
                    //allocate new stringbuilder for cleaning existing strinbuilder
                    result = new StringBuilder();
                    //splitting movie name and year from string to be added to api
                    String[] parts = selectedMovie.split(" ");
                    String movieName = parts[0];
                    String movieYear = parts[1];

                    query = query + movieName + "%20" + movieYear;

                    try {
                        URL url = new URL(query);
                        Log.i("QUERY", query);
                        URLConnection conn = url.openConnection();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = rd.readLine()) != null) {
                            result.append(line);
                        }
                        rd.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Log.i("JSON Result ID",result.toString());
                                parseJsonToString(result.toString());
                                loadImage(imageURL);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //API CALL TO GET RATING
                    getResult2();
                }
        });
        thread.start();
    }

    public void getRating(View view) {
        Log.d("Button", "Button Clicked!");
        //API CALL WHICH GETS ID AND AFTER THE RATING WITH ANOTHER CALL
        getResult();
    }

    //parse to get ID
    public void parseJsonToString(String responseString) throws JSONException {

        JSONObject data = new JSONObject(responseString);

        JSONArray array =
                data.getJSONArray("results");
        JSONObject jsonObject =
                array.getJSONObject(0);
        //getting image id
        movieId = jsonObject.getString("id");
        Log.i("MOVIE_ID",movieId);

        //setting imageview
        imageURL= jsonObject.getString("image");
        Log.i("MOVIE_IMAGE",imageURL);

    }

    //method to load image (Bitmap) taken from json obj
    public void loadImage(final String imageURL) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("IMAGE_URL",imageURL);
                try {
                    URL url = new URL(imageURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream is = urlConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        setImage();
                    }
                });
            }
        });
        thread.start();
    }
    //method to set the image
    public void setImage(){
        imageView.setImageBitmap(bitmap);
    }

    //getResult to get rating
    public void getResult2(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String apiKey = "k_9v4mqwhz";
                String query = "https://imdb-api.com/en/API/UserRatings/"+apiKey+"/"+movieId;//+id frm the other request
                //allocate new stringbuilder for cleaning existing strinbuilder
                result = new StringBuilder();

                    try {
                        URL url = new URL(query);
                        Log.i("QUERY", query);
                        URLConnection conn = url.openConnection();
                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = rd.readLine()) != null) {
                            result.append(line);
                        }
                        rd.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                Log.i("JSON Result RATING",result.toString());
                                parseJsonToString2(result.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
        });
        thread.start();
    }
    //parse to get rating
    public void parseJsonToString2(String responseString) throws JSONException {

        JSONObject data = new JSONObject(responseString);
        String rating = data.getString("totalRating");
        //setting textViews
        movieNameTextView.setText(selectedMovie);
        movieRatingTextView.setText("Rating: " + rating);
        Log.i("RATING",rating);

    }
}