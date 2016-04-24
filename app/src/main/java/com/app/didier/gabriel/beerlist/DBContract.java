
/**
 * Created by Gabriel on 16-04-20.
 */

package com.app.didier.gabriel.beerlist;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * This class defines a contract between the DBProvider
 */
public final class DBContract {
    public static final String AUTHORITY = "com.app.didier.gabriel.provider.beers";

    // This prevents the class from being instantiated
    private DBContract(){}

    /**
     * Contract for the beers database.
     */
    public static abstract class Beers implements BaseColumns {
        // The SQL table name <Type: TEXT>
        public static final String TABLE_NAME = "beers";

        /**
         * Column Names for SQL table
         */
        // Column name for a beer's name <Type: TEXT>
        public static final String COLUMN_NAME_BEER_NAME = "name";

        // Column name for a beer's brewery <Type: TEXT>
        public static final String COLUMN_NAME_BREWERY = "brewery";

        // Column name for a beer's color <Type: TEXT>
        public static final String COLUMN_NAME_COLOR = "color";

        // Column name for a beers's alcohol content <TYPE: TEXT>
        public static final String COLUMN_NAME_ALCOHOL_CONTENT = "alcohol";

        // Column name for the locations where a beer can be found <Type: TEXT>
        public static final String COLUMN_NAME_WHERE_BOUGHT = "places";

        // Column for the beer's score <Type: REAL>
        public static final String COLUMN_NAME_SCORE = "score";

        // Column for the beer's creation timestamp <Type: INTEGER>
        public static final String COLUMN_NAME_CREATE_DATE = "created";

        // Column for the beer's modification timestamp <Type: INTEGER>
        public static final String COLUMN_NAME_MODIFICATION_DATE = "modified";

        // ArrayList of all column names
        public static final String[] beers = new String[] {
                COLUMN_NAME_BEER_NAME, COLUMN_NAME_BREWERY, COLUMN_NAME_COLOR,
                COLUMN_NAME_ALCOHOL_CONTENT, COLUMN_NAME_WHERE_BOUGHT, COLUMN_NAME_SCORE,
                COLUMN_NAME_CREATE_DATE, COLUMN_NAME_MODIFICATION_DATE
        };

        /**
         * SQLite default sort order
         */
        public static final String DEFAULT_SORT_ORDER = "name DESC";

        /**
         * URI definitions to use with Android's ContentProvider
         */

        // URI's scheme for this provider's
        private static final String SCHEME = "content://";

        // Path for Beers' URI
        private static final String PATH_BEERS = "/beers";

        // Path for Beer's ID URI
        private static final String PATH_BEER_ID = "/beers/";

        // Relative beer's ID position segment in the URI
        public static final int BEER_ID_PATH_POSITION = 1;

        // The content:// for this table
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_BEERS);

        // Base URI to find a single beer in the beers table
        public static final Uri CONTENT_ID_URI_BASE
                = Uri.parse(SCHEME + AUTHORITY + PATH_BEER_ID);

        // Intent construction pattern or match pattern for a single beer
        // specified by its ID
        public static final Uri CONTENT_ID_URI_PATTERN
                = Uri.parse(SCHEME + AUTHORITY + PATH_BEER_ID + "/#");

        /**
         * The MIME type for the UriMatcher (could be anything until it is unique for each matcher)
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.app.didier.gabriel.beerlist.beers";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.app.didier.gabriel.beerlist.beers";

    }
}

