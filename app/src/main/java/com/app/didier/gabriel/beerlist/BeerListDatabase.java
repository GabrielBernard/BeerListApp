package com.app.didier.gabriel.beerlist;

/**
 * Created by Gabriel on 16-04-20.
 */

import com.app.didier.gabriel.beerlist.BeerListDBContract.Beers;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.net.Uri;

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class BeerListDatabase extends ContentProvider {

    // Database name, version, etc.
    private static final String TAG = "BeerListDatabase";
    private static final String DATABASE_NAME = "beers.db";
    private static final int DATABASE_VERSION = 0;

    // Important data for SQLite table creation
    private static final String TEXT_TYPE = " TEXT";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    // Creation of the SQLite table with the DBContract
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Beers.TABLE_NAME +
            " (" + Beers._ID + " INTEGER PRIMARY KEY," +
            Beers.COLUMN_NAME_BEER_ID + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_BEER_NAME + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_BREWERY + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_COLOR + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_ALCOHOL_CONTENT + DOUBLE_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_WHERE_BOUGHT + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_SCORE + DOUBLE_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_CREATE_DATE + INTEGER_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_MODIFICATION_DATE + INTEGER_TYPE + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Beers.TABLE_NAME;

    // Database helper
    private DatabaseHelper openHelper;

    /**
     * Class that helps in creating, opening, upgrading and deleting a SQL database.
     */
    static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");

            db.execSQL(SQL_DELETE_ENTRIES);

            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }


    @Override
    public boolean onCreate() {
        openHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
       return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
     return 0;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }
}

