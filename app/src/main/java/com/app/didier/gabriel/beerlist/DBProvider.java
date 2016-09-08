
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
    private static final String TEXT_UNIQUE = " TEXT NOT NULL UNIQUE";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    // Creation of the SQLite table with the DBContract
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Beers.TABLE_NAME +
            " (" + Beers._ID + " INTEGER PRIMARY KEY," +
            Beers.COLUMN_NAME_BEER_NAME + TEXT_UNIQUE + COMMA_SEP +
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

        if(initialValues.containsKey(Beers.COLUMN_NAME_PRICE)) {
            decimalPriceManagement(initialValues);
        }

        if(initialValues.containsKey(Beers.COLUMN_NAME_ALCOHOL_CONTENT)) {
            decimalAlcoholManagement(initialValues);
        }

        long now = System.currentTimeMillis();

        if(!initialValues.containsKey(Beers.COLUMN_NAME_CREATE_DATE)) {
            initialValues.put(Beers.COLUMN_NAME_CREATE_DATE, now);
        }

        if(!initialValues.containsKey(Beers.COLUMN_NAME_MODIFICATION_DATE)){
            initialValues.put(Beers.COLUMN_NAME_MODIFICATION_DATE, now);
        }

        SQLiteDatabase db = openHelper.getWritableDatabase();

        long rowID = db.insertWithOnConflict(Beers.TABLE_NAME, Beers.COLUMN_NAME_BEER_NAME, initialValues, SQLiteDatabase.CONFLICT_ROLLBACK);

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

        if(values.containsKey(Beers.COLUMN_NAME_PRICE)) {
            decimalPriceManagement(values);
        }

        if(values.containsKey(Beers.COLUMN_NAME_ALCOHOL_CONTENT)) {
            decimalAlcoholManagement(values);
        }

        long now = System.currentTimeMillis();

        if(!values.containsKey(Beers.COLUMN_NAME_MODIFICATION_DATE)) {
            values.put(Beers.COLUMN_NAME_MODIFICATION_DATE, now);
        }

        SQLiteDatabase db = openHelper.getWritableDatabase();

        int rows = db.updateWithOnConflict(Beers.TABLE_NAME, values, where, whereArgs, SQLiteDatabase.CONFLICT_ROLLBACK);
        getContext().getContentResolver().notifyChange(uri, null);

        return rows;
    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs) {
        if(sURIMatcher.match(uri) != BEERS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = openHelper.getWritableDatabase();

        int rows = db.delete(Beers.TABLE_NAME, where, whereArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    private void decimalPriceManagement(ContentValues values) {
        int beer_price;
        String price;
        String value = (String) values.get(Beers.COLUMN_NAME_PRICE);
        if(value.isEmpty()){
            return;
        }
        value = value.replace(',', '.');
        int pos = value.indexOf('.');
        if (pos >= 0) {
            if(pos > 0) {
                price = value.substring(0, pos);
            } else {
                price = "0";
            }
            if (pos + 2 < value.length()) {
                price = price.concat(value.substring(pos+1, pos+3));
            } else if (pos + 1 < value.length()) {
                price = price.concat(value.substring(pos+1, pos+2) + "0");
            } else {
                price = price.concat("00");
            }
        } else {
            price = value + "00";
        }
        beer_price = Integer.parseInt(price);
        values.remove(Beers.COLUMN_NAME_PRICE);
        values.put(Beers.COLUMN_NAME_PRICE, beer_price);
    }

    private void decimalAlcoholManagement(ContentValues values) {
        int alcohol_content;
        String alcohol;
        String value = (String) values.get(Beers.COLUMN_NAME_ALCOHOL_CONTENT);
        if(value.isEmpty()){
            return;
        }
        value = value.replace(',', '.');
        int pos = value.indexOf('.');
        if(pos >= 0) {
            if(pos > 0){
                alcohol = value.substring(0, pos);
            } else {
                alcohol = "0";
            }
            if (pos + 1 < value.length()) {
                alcohol = alcohol.concat(value.substring(pos+1, pos+2));
            } else {
                alcohol = alcohol.concat("0");
            }

        } else {
            alcohol = value + "0";
        }
        alcohol_content = Integer.parseInt(alcohol);
        values.remove(Beers.COLUMN_NAME_ALCOHOL_CONTENT);
        values.put(Beers.COLUMN_NAME_ALCOHOL_CONTENT, alcohol_content);
    }
}

