package com.krystan.mypendelbuch.csv;

import com.krystan.mypendelbuch.database.AppDbHelper;
import com.krystan.mypendelbuch.database.CommonsDB;
import com.krystan.mypendelbuch.database.RefuelTableContract;
import com.krystan.mypendelbuch.database.ResultSet;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Creates the CSV file for the refuel entries
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
    public void createCSV() {
        /*Read values from the database*/
        readDatabase();
        /*Create result set*/
        ResultSet resultSet = new ResultSet();
        appDbHelper.getNextRecord(resultSet);
        if (resultSet.isAtEnd() == false) {
            /*Create header of the CSV file*/
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
            /*Parse through the result set*/
            while(resultSet.isAtEnd() == false) {
                float amount = (float)resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_AMOUNT);
                float distance = (float)resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_DISTANCE);

                String[] csvValue = new String[11];
                csvValue[0] = (String) resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_REFUEL_LOCATION);
                csvValue[1] = String.format(Locale.getDefault(), "%.2f", resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_OVERALL_DISTANCE));
                csvValue[2] = String.format(Locale.getDefault(), "%.2f", distance);
                csvValue[3] = String.format(Locale.getDefault(), "%.2f", resultSet.getColumnValue(RefuelTableContract.COLUMN_NAME_PRICE));
                csvValue[4] = String.format(Locale.getDefault(), "%.2f", amount);

                /*Calculate the average*/
                float average = amount * 100 / distance;
                /*Round the result up to two decimal values*/
                BigDecimal bd = new BigDecimal(Float.toString(average));
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                /*Write the value ot the CSV file*/
                csvValue[5] = String.format(Locale.getDefault(), "%.2f", bd.floatValue());

                /*Put the time stamp into the file*/
                csvValue[6] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_DAY));
                csvValue[7] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_MONTH));
                csvValue[8] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_YEAR));
                csvValue[9] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_HOUR));
                csvValue[10] = String.valueOf((int)resultSet.getColumnValue(CommonsDB.COLUMN_NAME_MINUTE));
                getCSVFileWriter().writeNext(csvValue);
                appDbHelper.getNextRecord(resultSet);
            }

        }
        appDbHelper.closeCursor();
        try {
            getCSVFileWriter().close();
            writer = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ------------------------------ *
     * Private methods
     * ------------------------------ */
    /**
     * Returns the latest instance of the CSVWriter
     *
     * @return the latest instance of the {@link CSVWriter}
     */
    private CSVWriter getCSVFileWriter() {
        if (writer == null) {
            writer = createFileWriter();
        }
        return writer;
    }

    /**
     * Creates a new CSVWriter if requested
     *
     * @return the newly created {@link CSVWriter}
     */
    private CSVWriter createFileWriter() {
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
            e.printStackTrace();
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
