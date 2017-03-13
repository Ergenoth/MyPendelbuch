package com.krystan.mypendelbuch.csv;

import com.krystan.mypendelbuch.database.AppDbHelper;
import com.krystan.mypendelbuch.database.CommonsDB;
import com.krystan.mypendelbuch.database.CommuneTableContract;
import com.krystan.mypendelbuch.database.ResultSet;
import com.krystan.mypendelbuch.exception.CSVException;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Creates the CSV file for the commune drives
 *
 * @author Robert Duck
 * @since 08.03.2017
 */

public class CsvExportCommune {
    /* --------------------- *
     * Private members
     * --------------------- */
    private AppDbHelper appDbHelper;
    private CSVWriter writer;

    /* --------------------- *
     * Constructor
     * --------------------- */
    public CsvExportCommune(AppDbHelper appDbHelper) {
        this.appDbHelper = appDbHelper;
    }

    /* --------------------- *
     * Public methods
     * --------------------- */
    /**
     * Writes the CSV file with the information from the database
     *
     * @throws CSVException when writing the CSV file fails
     */
    public void createCSV() throws CSVException {
        /*Read values from the database*/
        readDatabase();
        /*Create result set*/
        ResultSet resultSet = new ResultSet();
        appDbHelper.getNextRecord(resultSet);
        if (resultSet.isAtEnd() == false) {
            /*Create header of the CSV file*/
            createCSVHeader();
            /*Parse through the result set*/
            while(resultSet.isAtEnd() == false) {
                writeCSVEntry(resultSet);
                appDbHelper.getNextRecord(resultSet);
            }

        }
        appDbHelper.closeCursor();
        try {
            getCSVFileWriter().close();
            writer = null;
        } catch (IOException e) {
            throw new CSVException(e);
        }
    }

    /* --------------------- *
     * Private methods
     * --------------------- */
    /**
     * Writes an entry into the CSV file
     *
     * @param resultSet the result set from the database
     * @throws CSVException when writing the entry fails
     */
    private void writeCSVEntry(ResultSet resultSet) throws CSVException {
        String[] csvValue = new String[9];
        try {
            csvValue[0] = String.valueOf(resultSet.getColumnValue(CommuneTableContract.COLUMN_NAME_DEPARTURE_LOCATION));
            csvValue[1] = String.valueOf(resultSet.getColumnValue(CommuneTableContract.COLUMN_NAME_ARRIVAL_LOCATION));
            csvValue[2] = String.format(Locale.getDefault(), "%.2f", resultSet.getColumnValue(CommuneTableContract.COLUMN_NAME_DISTANCE));
            csvValue[3] = String.valueOf(resultSet.getColumnValue(CommuneTableContract.COLUMN_NAME_PRIVATE_CAR));
            csvValue[4] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_DAY));
            csvValue[5] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_MONTH));
            csvValue[6] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_YEAR));
            csvValue[7] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_HOUR));
            csvValue[8] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_MINUTE));
            getCSVFileWriter().writeNext(csvValue);
        } catch (Exception e) {
            throw new CSVException(e);
        }
    }

    /**
     * Creates the header for the CSV file
     *
     * @throws CSVException when writing the CSV file fails
     */
    private void createCSVHeader() throws CSVException {
        String[] csvHeader = new String[] {
                CommuneTableContract.COLUMN_NAME_DEPARTURE_LOCATION,
                CommuneTableContract.COLUMN_NAME_ARRIVAL_LOCATION,
                CommuneTableContract.COLUMN_NAME_DISTANCE,
                CommuneTableContract.COLUMN_NAME_PRIVATE_CAR,
                CommonsDB.COLUMN_NAME_DAY,
                CommonsDB.COLUMN_NAME_MONTH,
                CommonsDB.COLUMN_NAME_YEAR,
                CommonsDB.COLUMN_NAME_HOUR,
                CommonsDB.COLUMN_NAME_MINUTE
        };
        getCSVFileWriter().writeNext(csvHeader);
    }

    /**
     * Returns the latest instance of the CSVWriter
     *
     * @return the latest instance of the {@link CSVWriter}
     */
    private CSVWriter getCSVFileWriter() throws CSVException {
        if (writer == null) {
            writer = createFileWriter();
        }
        return writer;
    }

    /**
     * Creates a new CSVWriter if requested
     *
     * @return the newly created {@link CSVWriter}
     * @throws CSVException when writing the CSV file fails
     */
    private CSVWriter createFileWriter() throws CSVException {
        CSVWriter writer = null;

        /*Create the file name*/
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        Calendar calendar = Calendar.getInstance();
        int calendarMonth = calendar.get(Calendar.MONTH) + 1;
        String fileName = "export_commune_" + calendar.get(Calendar.YEAR) + "_" + calendarMonth + "_" + calendar.get(Calendar.DAY_OF_MONTH) + ".csv";
        String filePath = baseDir + File.separator + fileName;
        File file = new File(filePath);

        /*Check if the file is existent, if so, delete it!*/
        if (file.exists() == true) {
            file.delete();
        }

        /*Create the CSVWriter with the file path*/
        try {
            writer = new CSVWriter(new FileWriter(filePath), ';', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER);
        } catch (IOException e) {
            throw new CSVException(e);
        }

        return writer;
    }

    /**
     * Read the values from the database
     */
    private void readDatabase() {
        /*Fill the result columns*/
        List<String> resultColumns = new ArrayList<>();
        resultColumns.add(CommuneTableContract.COLUMN_NAME_DEPARTURE_LOCATION);
        resultColumns.add(CommuneTableContract.COLUMN_NAME_ARRIVAL_LOCATION);
        resultColumns.add(CommuneTableContract.COLUMN_NAME_DISTANCE);
        resultColumns.add(CommuneTableContract.COLUMN_NAME_PRIVATE_CAR);

        /*Read from the database*/
        appDbHelper.openCursor(CommuneTableContract.TABLE_NAME, resultColumns);
    }
}
