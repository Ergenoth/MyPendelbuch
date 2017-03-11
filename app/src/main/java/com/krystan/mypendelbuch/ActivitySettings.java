package com.krystan.mypendelbuch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.krystan.mypendelbuch.common.SettingsHelper;

public class ActivitySettings extends AppCompatActivity {
    /* ------------------------------------ *
     * Private members
     * ------------------------------------ */
    private SettingsHelper helper = null;

    /* ------------------------------------ *
     * Public methods
     * ------------------------------------ */
    public void buttonClickSave(View view) {
        /*Save home location*/
        EditText homeLocation = (EditText) findViewById(R.id.EditSettingsHome);
        helper.writeSettingValue(SettingsHelper.SETTING_HOME, homeLocation.getText().toString());

        /*Save work location*/
        EditText workLocation = (EditText) findViewById(R.id.EditSettingsWork);
        helper.writeSettingValue(SettingsHelper.SETTING_WORK, workLocation.getText().toString());

        /*Save default distance*/
        EditText defaultDistance = (EditText) findViewById(R.id.EditSettingsDistance);
        helper.writeSettingValue(SettingsHelper.SETTING_DISTANCE, defaultDistance.getText().toString());

        /*Save the changed information to the file*/
        helper.persistProperties();

        /*Close activity*/
        finish();
    }

    /* ------------------------------------ *
     * Overrides AppCompatActivity
     * ------------------------------------ */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        /*Loads the settings and shows them*/
        loadSettings();
    }

    /* ------------------------------------ *
     * Private methods
     * ------------------------------------ */
    /**
     * Loads the settings and shows them on the UI
     */
    private void loadSettings() {
        helper = SettingsHelper.getSettingsHelper(getApplicationContext());

        /*Set the home location*/
        EditText homeLocation = (EditText) findViewById(R.id.EditSettingsHome);
        homeLocation.setText(helper.getSettingValue(SettingsHelper.SETTING_HOME));

        /*Set the work location*/
        EditText workLocation = (EditText) findViewById(R.id.EditSettingsWork);
        workLocation.setText(helper.getSettingValue(SettingsHelper.SETTING_WORK));

        /*Set the default distance*/
        EditText defaultDistance = (EditText) findViewById(R.id.EditSettingsDistance);
        defaultDistance.setText(helper.getSettingValue(SettingsHelper.SETTING_DISTANCE));
    }
}
