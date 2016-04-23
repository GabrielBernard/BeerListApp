package com.app.didier.gabriel.beerlist;

/**
 * Created by Gabriel on 16-04-20.
 */

// import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class defines a contract between the BeerListDatabase
 */
public final class BeerListDBContract {
    public static final String PACKAGE = "com.app.didier.gabriel.beerlist";

    // This prevents the class from being instantiated
    private BeerListDBContract(){}

    /**
     * Contract for the beers database.
     */
    public static abstract class Beers implements BaseColumns {
        // The SQL table name <Type: TEXT>
        public static final String TABLE_NAME = "beers";

        // Column name for a beer's id <Type: INTEGER PRIMARY KEY >
        public static final String COLUMN_NAME_BEER_ID = "beersid";

        // Column name for a beer's name <Type: TEXT>
        public static final String COLUMN_NAME_BEER_NAME = "name";

        // Column name for a beer's brewery <Type: TEXT>
        public static final String COLUMN_NAME_BREWERY = "brewery";

        // Column name for a beer's color <Type: TEXT>
        public static final String COLUMN_NAME_COLOR = "color";

        // Column name for a beers's alcohol content <TYPE: TEXT>
        public static final String COLUMN_NAME_ALCOHOL_CONTENT = "alcohol";

        // Column name for the locations where a beer can be found <Type: TEXT>
        public static final String COLUMN_NAME_WHERE_BOUGHT = "where";

        // Column for the beer's score <Type: REAL>
        public static final String COLUMN_NAME_SCORE = "score";

        // Column for the beer's creation timestamp <Type: INTEGER>
        public static final String COLUMN_NAME_CREATE_DATE = "created";

        // Column for the beer's modification timestamp <Type: INTEGER>
        public static final String COLUMN_NAME_MODIFICATION_DATE = "modified";

    }
}

