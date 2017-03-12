package com.krystan.mypendelbuch.common;

import android.content.Context;

import com.krystan.mypendelbuch.R;
import com.krystan.mypendelbuch.exception.CommuneException;

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
     *
     * @return {@link String} representation from the home location
     * @throws CommuneException when there was an error in validating the data
     */
    public String getHomeLocation() throws CommuneException {
        String returnValue = homeLocation;
        /*If the return value is for some reason null or empty, throw an exception*/
        if (returnValue == null || returnValue.isEmpty() == true) {
            throw new CommuneException(applicationContext.getString(R.string.SettingsHomeError));
        }
        return returnValue;
    }

    /**
     * Returns the work location read from the configuration file
     *
     * @return {@link String} representation from the work location
     * @throws CommuneException when there was an error in validating the data
     */
    public String getWorkLocation() throws CommuneException {
        String returnValue = workLocation;
        /*If the return value is for some reason null or empty, throw an exception*/
        if (returnValue == null || returnValue.isEmpty() == true) {
            throw new CommuneException(applicationContext.getString(R.string.SettingsWorkError));
        }
        return returnValue;
    }

    /**
     * Returns the default distance read from the configuration file
     *
     * @return {@link Integer} representation from the default distance
     * @throws CommuneException when there was an error in validating the data
     */
    public Integer getDefaultDistance() throws CommuneException {
        Integer returnValue;
        try {
            returnValue = Integer.valueOf(defaultDistance);
        } catch (NumberFormatException e) {
            throw new CommuneException(applicationContext.getString(R.string.SettingsDistanceError), e);
        } catch (NullPointerException e1) {
            throw new CommuneException(applicationContext.getString(R.string.SettingsDistanceError), e1);
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
