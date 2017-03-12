package com.krystan.mypendelbuch;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.krystan.mypendelbuch.common.ActivityHelper;
import com.krystan.mypendelbuch.common.CommuneDataBean;
import com.krystan.mypendelbuch.common.CommuneHelper;
import com.krystan.mypendelbuch.common.SettingsHelper;
import com.krystan.mypendelbuch.csv.CsvExportCommune;
import com.krystan.mypendelbuch.csv.CsvExportRefuel;
import com.krystan.mypendelbuch.database.AppDbHelper;
import com.krystan.mypendelbuch.database.CommuneTableContract;
import com.krystan.mypendelbuch.exception.CommuneException;

public class MainActivity extends AppCompatActivity {
    /* -------------------------------- *
     * Private members
     * -------------------------------- */
    private AppDbHelper dbHelper = null;

    /* -------------------------------- *
     * Public methods
     * -------------------------------- */
    /**
     * Handles the button click from all buttons on them ain activity.<br>
     * The method decides which function will be executed
     *
     * @param view the view or in other words the widget which issued the method call
     */
    public void handleButtonClick(View view) {
        switch (view.getId()) {
            case R.id.ButtonWorkDefault:
                handleCommuneButton(true);
                break;
            case R.id.ButtonDefaultHome:
                handleCommuneButton(false);
                break;
            case R.id.ButtonExportCommune:
                /*Export commune entries*/
                CsvExportCommune csvExportCommune = new CsvExportCommune(dbHelper);
                csvExportCommune.createCSV();
                ActivityHelper.showToast(this, getString(R.string.CSVSuccess));
                break;
            case R.id.ButtonExportRefuel:
                /*Export refuel entries*/
                CsvExportRefuel csvExportRefuel = new CsvExportRefuel(dbHelper);
                csvExportRefuel.createCSV();
                /*Show that everything worked*/
                ActivityHelper.showToast(this, getString(R.string.CSVSuccess));
                break;
            case R.id.ButtonAlternative:
                Intent altIntent = new Intent(this, ActivityAlternative.class);
                startActivity(altIntent);
                break;
            case R.id.ButtonRefuel:
                Intent refuelIntent = new Intent(this, ActivityRefuel.class);
                startActivity(refuelIntent);
                break;
        }
    }

    /* -------------------------------- *
     * Overrides AppCompatActivity
     * -------------------------------- */
    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Load the database object*/
        dbHelper = AppDbHelper.getDbHelper(getApplicationContext());

        /*Check if the application has the permission to write to the external storage*/
        verifyStoragePermissions();
    }

    /** {@inheritDoc} */
    @Override
    protected void onDestroy() {
        /*Close the database when the app is closed*/
        dbHelper.close();
        /*Destroy this activity and the whole application*/
        super.onDestroy();
    }

    /* -------------------------------- *
     * Overrides android.app.Activity
     * -------------------------------- */
    /** {@inheritDoc} */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Check if we handle the menu item*/
        switch(item.getItemId()) {
            case R.id.settings:
                Intent settingsIntent = new Intent(this, ActivitySettings.class);
                startActivity(settingsIntent);
                return true;
        }

        /*Default call to the super method implementation if the menu item is not handled*/
        return super.onOptionsItemSelected(item);
    }

    /* -------------------------------- *
     * Private methods
     * -------------------------------- */
    /**
     * Checks and ensures that the application is able to write to the external storage
     */
    private void verifyStoragePermissions() {
        /*Check if permission is already granted*/
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        /*If there is no permission, the app take the permission and asks the user politely*/
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    /**
     * Handles when one of the commune buttons pressed.<br>
     * Fills the values map with the values designed for the communes table
     *
     * @param work which button was pressed; {@code true} when the work button was pressed; {@code false} when the
     *             button home was pressed
     */
    private void handleCommuneButton(boolean work) {
        CommuneHelper communeHelper = new CommuneHelper(this);
        try {
            communeHelper.writeCommuneEntry(work, isPrivateCar(R.id.ToggleButtonDefaultCar));
        } catch (CommuneException e) {
            ActivityHelper.showToast(this, e.getMessage());
            return;
        }

        /*Show toast that the record was saved*/
        ActivityHelper.showToast(this, getString(R.string.DatabaseEntrySuccess));
    }

    /**
     * Determines if the toggle button for the private car is switched on or off
     *
     * @return {@code true} if the toggle button is switched on; otherwise {@code false}
     */
    private Boolean isPrivateCar(int widgetID) {
        ToggleButton toggleButton = (ToggleButton)findViewById(widgetID);
        Boolean isPrivate = new Boolean(toggleButton.isChecked());
        return isPrivate;
    }
}
