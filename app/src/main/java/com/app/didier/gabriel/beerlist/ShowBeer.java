package com.app.didier.gabriel.beerlist;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ShowBeer extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] PROJECTION = new String[]{
            DBContract.Beers._ID,
            DBContract.Beers.COLUMN_NAME_BEER_NAME,
            DBContract.Beers.COLUMN_NAME_BREWERY,
            DBContract.Beers.COLUMN_NAME_COLOR,
            DBContract.Beers.COLUMN_NAME_ALCOHOL_CONTENT,
            DBContract.Beers.COLUMN_NAME_PRICE,
            DBContract.Beers.COLUMN_NAME_WHERE_BOUGHT
    };

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_beer);

        Bundle extras = getIntent().getExtras();

        String[] fromColumns = {
                DBContract.Beers.COLUMN_NAME_BEER_NAME,
                DBContract.Beers.COLUMN_NAME_BREWERY,
                DBContract.Beers.COLUMN_NAME_COLOR,
                DBContract.Beers.COLUMN_NAME_ALCOHOL_CONTENT,
                DBContract.Beers.COLUMN_NAME_PRICE,
                DBContract.Beers.COLUMN_NAME_WHERE_BOUGHT
        };

        int[] to = {
                R.id.Show_Name, R.id.Show_Brewery, R.id.Show_Color,
                R.id.Show_AlcoholContent, R.id.Show_Price, R.id.Show_WhereFound
        };

        ListView listView = (ListView) findViewById(R.id.show_beer_listview);

        adapter = new SimpleCursorAdapter(this, R.layout.item_showbeer_listview, null, fromColumns, to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        getLoaderManager().initLoader(2, extras, this);
        listView.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Uri CONTENT_URI = DBContract.Beers.CONTENT_URI;
        String beerId = Integer.toString(arg1.getInt(BeerListActivity.ON_CLICK_BEER_ID));
        String where = DBContract.Beers._ID + " = " + beerId;
        return new CursorLoader(this, CONTENT_URI, PROJECTION, where, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the Cursor is being placed in a CursorAdapter, you should use the
        // swapCursor(null) method to remove any references it has to the
        // Loader's data.
        adapter.swapCursor(null);
    }
}
