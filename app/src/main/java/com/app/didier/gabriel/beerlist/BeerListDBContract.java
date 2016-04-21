package com.app.didier.gabriel.beerlist;

/**
 * Created by Gabriel on 16-04-20.
 */

import android.net.Uri;
import android.provider.BaseColumns;

public final class BeerListDBContract {
    public static final String PACKAGE = "com.app.didier.gabriel.beerlist";

    // This is a contract class, thus it must not be instantiated
    private BeerListDBContract(){}

    public static abstract class Beers implements BaseColumns {
        public static final String TABLE_NAME = "beers";
        public static final String COLUMN_NAME_BEER_ID = "beersid";
        public static final String COLUMN_NAME_BEER_NAME = "name";
        public static final String COLUMN_NAME_BREWERY = "brewery";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_ALCOHOL_CONTENT = "alcohol";
        public static final String COLUMN_NAME_WHERE_BOUGHT = "where";
        public static final String COLUMN_NAME_SCORE = "score";
    }
}

