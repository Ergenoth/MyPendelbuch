package com.krystan.mypendelbuch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.krystan.mypendelbuch.common.ActivityHelper;
import com.krystan.mypendelbuch.common.CommuneHelper;
import com.krystan.mypendelbuch.exception.CommuneException;

import java.text.ParseException;

public class ActivityAlternative extends AppCompatActivity {
    /* -------------------------------- *
     * Private members
     * -------------------------------- */
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
        if (view.getId() == R.id.ButtonHomeAlternative) {
            handleCommuneButton(false);
        } else {
            handleCommuneButton(true);
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
    }

    /* -------------------------------- *
     * Private methods
     * -------------------------------- */
    /**
     * Reads the entered values from the activity
     *
     * @throws CommuneException when parsing of the alternative distance fails
     */
    private void getEnteredValues() throws CommuneException {
        /*Read alternative location*/
        EditText editAlternativeLocation = (EditText) findViewById(R.id.EditAlternativeLocation);
        alternativeLocation = editAlternativeLocation.getText().toString();

        /*Read alternative distance*/
        EditText editAlternativeDistance = (EditText) findViewById(R.id.EditAlternativeDistance);
        try {
            alternativeDistance = ActivityHelper.parseFloatValue(editAlternativeDistance.getText().toString());
        } catch (ParseException e) {
            throw new CommuneException(getString(R.string.ErrorParsingNumeric), e);
        }

        /*Read flat of private car*/
        ToggleButton toggleButtonPrivateCar = (ToggleButton) findViewById(R.id.ToggleButtonAlternativeCar);
        privateCar = toggleButtonPrivateCar.isChecked();
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
            getEnteredValues();
            communeHelper.writeCommuneEntry(work, privateCar, alternativeLocation, alternativeDistance);
        } catch (CommuneException e) {
            ActivityHelper.showToast(this, e.getMessage());
            return;
        }

        /*Show toast that the record was saved*/
        ActivityHelper.showToast(this, getString(R.string.DatabaseEntrySuccess));
    }
}
