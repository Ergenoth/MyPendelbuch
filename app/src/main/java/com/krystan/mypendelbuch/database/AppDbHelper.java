package com.krystan.mypendelbuch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Helper class for the SQLite database used by this app.<br>
 * This class takes care of the creating the tables and or upgrading the tables if a newer schema version is available
 */

public class AppDbHelper extends SQLiteOpenHelper implements Serializable {
    /* ------------------------------- *
     * Private final static members
     * ------------------------------- */
    private final static String DB_NAME = "fahrtenbuch.db";
    private final static int DB_VERSION = 1;

    /* ------------------------------- *
     * Private static members
     * ------------------------------- */
    private static AppDbHelper appDbHelper = null;

    /* ------------------------------- *
     * Private members
     * ------------------------------- */
    private SQLiteDatabase db = null;
    private Cursor cursor;

    /* ------------------------------- *
     * Private constructor
     * ------------------------------- */
    private AppDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /* ------------------------------- *
     * Public static methods
     * ------------------------------- */
    /**
     * Returns always the latest instance of the internal AppDbHelper object
     *
     * @param context the {@link Context} of the application
     * @return the latest object of {@link AppDbHelper}
     */
    public static AppDbHelper getDbHelper(Context context) {
        if (appDbHelper == null) {
            appDbHelper = new AppDbHelper(context);
        }
        return appDbHelper;
    }

    /* ------------------------------- *
     * Public methods
     * ------------------------------- */
    /**
     * Inserts the given values into the given table name.<br>
     * The values will be extended with the time stamp
     *
     * @param tableName the name of the table to insert the values
     * @param values the values which should be inserted into the table
     */
    public long insert(String tableName, ContentValues values) {
        /*Extent the values with the time stamp. This is for every table the same*/
        Calendar today = Calendar.getInstance();
        values.put(CommonsDB.COLUMN_NAME_DAY, today.get(Calendar.DAY_OF_MONTH));
        int calendarMonth = today.get(Calendar.MONTH) + 1;
        values.put(CommonsDB.COLUMN_NAME_MONTH, calendarMonth);
        values.put(CommonsDB.COLUMN_NAME_YEAR, today.get(Calendar.YEAR));
        values.put(CommonsDB.COLUMN_NAME_HOUR, today.get(Calendar.HOUR_OF_DAY));
        values.put(CommonsDB.COLUMN_NAME_MINUTE, today.get(Calendar.MINUTE));

        /*Insert the values into the requested database*/
        SQLiteDatabase localDB = ensureDB();
        return localDB.insert(tableName, null, values);
    }

    /**
     * Reads all entries from the given table into the defined columns.<br>
     * These columns are extended with the default time stamp columns
     *
     * @param tableName the name of the table to gain the information for
     * @param columns the columns are interested in from the table
     */
    public void openCursor(String tableName, List<String> columns) {
        SQLiteDatabase localDB = ensureDB();
        /*Ensure first if the already open cursor is closed*/
        closeCursor();
        /*Add time stamp to the result list*/
        columns.add(CommonsDB.COLUMN_NAME_DAY);
        columns.add(CommonsDB.COLUMN_NAME_MONTH);
        columns.add(CommonsDB.COLUMN_NAME_YEAR);
        columns.add(CommonsDB.COLUMN_NAME_HOUR);
        columns.add(CommonsDB.COLUMN_NAME_MINUTE);
        /*Read the requested information from the database*/
        cursor = localDB.query(tableName, columns.toArray(new String[0]), null, null, null, null, null);
    }

    /**
     * Closes the cursor which was opened for reading the information from the database
     */
    public void closeCursor() {
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * Reads the next record and moves the information from the result cursor into the result set for further
     * processing by the implementor
     *
     * @param resultSet the result set to receive the information from the database
     */
    public void getNextRecord(IResultSet resultSet) {
        if (cursor != null) {
            if (cursor.moveToNext() == true) {
                for (int index = 0; index < cursor.getColumnCount(); index++) {
                    int columnType = cursor.getType(index);
                    Object columnValue = null;
                    switch (columnType) {
                        case Cursor.FIELD_TYPE_STRING:
                            columnValue = cursor.getString(index);
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            columnValue = cursor.getInt(index);
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            columnValue = cursor.getFloat(index);
                            break;
                    }
                    resultSet.addColumn(cursor.getColumnName(index), columnValue);
                }
            } else {
                resultSet.setAtEnd(true);
            }
        } else {
            resultSet.setAtEnd(true);
        }
    }

    /* ------------------------------- *
     * Implements SQLiteOpenHelper
     * ------------------------------- */
    /** {@inheritDoc} */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CommuneTableContract.SQL_CREATE_TABLE);
        db.execSQL(RefuelTableContract.SQL_CREATE_TABLE);
    }

    /** {@inheritDoc} */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CommuneTableContract.SQL_DELETE_TABLE);
        db.execSQL(RefuelTableContract.SQL_DELETE_TABLE);
        onCreate(db);
    }

    /* ------------------------------- *
     * Private methods
     * ------------------------------- */
    /**
     * Ensures that the database object is all time available
     *
     * @return the database object
     */
    private SQLiteDatabase ensureDB() {
        if (db == null) {
            db = getWritableDatabase();
        }
        return db;
    }
}
