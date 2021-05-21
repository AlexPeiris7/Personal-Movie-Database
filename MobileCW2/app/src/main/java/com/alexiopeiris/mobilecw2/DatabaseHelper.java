package com.alexiopeiris.mobilecw2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //creating table columns
    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "MovieTable";
    private static final String COL1 = "MovieTitle";
    private static final String COL2 = "MovieYear";
    private static final String COL3 = "MovieDirector";
    private static final String COL4 = "MovieActors";
    private static final String COL5 = "MovieRating";
    private static final String COL6 = "MovieReview";
    private static final String COL7 = "MovieFavourite";

    public DatabaseHelper (Context context){
        super(context,TABLE_NAME,null,34);
    }

    //create new table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ TABLE_NAME +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT NOT NULL , " +
                COL2 + " INTEGER NOT NULL , " +
                COL3 + " TEXT NOT NULL , " +
                COL4 + " TEXT NOT NULL , " +
                COL5 + " INTEGER NOT NULL , " +
                COL6 + " TEXT NOT NULL , " +
                COL7 + " TEXT NOT NULL ); ";
        db.execSQL(createTable);
    }

    //check if table already exist if it does drop it
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //will return true if inserted correctly
    public boolean insertData(String title, int year, String director, String actors, int rating, String review,String favourite){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,title);
        contentValues.put(COL2,year);
        contentValues.put(COL3,director);
        contentValues.put(COL4,actors);
        contentValues.put(COL5,rating);
        contentValues.put(COL6,review);
        contentValues.put(COL7,favourite);

        long result = db.insert(TABLE_NAME,null,contentValues);
        //result will be -1 if values werent inserted correctly
        //if correctly anthing >0
        if (result == -1) {
            return false;
        }else{
            return true;
        }
    }
    // method to delete a Record of movie
    public boolean deleteEntry(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where=COL1+ "=?";
        int result = db.delete(TABLE_NAME, where, new String[]{title}) ;
        if (result == -1) {
            return false;
        }else{
            return true;
        }
    }
    //update a movie
    public boolean updateData(String previousTitle,String title, int year, String director, String actors, int rating, String review,String favourite){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,title);
        contentValues.put(COL2,year);
        contentValues.put(COL3,director);
        contentValues.put(COL4,actors);
        contentValues.put(COL5,rating);
        contentValues.put(COL6,review);
        contentValues.put(COL7,favourite);
        String where=COL1+ "=?";

        long result = db.update(TABLE_NAME,contentValues,where,new String[]{previousTitle});
        //result will be -1 if values werent inserted correctly
        //if correctly anthing >0
        if (result == -1) {
            return false;
        }else{
            return true;
        }
    }

    //update only favourite col
    public boolean updateFavourite(String movieTitle, String favourite){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL7,favourite);
        String where=COL1 + "=?";
        long result = db.update(TABLE_NAME,contentValues,where,new String[]{movieTitle});
        //result will be -1 if values werent inserted correctly
        //if correctly anthing >0
        if (result == -1) {
            return false;
        }else{
            return true;
        }
    }

    //get only one movie data
    public Cursor getOneMovieData(String movie){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 +" = "+ '"'+movie+'"' ;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    //get data that contains userinput
    public Cursor getDetailsBasedOnUserInput(String userInput){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COL1 +" LIKE "+'"'+ '%'+userInput+'%'+'"' + " OR " +
                COL3 +" LIKE "+'"'+ '%'+userInput+'%'+'"' + " OR " +
                COL4 +" LIKE "+'"'+ '%'+userInput+'%'+'"'  ;
        System.out.println(query);
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    //get all table data
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
}
