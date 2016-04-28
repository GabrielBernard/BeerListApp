package com.app.didier.gabriel.beerlist;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddBeer extends AppCompatActivity {

    // LinearLayout where all the data to add a beer can be found.
    //private LinearLayout addBeerLayout;
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
        if (toolbar != null) {
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

        //addBeerLayout = (LinearLayout) findViewById(R.id.add_beer_layout);
    }

    /** Called when the user clicks the Save Beer button */
    public void addBeerToDB(View view) {

        // Toast duration and message
        int duration = Toast.LENGTH_LONG;
        String message;

        // Find the beer name and verifies that it is not empty
        EditText editText = (EditText) findViewById(R.id.Name);

        assert editText != null;
        String beer_name = editText.getText().toString();

        if(beer_name.isEmpty()){
            message = getString(R.string.beer_must_have_name);
            Toast toast = Toast.makeText(getBaseContext(), message, duration);
            toast.show();
            return;
        }

        // Define a map containing the values to add to the database
        ContentValues data = new ContentValues();

        // Call a private method that retrieve all the informations
        // given by the user for the new beer
        retrieveDataInView(data);

        // Try to write the beer to the database with the content provider
        getContentResolver().insert(DBContract.Beers.CONTENT_URI, data);

        // Display success message for the write in the database
        message = getString(R.string.beer_saved);
        Toast toast = Toast.makeText(getBaseContext(), message, duration);
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

