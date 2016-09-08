package com.app.didier.gabriel.beerlist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Gabriel on 16-05-04.
 */
public class BeerAdapter extends CursorAdapter {

    private int layout;
    private int[] to;
    private String[] fromColumns;

    public BeerAdapter(Context context, int layout, Cursor cursor, String[] fromColumns, int[] to,  int flags) {
        super(context, cursor, flags);
        if(fromColumns.length != to.length){
            throw new IllegalArgumentException("String[] and int[] must be the same length");
        }
        if(fromColumns.length<=0 || to.length<=0){
            throw new IllegalArgumentException("Argments must have a length of more than 0");
        }
        this.layout = layout;
        this.to = to;
        this.fromColumns = fromColumns;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        for(int i = 0; i < to.length; i++) {
            // Find fields to populate in inflated template
            TextView textView = (TextView) view.findViewById(to[i]);
            // Extract properties from cursor
            int pos = cursor.getColumnIndexOrThrow(fromColumns[i]);
            String data = cursor.getString(pos);
            if(fromColumns[i].equals(DBContract.Beers.COLUMN_NAME_PRICE) && !data.isEmpty()) {
                double price = Double.parseDouble(data);
                price /= 100;
                data = Double.toString(price);
                if(data.length() <= 3) {
                    data = data.concat("0");
                }
                //int decimal = data.length()-2;
               // String beer_price = data.substring(0, decimal) + "." + data.substring(decimal);
                // Populate fields with extracted properties
                textView.setText(data);
            } else if(fromColumns[i].equals(DBContract.Beers.COLUMN_NAME_ALCOHOL_CONTENT) && !data.isEmpty()) {
                double alcohol = Double.parseDouble(data);
                alcohol /= 10;
                data = Double.toString(alcohol);
                if(data.length() <= 2) {
                    data = data.concat("0");
                }
                // Populate fields with extracted properties
                textView.setText(data);
            } else {
                // Populate fields with extracted properties
                textView.setText(data);
            }
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layout, parent, false);
    }
}
