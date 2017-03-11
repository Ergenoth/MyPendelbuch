package com.krystan.mypendelbuch;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.krystan.mypendelbuch.common.ActivityHelper;
import com.krystan.mypendelbuch.common.SettingsHelper;
import com.krystan.mypendelbuch.database.AppDbHelper;
import com.krystan.mypendelbuch.database.CommuneTableContract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ActivityAlternative extends AppCompatActivity {
    /* -------------------------------- *
     * Private members
     * -------------------------------- */
    private AppDbHelper dbHelper = null;
    private SettingsHelper settingsHelper = null;
    private String alternativeLocation = "";
    private float alternativeDistance = 0f;
    private Boolean privateCar = true;

    /* -------------------------------- *
     * Public methods
     * -------------------------------- */
    /**
     * Handles the button click for the buttons on the activity
     *
     * @param view the clicked button
     */
    public void buttonCommuneClick(View view) {
        getEnteredValues();
        if (view.getId() == R.id.ButtonHomeAlternative) {
            writeValues(false);
        } else {
            writeValues(true);
        }
        finish();
    }

    /* -------------------------------- *
     * Overrides AppCompatActivity
     * -------------------------------- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative);

        /*Get the database helper*/
        dbHelper = AppDbHelper.getDbHelper(getApplicationContext());
        /*Get settings helper*/
        settingsHelper = SettingsHelper.getSettingsHelper(getApplicationContext());
    }

    /* -------------------------------- *
     * Private methods
     * -------------------------------- */
    /**
     * Reads the entered values from the activity
     */
    private void getEnteredValues() {
        /*Read alternative location*/
        EditText editAlternativeLocation = (EditText) findViewById(R.id.EditAlternativeLocation);
        alternativeLocation = editAlternativeLocation.getText().toString();

        /*Read alternative distance*/
        EditText editAlternativeDistance = (EditText) findViewById(R.id.EditAlternativeDistance);
        alternativeDistance = ActivityHelper.parseFloatValue(editAlternativeDistance.getText().toString());

        /*Read flat of private car*/
        ToggleButton toggleButtonPrivateCar = (ToggleButton) findViewById(R.id.ToggleButtonAlternativeCar);
        privateCar = toggleButtonPrivateCar.isChecked();
    }

    /**
     * Writes the values from the activity to the database
     */
    private void writeValues(boolean work) {
        /*Fill the values*/
        ContentValues values = new ContentValues();
        if (work == true) {
            values.put(CommuneTableContract.COLUMN_NAME_DEPARTURE_LOCATION, alternativeLocation);
            values.put(CommuneTableContract.COLUMN_NAME_ARRIVAL_LOCATION, settingsHelper.getSettingValue(SettingsHelper.SETTING_WORK));
        } else {
            values.put(CommuneTableContract.COLUMN_NAME_DEPARTURE_LOCATION, settingsHelper.getSettingValue(SettingsHelper.SETTING_WORK));
            values.put(CommuneTableContract.COLUMN_NAME_ARRIVAL_LOCATION, alternativeLocation);
        }
        values.put(CommuneTableContract.COLUMN_NAME_DISTANCE, alternativeDistance);
        values.put(CommuneTableContract.COLUMN_NAME_PRIVATE_CAR, privateCar.toString());

        /*Insert the values into the database*/
        dbHelper.insert(CommuneTableContract.TABLE_NAME, values);

        /*Show toast that the record was saved*/
        Toast savedToast = Toast.makeText(this, "Datenbankeintrag erfolgreich gespeichert", Toast.LENGTH_SHORT);
        savedToast.show();
    }
}
