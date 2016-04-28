package com.app.didier.gabriel.beerlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ShowBeer extends AppCompatActivity {

    private String beerId;

    private static final String[] FROM_COLUMNS = {
            DBContract.Beers.COLUMN_NAME_BEER_NAME,
            DBContract.Beers.COLUMN_NAME_BREWERY,
            DBContract.Beers.COLUMN_NAME_COLOR,
            DBContract.Beers.COLUMN_NAME_ALCOHOL_CONTENT,
            DBContract.Beers.COLUMN_NAME_PRICE,
            DBContract.Beers.COLUMN_NAME_WHERE_BOUGHT
    };

    private static final String[] PROJECTION = new String[]{
            DBContract.Beers._ID,
            DBContract.Beers.COLUMN_NAME_BEER_NAME,
            DBContract.Beers.COLUMN_NAME_BREWERY,
            DBContract.Beers.COLUMN_NAME_COLOR,
            DBContract.Beers.COLUMN_NAME_ALCOHOL_CONTENT,
            DBContract.Beers.COLUMN_NAME_PRICE,
            DBContract.Beers.COLUMN_NAME_WHERE_BOUGHT
    };

    private static final int[] TO = {
            R.id.Show_Name, R.id.Show_Brewery, R.id.Show_Color,
            R.id.Show_AlcoholContent, R.id.Show_Price, R.id.Show_WhereFound
    };

    private HashMap<String, EditText> columnNameToEditText;

    private Cursor cursor;

    //private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_beer);
        Bundle extras = getIntent().getExtras();

        beerId = Integer.toString(extras.getInt(BeerListActivity.ON_CLICK_BEER_ID));

        columnNameToEditText = new HashMap<>();
        for(int i = 0; i < TO.length; i++) {
            EditText editText = (EditText) findViewById(TO[i]);
            columnNameToEditText.put(FROM_COLUMNS[i], editText);
        }

        cursor = getCursorFromId();

        displayCursorContentInView();
    }


    public void updateBeer(View view) {
        ContentValues updates = new ContentValues();
        int rows = 0;

        int duration = Toast.LENGTH_LONG;
        String message;
        Toast toast;

        findUpdates(updates);

        if(updates.size() > 0) {
            String where = DBContract.Beers._ID + " = " + beerId;
            rows = getContentResolver().update(DBContract.Beers.CONTENT_URI, updates, where, null);
        }
        if(rows > 0) {
            message = getString(R.string.beer_updated);
            toast = Toast.makeText(getBaseContext(), message, duration);
        } else {
            message = getString(R.string.beer_not_updated);
            toast = Toast.makeText(getBaseContext(), message, duration);
        }
        toast.show();
        finish();
    }

    public void deleteBeer(View view) {
        int duration = Toast.LENGTH_LONG;
        String message;
        Toast toast;

        String where = DBContract.Beers._ID + " = " + beerId;
        int rows = getContentResolver().delete(DBContract.Beers.CONTENT_URI, where, null);

        if(rows > 0) {
            message = getString(R.string.beer_deleted);
            toast = Toast.makeText(getBaseContext(), message, duration);
        } else {
            message = getString(R.string.beer_not_deleted);
            toast = Toast.makeText(getBaseContext(), message, duration);
        }
        toast.show();
        finish();
    }

    private void findUpdates(ContentValues content) {
        int count = cursor.getColumnCount();
        cursor.moveToFirst();
        for(int i = 0; i < count - 1 && cursor.isLast(); i++) {

            String value = columnNameToEditText.get(FROM_COLUMNS[i]).getText().toString();
            int pos = cursor.getColumnIndex(FROM_COLUMNS[i]);

            if(!cursor.getString(pos).equals(value)) {
                content.put(FROM_COLUMNS[i], value);
            }
        }
    }


    private Cursor getCursorFromId() {
        Uri CONTENT_URI = DBContract.Beers.CONTENT_URI;
        String where = DBContract.Beers._ID + " = " + beerId;
        return getContentResolver().query(CONTENT_URI, PROJECTION, where, null, null);
    }

    private void displayCursorContentInView() {
        int count = cursor.getColumnCount();
        cursor.moveToFirst();
        for(int i = 0; i < count - 1 && cursor.isLast(); i++) {
            int pos = cursor.getColumnIndex(FROM_COLUMNS[i]);
            EditText editText = columnNameToEditText.get(FROM_COLUMNS[i]);
            editText.setText(cursor.getString(pos));
        }
    }
}
