package com.app.didier.gabriel.beerlist;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddBeer extends AppCompatActivity {

    // LinearLayout where all the data to add a beer can be found.
    private LinearLayout addBeerLayout;

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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        addBeerLayout = (LinearLayout) findViewById(R.id.add_beer_layout);
    }

    /** Called when the user clicks the Save Beer button */
    public void addBeerToDB(View view) {

        // Toast duration and message
        int duration = Toast.LENGTH_LONG;
        String message;

        // Find the beer name and verifies that it is not empty
        String beer_name = ((EditText) findViewById(R.id.Name)).getText().toString();
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
        findDataInView(addBeerLayout, data, 0);

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
     * Private method that search a LinearLayout for all EditText and get the values
     * for each of them.
     * @param layout : the layout to search
     * @param data : the container for the datas
     * @param count : the column in the database that correspond to the data
     */
    private void findDataInView(LinearLayout layout, ContentValues data, int count) {
        for(int i = 0; i < addBeerLayout.getChildCount(); i++ ){
            if(layout.getChildAt(i) instanceof  EditText){
                Editable text = ((EditText) layout.getChildAt(i)).getText();
                data.put(DBContract.Beers.beers[count++], text.toString());

            } else if(layout.getChildAt(i) instanceof LinearLayout) {
                findDataInView((LinearLayout) layout.getChildAt(i), data, count);
                count = data.size();
            }
        }
    }
}

