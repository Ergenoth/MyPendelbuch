package com.krystan.mypendelbuch.common;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Helper class for the settings of the application.<br>
 * This class is treated a singleton because different activities have to access these information.<br>
 * This class takes care of reading, writing and persisting the configuration information for the application
 */

public class SettingsHelper {
    /* --------------------------------- *
     * Public final static members
     * --------------------------------- */
    public final static String SETTING_HOME = "HomeLocation";
    public final static String SETTING_WORK = "WorkLocation";
    public final static String SETTING_DISTANCE = "DefaultDistance";

    /* --------------------------------- *
     * Private static members
     * --------------------------------- */
    private static SettingsHelper singleton = null;
    private static String FILE_NAME = "appSettings.properties";
    private static Properties settings = null;

    /* --------------------------------- *
     * Private members
     * --------------------------------- */
    private Context applicationContext = null;

    /* --------------------------------- *
     * Private constuctor
     * --------------------------------- */
    private SettingsHelper(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /* --------------------------------- *
     * Public methods
     * --------------------------------- */
    /**
     * Returns the value of a specific setting
     *
     * @param settingName the name of the setting
     * @return the value of the setting; if the setting is not in the file an empty String
     */
    public String getSettingValue(String settingName) {
        Properties settingsLocal = getProperties();
        return settingsLocal.getProperty(settingName, "");
    }

    /**
     * Writes the value to the setting name into the properties object of this application
     *
     * @param settingName the name of the stting
     * @param settingValue the value for the setting
     */
    public void writeSettingValue(String settingName, String settingValue) {
        Properties settingsLocal = getProperties();
        settingsLocal.setProperty(settingName, settingValue);
    }

    /**
     * Writes the properties from the object into a file onto the devices memory
     */
    public void persistProperties() {
        Properties settingsLocal = getProperties();
        try {
            FileOutputStream fos = applicationContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            settingsLocal.store(fos, "Configuration");
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* --------------------------------- *
     * Public static methods
     * --------------------------------- */
    /**
     * Provider for the singleton instance of this configuration instance.<br>
     * A singleton because the informatino of the configuratio have to be accessed over several activities
     *
     * @param applicationContext the application context
     * @return the singleton object of {@link SettingsHelper}
     */
    public static SettingsHelper getSettingsHelper(Context applicationContext) {
        if (singleton == null) {
            singleton = new SettingsHelper(applicationContext);
        }
        return singleton;
    }

    /* --------------------------------- *
     * Private methods
     * --------------------------------- */
    /**
     * Returns the singleton object of the properties.<br>
     * When the object is created, the properties are getting loaded from the default file name
     *
     * @return the singleton object of {@link Properties}
     */
    private Properties getProperties() {
        if (settings == null) {
            settings = new Properties();
            try {
                FileInputStream fis = applicationContext.openFileInput(FILE_NAME);
                settings.load(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return settings;
    }
}
