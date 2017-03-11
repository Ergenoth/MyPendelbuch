package com.krystan.mypendelbuch.database;

/**
 * This class is a contract for the database table of the commune drives entered by the app.<br>
 * This class can't be instantiated is rather more a set of commands and definitions for the table
 */

public final class CommuneTableContract {
    /* ---------------------------- *
     * Public static final members
     * ---------------------------- */
    public static final String TABLE_NAME = "commune_entries";
    public static final String COLUMN_NAME_DEPARTURE_LOCATION = "departure_location";
    public static final String COLUMN_NAME_ARRIVAL_LOCATION = "arrival_location";
    public static final String COLUMN_NAME_DISTANCE = "distance";
    public static final String COLUMN_NAME_PRIVATE_CAR = "private_car";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            CommonsDB.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
            CommonsDB.COLUMN_NAME_DAY + " INTEGER," +
            CommonsDB.COLUMN_NAME_MONTH + " INTEGER," +
            CommonsDB.COLUMN_NAME_YEAR + " INTEGER," +
            CommonsDB.COLUMN_NAME_HOUR + " INTEGER," +
            CommonsDB.COLUMN_NAME_MINUTE + " INTEGER," +
            COLUMN_NAME_DEPARTURE_LOCATION + " TEXT," +
            COLUMN_NAME_ARRIVAL_LOCATION + " TEXT," +
            COLUMN_NAME_DISTANCE + " REAL," +
            COLUMN_NAME_PRIVATE_CAR + " TEXT)";

    /* ---------------------------- *
     * Private constructor
     * ---------------------------- */
    private CommuneTableContract() {}
}
