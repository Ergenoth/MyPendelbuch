package com.krystan.mypendelbuch.common;

import android.content.Context;
import android.widget.Toast;

import com.krystan.mypendelbuch.exception.CommuneException;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Abstract class with abstract helper methods for the various activity in the app
 *
 * @author Robert Duck
 * @since 11.03.2017
 */

public abstract class ActivityHelper {
    /**
     * Parses the float value out of a String value. <br>
     * The parsing depends on the default locale set by the VM
     *
     * @param floatValue the string representation of the float value
     * @return the float value of the string
     * @throws ParseException when parsing the float values fails
     */
    public static float parseFloatValue(String floatValue) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance();
        Number number = null;
        try {
            number = numberFormat.parse(floatValue);
        } catch (ParseException e) {
            throw e;
        }
        return number.floatValue();
    }

    /**
     * Shows a toast
     *
     * @param activityContext the context from the activity calling the toast
     * @param message the message for the toast
     */
    public static void showToast(Context activityContext, String message) {
        Toast.makeText(activityContext, message, Toast.LENGTH_SHORT).show();
    }
}
