package com.krystan.mypendelbuch.common;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by Robert on 11.03.2017.
 */

public abstract class ActivityHelper {
    /**
     * Parses the float value out of a String value. <br>
     * The parsing depends on the default locale set by the VM
     *
     * @param floatValue the string representation of the float value
     * @return the float value of the string
     */
    public static float parseFloatValue(String floatValue) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        Number number = null;
        try {
            number = numberFormat.parse(floatValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return number.floatValue();
    }
}
