package com.app.didier.gabriel.beerlist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class BeerListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter adapter;
    private static final String[] PROJECTION = new String[]{
            DBContract.Beers._ID,
            DBContract.Beers.COLUMN_NAME_BEER_NAME,
            DBContract.Beers.COLUMN_NAME_PRICE
    };
    private String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beer_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.list_view);

        String[] fromColumns = {DBContract.Beers.COLUMN_NAME_BEER_NAME, DBContract.Beers.COLUMN_NAME_PRICE};
        int[] to = {R.id.beer_name, R.id.beer_price};

        adapter = new SimpleCursorAdapter(this, R.layout.item_in_listview, null, fromColumns, to, 0);
        listView.setAdapter(adapter);

        order = DBContract.Beers.DEFAULT_SORT_ORDER;

        getSupportLoaderManager().initLoader(1, null, this);

    }

    /** Called when the user clicks the floating add button */
    public void addBeerWindow(View view){
        Intent intent = new Intent(this, AddBeer.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Uri CONTENT_URI = DBContract.Beers.CONTENT_URI;
        return new CursorLoader(this, CONTENT_URI, PROJECTION, null, null, order);
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
