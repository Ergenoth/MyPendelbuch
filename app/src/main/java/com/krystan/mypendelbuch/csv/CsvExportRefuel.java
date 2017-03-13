package com.krystan.mypendelbuch.csv;

import com.krystan.mypendelbuch.database.AppDbHelper;
import com.krystan.mypendelbuch.database.CommonsDB;
import com.krystan.mypendelbuch.database.RefuelTableContract;
import com.krystan.mypendelbuch.database.ResultSet;
import com.krystan.mypendelbuch.exception.CSVException;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Result;

/**
 * Creates the CSV file for the refuel entries
 *
 * @author Robert Duck
 * @since 08.03.2017
 */

public class CsvExportRefuel {
    /* ------------------------------ *
     * Private final static members
     * ------------------------------ */

    /* ------------------------------ *
     * Private members
     * ------------------------------ */
    private AppDbHelper appDbHelper;
    private CSVWriter writer;

    /* ------------------------------ *
     * Constructor
     * ------------------------------ */
    public CsvExportRefuel(AppDbHelper appDbHelper) {
        this.appDbHelper = appDbHelper;
    }

    /* ------------------------------ *
     * Public methods
     * ------------------------------ */
    /**
     * Writes the refuel entries from the database into a CSV file
     *
     * @throws CSVException when creating the CSV file fails
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

    /* ------------------------------ *
     * Private methods
     * ------------------------------ */
    /**
     * Writes the currently processed CSV entry into the CSV file
     *
     * @param resultSet the currently processed result set
     * @throws CSVException when writing the entry fails
     */
    private void writeCSVEntry(ResultSet resultSet) throws CSVException {
        try {
            float amount = (float) resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_AMOUNT);
            float distance = (float) resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_DISTANCE);

            String[] csvValue = new String[11];
            csvValue[0] = String.valueOf(resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_REFUEL_LOCATION));
            csvValue[1] = String.format(Locale.getDefault(), "%.2f", resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_OVERALL_DISTANCE));
            csvValue[2] = String.format(Locale.getDefault(), "%.2f", distance);
            csvValue[3] = String.format(Locale.getDefault(), "%.2f", resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_PRICE));
            csvValue[4] = String.format(Locale.getDefault(), "%.2f", amount);
            csvValue[5] = calculateAverage(amount, distance);
            csvValue[6] = String.valueOf((int) resultSet.getColumnValue(CommonsDB.COLUMN_NAME_DAY));
            csvValue[7] = String.valueOf((int) resultSet.getColumnValue(CommonsDB.COLUMN_NAME_MONTH));
            csvValue[8] = String.valueOf((int) resultSet.getColumnValue(CommonsDB.COLUMN_NAME_YEAR));
            csvValue[9] = String.valueOf((int) resultSet.getColumnValue(CommonsDB.COLUMN_NAME_HOUR));
            csvValue[10] = String.valueOf((int) resultSet.getColumnValue(CommonsDB.COLUMN_NAME_MINUTE));
            getCSVFileWriter().writeNext(csvValue);
        } catch (Exception e) {
            throw new CSVException(e);
        }
    }

    /**
     * Calculates the average for the currently processed refuel entry
     *
     * @param amount the amount of gas put into the car
     * @param distance the distance drove since the last refual
     * @return the average for the refuel process
     * @throws CSVException when calculating the average fails
     */
    private String calculateAverage(float amount, float distance) throws CSVException {
        try {
            float average = amount * 100 / distance;
        /*Round the result up to two decimal values*/
            BigDecimal bd = new BigDecimal(Float.toString(average));
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return String.format(Locale.getDefault(), "%.2f", bd.floatValue());
        } catch (NumberFormatException e) {
            throw new CSVException(e);
        }
    }

    /**
     * Creates the CSV header and writes the header to the CSV file
     *
     * @throws CSVException when writing the CSV header fails
     */
    private void createCSVHeader() throws CSVException {
        String[] csvHeader = new String[] {
                RefuelTableContract.COLUMN_NAME_REFUEL_LOCATION,
                RefuelTableContract.COLUMN_NAME_OVERALL_DISTANCE,
                RefuelTableContract.COLUMN_NAME_DISTANCE,
                RefuelTableContract.COLUMN_NAME_PRICE,
                RefuelTableContract.COLUMN_NAME_AMOUNT,
                "average",
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
     * @throws CSVException when writing the CSV file fails
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
        String fileName = "export_refuel_" + calendar.get(Calendar.YEAR) + "_" + calendarMonth + "_" + calendar.get(Calendar.DAY_OF_MONTH) + ".csv";
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
        resultColumns.add(RefuelTableContract.COLUMN_NAME_REFUEL_LOCATION);
        resultColumns.add(RefuelTableContract.COLUMN_NAME_OVERALL_DISTANCE);
        resultColumns.add(RefuelTableContract.COLUMN_NAME_DISTANCE);
        resultColumns.add(RefuelTableContract.COLUMN_NAME_PRICE);
        resultColumns.add(RefuelTableContract.COLUMN_NAME_AMOUNT);

        /*Read from the database*/
        appDbHelper.openCursor(RefuelTableContract.TABLE_NAME, resultColumns);
    }
}
