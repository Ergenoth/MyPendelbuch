package com.krystan.mypendelbuch.common;

import android.content.Context;

import com.krystan.mypendelbuch.R;

/**
 * Data bean for holding the information the default commune buttons need.<br>
 * Basically this data bean is used to read the default settings from the configuration file and to provide the read
 * information
 *
 * @author Robert Duck
 * @since 12.03.2017
 */

public class CommuneDataBean {
    /* ------------------------- *
     * Private members
     * ------------------------- */
    private String homeLocation;
    private String workLocation;
    private String defaultDistance;
    private Context applicationContext;

    /* ------------------------- *
     * Constructor
     * ------------------------- */
    public CommuneDataBean(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /* ------------------------- *
     * Public methods
     * ------------------------- */
    /**
     * Returns the home location read from the configuration file
     * @return {@link String} representation from the home location; otherwise {@code null} when there was an error
     * in the settings for the home location
     */
    public String getHomeLocation() {
        String returnValue = homeLocation;
        /*If the return value is for some reason already null do nothing. If not, check if the entered information
        * is usable from the application*/
        if (returnValue != null && returnValue.isEmpty() == true) {
            returnValue = null;
        }
        return returnValue;
    }

    /**
     * Returns the work location read from the configuration file
     * @return {@link String} representation from the work location; otherwise {@code null} when there was an error
     * in the settings for the work location
     */
    public String getWorkLocation() {
        String returnValue = workLocation;
        /*If the return value is for some reason already null do nothing. If not, check if the entered information
        * is usable from the application*/
        if (returnValue != null && returnValue.isEmpty() == true) {
            ActivityHelper.showToast(applicationContext, applicationContext.getString(R.string.SettingsHomeError));
            returnValue = null;
        }
        return returnValue;
    }

    /**
     * Returns the default distance read from the configuration file
     * @return {@link Integer} representation from the default distance; otherwise {@code null} when there was an error
     * in the settings for the work location
     */
    public Integer getDefaultDistance() {
        Integer returnValue;
        /*If the information are not read correctly from the configuration file*/
        if (defaultDistance == null) {
            returnValue = null;
        /*Check if the read information can be transformed into a valid integer*/
        } else {
            try {
                returnValue = Integer.valueOf(defaultDistance);
            } catch (NumberFormatException e) {
                returnValue = null;
                ActivityHelper.showToast(applicationContext, applicationContext.getString(R.string.SettingsDistanceError));
            }
        }
        return returnValue;
    }

    /**
     * Reads the default settings from the configuration file
     */
    public void readDefaultSettings() {
        SettingsHelper settingsHelper = SettingsHelper.getSettingsHelper(applicationContext);
        homeLocation = settingsHelper.getSettingValue(SettingsHelper.SETTING_HOME);
        workLocation = settingsHelper.getSettingValue(SettingsHelper.SETTING_WORK);
        defaultDistance = settingsHelper.getSettingValue(SettingsHelper.SETTING_DISTANCE);
    }
}
