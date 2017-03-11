package com.krystan.mypendelbuch;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.krystan.mypendelbuch.common.ActivityHelper;
import com.krystan.mypendelbuch.database.AppDbHelper;
import com.krystan.mypendelbuch.database.RefuelTableContract;

public class ActivityRefuel extends AppCompatActivity {
    /* ---------------------------------- *
     * Private members
     * ---------------------------------- */
    private String nameGasStation;
    private float amountOverallDistance;
    private float amountDroveDistance;
    private float amountPrice;
    private float amountLiter;

    /* ---------------------------------- *
     * Public methods
     * ---------------------------------- */
    /**
     * Handles all clicks on the buttons of this activity.<br>
     * Currently there is only one button on this activity
     *
     * @param view the widget which caused this click/event
     */
    public void handleButtonClick(View view) {
        switch(view.getId()) {
            case R.id.ButtonSave:
                readActivityEntries();
                writeDatabase();
                finish();
                break;
        }
    }

    /* ---------------------------------- *
     * Overrides AppCompatActivity
     * ---------------------------------- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel);
    }

    /* ---------------------------------- *
     * Private members
     * ---------------------------------- */
    /**
     * Writes the entries entered in this activity into the database
     */
    private void writeDatabase() {
        /*Fill the values*/
        ContentValues values = new ContentValues();
        values.put(RefuelTableContract.COLUMN_NAME_REFUEL_LOCATION, nameGasStation);
        values.put(RefuelTableContract.COLUMN_NAME_OVERALL_DISTANCE, amountOverallDistance);
        values.put(RefuelTableContract.COLUMN_NAME_DISTANCE, amountDroveDistance);
        values.put(RefuelTableContract.COLUMN_NAME_PRICE, amountPrice);
        values.put(RefuelTableContract.COLUMN_NAME_AMOUNT, amountLiter);

        /*Insert the values into the database*/
        AppDbHelper dbHelper = AppDbHelper.getDbHelper(getApplicationContext());
        dbHelper.insert(RefuelTableContract.TABLE_NAME, values);

        /*Show toast that the record was saved*/
        Toast savedToast = Toast.makeText(this, "Datenbankeintrag erfolgreich gespeichert", Toast.LENGTH_SHORT);
        savedToast.show();
    }

    /**
     * Reads all the input fields with the information for the database
     */
    private void readActivityEntries() {
        /*Read the name of the gas station*/
        EditText gasStation = (EditText) findViewById(R.id.EditNameStation);
        nameGasStation = gasStation.getText().toString();

        /*Read the overall distance of the car*/
        EditText overallDistance = (EditText) findViewById(R.id.EditDistanceAll);
        amountOverallDistance = ActivityHelper.parseFloatValue(overallDistance.getText().toString());

        /*Read the drove distance of the car*/
        EditText droveDistance = (EditText) findViewById(R.id.EditDistanceDrove);
        amountDroveDistance = ActivityHelper.parseFloatValue(droveDistance.getText().toString());

        /*Read the amount of price for the last refuel*/
        EditText price = (EditText) findViewById(R.id.EditPrice);
        amountPrice = ActivityHelper.parseFloatValue(price.getText().toString());

        /*Read the amount of liters put in the tank*/
        EditText liter = (EditText) findViewById(R.id.EditAmount);
        amountLiter = ActivityHelper.parseFloatValue(liter.getText().toString());
    }
}
