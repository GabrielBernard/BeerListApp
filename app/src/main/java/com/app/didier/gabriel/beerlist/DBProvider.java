
/**
 * DBProvider.java
 * Created by Gabriel on 16-04-20.
 * Modified on 16-04-27
 */

package com.app.didier.gabriel.beerlist;

import com.app.didier.gabriel.beerlist.DBContract.Beers;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;

import android.content.UriMatcher;
import android.database.Cursor;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Class that serve as a provider to create, update or delete entries in
 * the database.
 * This class extends the ContentProvider class from the android sdk.
 * This means that every call to getContentResolver().SOMETHING(...), are
 * resolved by this provider with the corresponding SOMETHING() function.
 *
 * SOMETHING must either be insert, update, delete or query.
 */
public class DBProvider extends ContentProvider {

    // Database name, version, etc.
    private static final String DATABASE_NAME = "beers.db";
    private static final int DATABASE_VERSION = 1;

    // Important data for SQLite table creation
    private static final String TEXT_TYPE = " TEXT";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    // Creation of the SQLite table with the DBContract
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Beers.TABLE_NAME +
            " (" + Beers._ID + " INTEGER PRIMARY KEY," +
            Beers.COLUMN_NAME_BEER_NAME + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_BREWERY + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_COLOR + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_ALCOHOL_CONTENT + DOUBLE_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_PRICE+ DOUBLE_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_WHERE_BOUGHT + TEXT_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_SCORE + DOUBLE_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_CREATE_DATE + INTEGER_TYPE + COMMA_SEP +
            Beers.COLUMN_NAME_MODIFICATION_DATE + INTEGER_TYPE + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Beers.TABLE_NAME;

    // Database helper
    private DatabaseHelper openHelper;

    // URI matcher
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /**
     * Constants used by the Uri matcher to choose an action based on the pattern
     * of the incoming URI
     */
    // Beers URI pattern
    private static final int BEERS = 1;

    // Beer ID URI pattern
    private static final int BEER_ID = 2;

    /**
     * Projection Maps used by the Query Builder
     */
    private static HashMap<String, String> sBeersProjectionMap;

    static
    {
        /**
         * Uri Matcher
         */
        sURIMatcher.addURI(DBContract.AUTHORITY, Beers.TABLE_NAME, BEERS);
        sURIMatcher.addURI(DBContract.AUTHORITY, Beers._ID, BEER_ID);

        /**
         * Projection Map implementation
         */
        sBeersProjectionMap = new HashMap<>();
        sBeersProjectionMap.put(Beers._ID, Beers._ID);
        sBeersProjectionMap.put(Beers.COLUMN_NAME_BEER_NAME, Beers.COLUMN_NAME_BEER_NAME);
        sBeersProjectionMap.put(Beers.COLUMN_NAME_BREWERY, Beers.COLUMN_NAME_BREWERY);
        sBeersProjectionMap.put(Beers.COLUMN_NAME_COLOR, Beers.COLUMN_NAME_COLOR);
        sBeersProjectionMap.put(Beers.COLUMN_NAME_ALCOHOL_CONTENT, Beers.COLUMN_NAME_ALCOHOL_CONTENT);
        sBeersProjectionMap.put(Beers.COLUMN_NAME_PRICE, Beers.COLUMN_NAME_PRICE);
        sBeersProjectionMap.put(Beers.COLUMN_NAME_WHERE_BOUGHT, Beers.COLUMN_NAME_WHERE_BOUGHT);
        sBeersProjectionMap.put(Beers.COLUMN_NAME_SCORE, Beers.COLUMN_NAME_SCORE);
        sBeersProjectionMap.put(Beers.COLUMN_NAME_CREATE_DATE, Beers.COLUMN_NAME_CREATE_DATE);
        sBeersProjectionMap.put(Beers.COLUMN_NAME_MODIFICATION_DATE, Beers.COLUMN_NAME_MODIFICATION_DATE);

    }

    /**
     * Class that helps in creating, opening, upgrading and deleting a SQL database.
     * The first time it is called, it will create the database following the BeerListContract.
     * After it will take care of updating the database.
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
            /*Log.w(TAG, "Upgrading from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");*/

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
        // Creation of a database helper
        openHelper = new DatabaseHelper(getContext());
        return true;
    }

    /**
     * Funciton that return the MIME for the URI matcher depending of the entered URI.
     * @param  uri : URI used to access the SQLite Database.
     * @return : The MIME for the URI matcher.
     */
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case (BEERS):
                return Beers.CONTENT_TYPE;
            case (BEER_ID):
                return Beers.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        // Create a query builder for the beers SQL database
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Beers.TABLE_NAME);

        // Build a query with the good parameters
        switch(sURIMatcher.match(uri)){
            // If attempting to access all the beers
            case(BEERS):
                queryBuilder.setProjectionMap(sBeersProjectionMap);
                break;
            // If attempting to access one beer in particular
            case(BEER_ID):
                queryBuilder.setProjectionMap(sBeersProjectionMap);
                queryBuilder.appendWhere(Beers._ID + "=" + uri.getPathSegments().get(Beers.BEER_ID_PATH_POSITION));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Choose the sort order of the query result
        String order;
        if(TextUtils.isEmpty(sortOrder)) {
            order = Beers.DEFAULT_SORT_ORDER;
        } else {
            order = sortOrder;
        }

        // Opening database in readable mode
        SQLiteDatabase db = openHelper.getReadableDatabase();

        /**
         * Attempting query.
         * If it does not work, the cursor c will be null.
         */
        Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, order);

        // Tells the cursor tu watch this URI for any changes
        if(getContext() != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return c;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues initialValues) {

        if(sURIMatcher.match(uri) != BEERS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(initialValues == null) {
            throw new IllegalArgumentException("Insertion in the beer database must at least contains the name of the beer.");
        }

        long now = System.currentTimeMillis();

        if(!initialValues.containsKey(Beers.COLUMN_NAME_CREATE_DATE)) {
            initialValues.put(Beers.COLUMN_NAME_CREATE_DATE, now);
        }

        if(!initialValues.containsKey(Beers.COLUMN_NAME_MODIFICATION_DATE)){
            initialValues.put(Beers.COLUMN_NAME_MODIFICATION_DATE, now);
        }

        SQLiteDatabase db = openHelper.getWritableDatabase();

        long rowID = db.insert(Beers.TABLE_NAME, Beers.COLUMN_NAME_BEER_NAME, initialValues);

        if(rowID > 0) {
            Uri returnedUri = ContentUris.withAppendedId(Beers.CONTENT_ID_URI_BASE, rowID);

            // Notify observers of changes in data
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(returnedUri, null);


            return returnedUri;
        }

        // Insert did not worked
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String where, String[] whereArgs) {
        if(sURIMatcher.match(uri) != BEERS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        long now = System.currentTimeMillis();

        if(!values.containsKey(Beers.COLUMN_NAME_MODIFICATION_DATE)) {
            values.put(Beers.COLUMN_NAME_MODIFICATION_DATE, now);
        }

        SQLiteDatabase db = openHelper.getWritableDatabase();

        return db.update(Beers.TABLE_NAME, values, where, whereArgs);

        /*if(rowsModified > 0) {
            return rowsModified;
        }*/

        //throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs) {
        if(sURIMatcher.match(uri) != BEERS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = openHelper.getWritableDatabase();

        return db.delete(Beers.TABLE_NAME, where, whereArgs);
    }
}

