package com.example.filmographyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Filmography.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "films";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_YEAR = "year";
    private static final String COL_ROLE = "role";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_YEAR + " INTEGER, " +
                COL_ROLE + " TEXT)";
        db.execSQL(createTable);
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, year, role) VALUES ('The Usual Suspects', 1995, 'Kevin Spacey')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (title, year, role) VALUES ('Focus', 2015, 'Will Smith')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addFilm(String title, int year, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_YEAR, year);
        values.put(COL_ROLE, role);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getAllFilms() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor searchByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE UPPER(" + COL_TITLE + ") LIKE UPPER(?)", new String[]{"%" + title + "%"});
    }

    public boolean deleteFilm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }

    // New update method
    public boolean updateFilm(int id, String title, int year, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_YEAR, year);
        values.put(COL_ROLE, role);
        int rowsUpdated = db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated > 0;
    }
}