package com.alexiopeiris.mobilecw2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;

public class Search extends AppCompatActivity {

    EditText userEntry;
    DatabaseHelper databaseHelper;
    ListView listView;
    String [] details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //hiding the action bar
        getSupportActionBar().hide();

        databaseHelper = new DatabaseHelper(this);
        userEntry = findViewById(R.id.userEntryET);
        listView = findViewById(R.id.listView);
    }

    public void setListView(){
        //setting up the list view with the movies retrieved from database
        final ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, details);
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = (String) listView.getItemAtPosition(position);
            }
        });
    };

    public void lookup(View view) {
        //setting empty list view before adding any new elements
        details = new String [0];
        setListView();

        String userInput = userEntry.getText().toString().toLowerCase();

        Cursor data = databaseHelper.getDetailsBasedOnUserInput(userInput);

        if(data.getCount()==0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Nothing with what you entered...",
                    Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        ArrayList<String> tempDetails = new ArrayList<>();
        while(data.moveToNext()){
            if(data.getString(1).toLowerCase().contains(userInput)){
                tempDetails.add("Title: "+data.getString(1));
            }
            if(data.getString(3).toLowerCase().contains(userInput)){
                tempDetails.add("Director: "+data.getString(3));
            }
            if(data.getString(4).toLowerCase().contains(userInput)){
                tempDetails.add("Actor/Actresses: "+data.getString(4));
            }
        }
        //sorting the movies list
        //collator is like a comparator
        Collections.sort(tempDetails, Collator.getInstance());
        //creating a temp array to store movie names of the size of the arraylist
        String[] tempArray = new String [tempDetails.size()];

        //adding movie names from arraylist to array
        for(int x = 0 ; x<tempDetails.size(); x++){
            tempArray[x] = tempDetails.get(x);
        }

        //setting temp array to actual movie array
        details = tempArray;

        setListView();

    }
}