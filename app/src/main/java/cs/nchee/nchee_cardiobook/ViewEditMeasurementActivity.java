package cs.nchee.nchee_cardiobook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewEditMeasurementActivity extends AppCompatActivity {
    private TextInputEditText systolicText;
    private TextInputEditText heartrateText;
    private TextInputEditText diastolicText;
    private TextInputEditText commentText;
    private TextView dateText;
    private DatePickerDialog.OnDateSetListener date;
    final Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_measurement);

        systolicText = findViewById(R.id.userSystolic);
        heartrateText = findViewById(R.id.userHeartrate);
        diastolicText = findViewById(R.id.userDiastolic);
        commentText = findViewById(R.id.userComment);
        dateText = findViewById(R.id.tvEditDateAndTime);

        // set everything via intent
        Intent intent = getIntent();
        Measurement measurement = (Measurement) intent.getSerializableExtra("edit");

        systolicText.setText(Integer.toString(measurement.getSystolic()));
        heartrateText.setText(Integer.toString(measurement.getHeartrate()));
        diastolicText.setText(Integer.toString(measurement.getDiastolic()));

        DateFormat dateFormat = new SimpleDateFormat("HH:mm | yyyy-MM-dd");
        dateText.setText(dateFormat.format(measurement.getDateAndTime().getTime()));
        commentText.setText(measurement.getComment());
        // source: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

    }

    // source: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
    private void updateLabel() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm | yyyy-MM-dd");
        dateText.setText(dateFormat.format(calendar.getTime()));
    }

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

    // For checking required fields.
    private boolean isEmpty(TextInputEditText tietText) {
        return (tietText.getText().toString().matches(""));
    }

    private void addMeasurement(Calendar _dateAndTime, int _systolic
            , int _diastolic, int _heartrate, String _comment) {

        Measurement measurement = new Measurement(_dateAndTime,
                _systolic, _diastolic, _heartrate, _comment);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("edit", measurement);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    // source: https://stackoverflow.com/questions/35913195/is-any-option-to-add-tick-mark-on-the-right-side-of-the-toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_appbar, menu);
        return true;
    }

    // source: https://developer.android.com/training/appbar/actions
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

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
