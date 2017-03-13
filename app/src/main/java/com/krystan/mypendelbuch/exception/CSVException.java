package com.krystan.mypendelbuch.exception;

/**
 * Wrapper Exception for everything related for working with CSV
 *
 * @author Robert Duck
 * @since 12.03.2017
 */

public class CSVException extends Exception {
    /* -------------------------- *
     * Construction
     * -------------------------- */
    public CSVException(Throwable cause) {
        super(cause);
    }
}
