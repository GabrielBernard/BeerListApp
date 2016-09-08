package com.app.didier.gabriel.beerlist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_beer);
        Bundle extras = getIntent().getExtras();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beerId = Integer.toString(extras.getInt(BeerListActivity.ON_CLICK_BEER_ID));

        columnNameToEditText = new HashMap<>();
        for(int i = 0; i < TO.length; i++) {
            EditText editText = (EditText) findViewById(TO[i]);
            columnNameToEditText.put(FROM_COLUMNS[i], editText);
        }

        cursor = getCursorFromId();

        displayCursorContentInView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_beer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case(R.id.show_beer_menu_delete):
                deleteBeer(null);
                return true;
            case(R.id.show_beer_update):
                updateBeer(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateBeer(View view) {
        ContentValues updates = new ContentValues();
        int rows = 0;

        int duration = Toast.LENGTH_LONG;
        String message;
        Toast toast;

        findUpdates(updates);

        if(updates.size() > 0) {
            if(updates.containsKey(DBContract.Beers.COLUMN_NAME_BEER_NAME)) {
                String beer_name = (String) updates.get(DBContract.Beers.COLUMN_NAME_BEER_NAME);
                if(beer_name.isEmpty()) {
                    message = getString(R.string.beer_must_have_name);
                    toast = Toast.makeText(getBaseContext(), message, duration);
                    toast.show();
                    return;
                }
            }

            try {
                String where = DBContract.Beers._ID + " = " + beerId;
                rows = getContentResolver().update(DBContract.Beers.CONTENT_URI, updates, where, null);
            } catch(SQLException e) {
                message = getString(R.string.beer_already_in_db);
                toast = Toast.makeText(getBaseContext(), message, duration);
                toast.show();
                return;
            } catch (NumberFormatException e) {
                message = getString(R.string.beer_price_problem);
                toast = Toast.makeText(getBaseContext(), message, duration);
                toast.show();
                return;
            }
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
            String cursorValue = value;
            int pos = cursor.getColumnIndex(FROM_COLUMNS[i]);
            if(FROM_COLUMNS[i].equals(DBContract.Beers.COLUMN_NAME_PRICE)){
                int decPos = value.indexOf('.');
                if(decPos >= 0) {
                    cursorValue = value.substring(0, decPos) + value.substring(decPos + 1);
                }
            } else if (FROM_COLUMNS[i].equals(DBContract.Beers.COLUMN_NAME_ALCOHOL_CONTENT)) {
                int decPos = value.indexOf('.');
                if(decPos >=0) {
                    cursorValue = value.substring(0, decPos) + value.substring(decPos + 1);
                }
            }
            if(!cursor.getString(pos).equals(cursorValue)) {
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
            String data = cursor.getString(pos);
            if(FROM_COLUMNS[i].equals(DBContract.Beers.COLUMN_NAME_PRICE) && !data.isEmpty()) {
                double price = Double.parseDouble(data);
                price /= 100;
                data = Double.toString(price);
                if(data.length() <= 3) {
                    data = data.concat("0");
                }
                // Populate fields with extracted properties
                editText.setText(data);
            } else if (FROM_COLUMNS[i].equals(DBContract.Beers.COLUMN_NAME_ALCOHOL_CONTENT) && !data.isEmpty()){
                double alcohol = Double.parseDouble(data);
                alcohol /= 10;
                data = Double.toString(alcohol);
                if(data.length() <= 2) {
                    data = data.concat("0");
                }
                // Populate fields with extracted properties
                editText.setText(data);
            } else {
                // Populate fields with extracted properties
                editText.setText(data);
            }
        }
    }
}
