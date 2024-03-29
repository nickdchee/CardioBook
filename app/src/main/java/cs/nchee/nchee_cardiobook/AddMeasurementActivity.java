/*
 * Developed by Nicholas Chee on 03/02/19 2:15 AM.
 * Last Modified 02/02/19 11:54 PM.
 * Copyright (c) 2019. All rights reserved.
 */

package cs.nchee.nchee_cardiobook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * AddMeasurementActivity class that launches when the user clicks on the FloatingActionButton
 * on the bottom right corner of the screen. This class differs slightly from
 * the ViewEditMeasurementActivity class in a few ways, i.e. no data being entered from a
 * Measurement object, as well as passing a "result" string as a key.
 */
public class AddMeasurementActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText systolicText;
    private TextInputEditText heartrateText;
    private TextInputEditText diastolicText;
    private TextInputEditText commentText;
    private TextView dateText;

    // for HH:mm without using Calendar.getTime()
    private int hour;
    private int minutes;

    // buttons
    private Button buttonDate;

    // get an instance of the calendar object (NOT MODIFIABLE)
    private DatePickerDialog.OnDateSetListener date;
    final Calendar calendar = Calendar.getInstance();

    /**
     * Initialize all our variables.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement);
        setTitle("New measurement");

        buttonDate = findViewById(R.id.bDate);
        buttonDate.setOnClickListener(this);

        systolicText = findViewById(R.id.userSystolic);
        heartrateText = findViewById(R.id.userHeartrate);
        diastolicText = findViewById(R.id.userDiastolic);
        commentText = findViewById(R.id.userComment);
        dateText = findViewById(R.id.tvEditDateAndTime);

        /**
         * When finishing DatePicker dialog prompt, we set the calendar object
         * to have the dates entered in the DatePickerDialog object.
         * Source: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
         */
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                showTimePicker();
            }

        };
    }

    /**
     * Creates a TimePickerDialog object.
     */
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minutes = minute;
                updateLabel();
            }
        }, hour, minutes, false);
        timePickerDialog.show();
    }

    /**
     * Updates the date TextView object to show the entered values from the dialogs.
     * Source: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
     */
    private void updateLabel() {
        DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm | yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(calendar.getTime());
        String dateTime = Integer.toString(hour) + ":" + Integer.toString(minutes) + " | " + dateString;
        try {
            dateText.setText(dateTimeFormat.format(dateTimeFormat.parse(dateTime)));
            calendar.setTime(dateTimeFormat.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * For checking if date Button is clicked.
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDate:
                new DatePickerDialog(this, R.style.DialogTheme, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            default:
                break;
        }
    }

    /**
     * For checking if required text fields are left blank.
     */
    private boolean isEmpty(TextInputEditText tietText) {
        return (tietText.getText().toString().matches(""));
    }

    /**
     * Add measurements.
     */
    private void addMeasurement(Calendar _dateAndTime, int _systolic
            ,int _diastolic, int _heartrate, String _comment) {

        Measurement measurement = new Measurement(_dateAndTime,
                _systolic, _diastolic, _heartrate, _comment);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", measurement);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    /**
     * Used to add check bar on top right of the app bar.
     * Source: https://stackoverflow.com/questions/35913195/is-any-option-to-add-tick-mark-on-the-right-side-of-the-toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_appbar, menu);
        return true;
    }

    /**
     * Fires when user clicks on the tick button. This checks if any of the entries
     * are left blank. If so, a Toast object is used to notify that the
     * user left a field empty. Otherwise, we add the measurement.
     * Source: https://developer.android.com/training/appbar/actions
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // check if inputs are left blank
                if (isEmpty(systolicText) || isEmpty(diastolicText) || isEmpty(heartrateText)
                        || calendar == null) {
                    Toast.makeText(this, "One or more fields is empty!", Toast.LENGTH_SHORT).show();
                    return false;
                }

                int systolic = Integer.parseInt(systolicText.getText().toString());
                int diastolic = Integer.parseInt(diastolicText.getText().toString());
                int heartrate = Integer.parseInt(heartrateText.getText().toString());
                String comment = commentText.getText().toString();
                addMeasurement(calendar, systolic, diastolic, heartrate, comment);
                return true;

            case R.id.close_settings:
                finish();

            default:
                // if we got here, the user's action was not recognized.
                // invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
