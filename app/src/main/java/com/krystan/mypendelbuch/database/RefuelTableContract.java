package com.krystan.mypendelbuch.database;

/**
 * This class is a contract for the database table of the refuels entered by the app.<br>
 * This class can't be instantiated is rather more a set of commands and definitions for the table
 */

public final class RefuelTableContract {
    /* ---------------------------- *
     * Public static final members
     * ---------------------------- */
    public static final String TABLE_NAME = "refuel_entries";
    public static final String COLUMN_NAME_REFUEL_LOCATION = "refuel_location";
    public static final String COLUMN_NAME_OVERALL_DISTANCE = "overall_distance";
    public static final String COLUMN_NAME_DISTANCE = "distance";
    public static final String COLUMN_NAME_PRICE = "price";
    public static final String COLUMN_NAME_AMOUNT= "amount";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    CommonsDB.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    CommonsDB.COLUMN_NAME_DAY + " INTEGER," +
                    CommonsDB.COLUMN_NAME_MONTH + " INTEGER," +
                    CommonsDB.COLUMN_NAME_YEAR + " INTEGER," +
                    CommonsDB.COLUMN_NAME_HOUR + " INTEGER," +
                    CommonsDB.COLUMN_NAME_MINUTE + " INTEGER," +
                    COLUMN_NAME_REFUEL_LOCATION + " TEXT," +
                    COLUMN_NAME_OVERALL_DISTANCE + " REAL," +
                    COLUMN_NAME_DISTANCE + " REAL," +
                    COLUMN_NAME_PRICE + " REAL," +
                    COLUMN_NAME_AMOUNT + " REAL)";

    /* ---------------------------- *
     * Private constructor
     * ---------------------------- */
    private RefuelTableContract() {}
}
