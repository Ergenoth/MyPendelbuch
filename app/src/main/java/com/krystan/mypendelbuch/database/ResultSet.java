package com.krystan.mypendelbuch.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Result set for the results of a database access cursor
 */

public class ResultSet implements IResultSet {
    /* --------------------- *
     * Private members
     * --------------------- */
    private boolean isAtEnd = false;
    private Map<String, Object> resultColumns;

    /* --------------------- *
     * Constructor
     * --------------------- */
    public ResultSet() {
        resultColumns = new HashMap<>();
    }

    /* --------------------- *
     * Public methods
     * --------------------- */
    /**
     * Returns the value of the given column name
     *
     * @param columnName the name of the column
     * @return the value of the column; if the column can not be found then {@code null}
     */
    public Object getColumnValue(String columnName) {
        return resultColumns.get(columnName);
    }

    /* --------------------- *
     * Implements IResultSet
     * --------------------- */
    /**{@inheritDoc}*/
    @Override
    public void setAtEnd(boolean isAtEnd) {
        this.isAtEnd = isAtEnd;
    }

    /**{@inheritDoc}*/
    @Override
    public boolean isAtEnd() {
        return isAtEnd;
    }

    /**{@inheritDoc}*/
    @Override
    public void addColumn(String columnName, Object value) {
        resultColumns.put(columnName, value);
    }
}
