package com.krystan.mypendelbuch.common;

import android.content.ContentValues;
import android.content.Context;

import com.krystan.mypendelbuch.R;
import com.krystan.mypendelbuch.database.AppDbHelper;
import com.krystan.mypendelbuch.database.CommuneTableContract;
import com.krystan.mypendelbuch.exception.CommuneException;

/**
 * Helper class for dealing with the commune information.<br>
 * Basically this class reads the information from the configuration file and writes the information to the database
 *
 * @author Robert Duck
 * @since 12.03.2017
 */

public class CommuneHelper {
    /* -------------------------- *
     * Private members
     * -------------------------- */
    private Context context;

    /* -------------------------- *
     * Private members
     * -------------------------- */
    public CommuneHelper(Context context) {
        this.context = context;
    }

    /* -------------------------- *
     * Public methods
     * -------------------------- */
    /**
     * Writes the entries form the UI into the database
     *
     * @param work which button was pushed to switch the departure and arrival location
     * @param privateCar if the commune was driven with the private car
     * @throws CommuneException when something went wrong in writing the database entry
     */
    public void writeCommuneEntry(boolean work, Boolean privateCar) throws CommuneException {
        /*Create data bean and read information from configuration file*/
        CommuneDataBean dataBean = new CommuneDataBean(context);
        dataBean.readDefaultSettings();

        /*Write information to database*/
        String departure;
        String arrival;
        if (work == true) {
            departure = dataBean.getHomeLocation();
            arrival = dataBean.getWorkLocation();
        } else {
            departure = dataBean.getWorkLocation();
            arrival = dataBean.getHomeLocation();
        }
        writeCommuneEntryInternal(departure, arrival, dataBean.getDefaultDistance(), privateCar);
    }

    /**
     * Writes the entries form the UI into the database
     *
     * @param work which button was pushed to switch the departure and arrival location
     * @param privateCar if the commune was driven with the private car
     * @param alternativeLocation the alternative location which can be arrival or destination
     * @param alternativeDistance the alternative distance
     * @throws CommuneException when something went wrong in writing the database entry
     */
    public void writeCommuneEntry(boolean work, Boolean privateCar, String alternativeLocation, float alternativeDistance) throws CommuneException {
        /*Create data bean and read information from configuration file*/
        CommuneDataBean dataBean = new CommuneDataBean(context);
        dataBean.readDefaultSettings();

        /*Write information to database*/
        String departure;
        String arrival;
        if (work == true) {
            departure = alternativeLocation;
            arrival = dataBean.getWorkLocation();
        } else {
            departure = dataBean.getWorkLocation();
            arrival = alternativeLocation;
        }
        writeCommuneEntryInternal(departure, arrival, alternativeDistance, privateCar);
    }

    /* -------------------------- *
     * Private methods
     * -------------------------- */
    /**
     *
     * @param departure
     * @param arrival
     * @param distance
     * @param privateCar
     * @throws CommuneException
     */
    private void writeCommuneEntryInternal(String departure, String arrival, float distance, Boolean privateCar) throws CommuneException {
        /*Fill values*/
        ContentValues values = new ContentValues();
        values.put(CommuneTableContract.COLUMN_NAME_DEPARTURE_LOCATION, departure);
        values.put(CommuneTableContract.COLUMN_NAME_ARRIVAL_LOCATION, arrival);
        values.put(CommuneTableContract.COLUMN_NAME_DISTANCE, distance);
        values.put(CommuneTableContract.COLUMN_NAME_PRIVATE_CAR, privateCar.toString());

        /*Write information to database*/
        long returnCode = AppDbHelper.getDbHelper(context).insert(CommuneTableContract.TABLE_NAME, values);
        if (returnCode <= 0) {
            throw new CommuneException(context.getString(R.string.DatabaseEntryError));
        }
    }
}
