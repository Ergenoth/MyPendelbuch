package com.krystan.mypendelbuch.database;

/**
 * Interface for the result sets from the database
 */

public interface IResultSet {
    /**
     * Sets the end of the result cursor
     *
     * @param isAtEnd {@code true} if there are no more results in the cursor; otherwise {@code false}
     */
    void setAtEnd(boolean isAtEnd);

    /**
     * Signalizes if the result cursor was at the end and therefore no other results
     */
    boolean isAtEnd();

    /**
     * Adds another result column with the result to the result set in order to present the implementor the result
     * without exposing the cursor object
     *
     * @param columnName the name of the column from the result cursor
     * @param value the value of the column from the result cursor
     */
    void addColumn(String columnName, Object value);
}
