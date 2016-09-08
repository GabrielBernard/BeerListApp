package com.app.didier.gabriel.beerlist;

import android.content.ContentValues;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddBeer extends AppCompatActivity {

    private int[] ids = {R.id.Name, R.id.Brewery, R.id.Color,
            R.id.AlcoholContent, R.id.Price, R.id.WhereFound};

    /**
     * This is the method that create the view based on the activity_add_beer.xml
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.AddBeerToolbar);
        assert toolbar != null;
            setSupportActionBar(toolbar);

            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_beer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_beer_insert) {
            addBeerToDB(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Save Beer button */
    public void addBeerToDB(View view) {

        // Toast duration and message
        Toast toast;
        int duration = Toast.LENGTH_LONG;
        String message;

        // Find the beer name and verifies that it is not empty
        EditText editText = (EditText) findViewById(R.id.Name);

        assert editText != null;
        String beer_name = editText.getText().toString();

        if(beer_name.isEmpty()){
            message = getString(R.string.beer_must_have_name);
            toast = Toast.makeText(getBaseContext(), message, duration);
            toast.show();
            return;
        }

        // Define a map containing the values to add to the database
        ContentValues data = new ContentValues();

        // Call a private method that retrieve all the informations
        // given by the user for the new beer
        retrieveDataInView(data);

        try {
            // Try to write the beer to the database with the content provider
            getContentResolver().insert(DBContract.Beers.CONTENT_URI, data);
        } catch (SQLException e){
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

        // Display success message for the write in the database
        message = getString(R.string.beer_saved);
        toast = Toast.makeText(getBaseContext(), message, duration);
        toast.show();

        // End of the add beer view
        finish();
    }

    /**
     * Private method that populate a ContentValues map with the beers information.
     * @param data : the container for the datas
     */
    private void retrieveDataInView(ContentValues data) {

        for(int i = 0; i < ids.length; i++) {
            EditText text = (EditText) findViewById(ids[i]);
            if(text != null) {
                Editable content = text.getText();
                data.put(DBContract.Beers.beers[i], content.toString());
            }
        }
    }
}

