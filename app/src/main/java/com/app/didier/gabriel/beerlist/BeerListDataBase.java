package com.app.didier.gabriel.beerlist;

/**
 * Created by Gabriel on 16-04-20.
 */

import com.app.didier.gabriel.beerlist.BeerListDBContract.Beers;

import android.content.ClipDescription;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.ContentProvider.PipeDataWriter;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.LiveFolders;
import android.text.TextUtils;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class BeerListDataBase {

    // Creation of the SQLite table with the DBContract.
    private static final String TAG = "BeerListDataBase";
    private static final String DATABASE_NAME = "beers.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TEXT_TYPE = "TEXT";
    private static final String DOUBLE_TYPE = "REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE" + Beers.TABLE_NAME +
            " (" + Beers._ID + " INTEGER PRIMARY KEY," +
            Beers.COLUMN_NAME_BEER_ID + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_BEER_NAME + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_BREWERY + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_COLOR + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_ALCOHOL_CONTENT + DOUBLE_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_WHERE_BOUGHT + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_SCORE + TEXT_TYPE + COMMA_SEP + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Beers.TABLE_NAME;

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

            db.execSQL("DROP TABLE IF EXISTS beers");

            onCreate(db);
        }
    }
}

