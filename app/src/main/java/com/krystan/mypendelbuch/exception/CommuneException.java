package com.krystan.mypendelbuch.exception;

/**
 * Wrapper Exception for everything related for commune processing
 *
 * @author Robert Duck
 * @since 12.03.2017
 */

public class CommuneException extends Exception {
    /* -------------------------- *
     * Construction
     * -------------------------- */
    public CommuneException(String message) {
        super(message);
    }

    public CommuneException(String message, Throwable cause) {
        super(message, cause);
    }
}
