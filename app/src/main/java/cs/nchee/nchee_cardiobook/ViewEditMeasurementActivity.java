package cs.nchee.nchee_cardiobook;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ViewEditMeasurementActivity extends AppCompatActivity {
    private TextInputEditText systolicText;
    private TextInputEditText heartrateText;
    private TextInputEditText diastolicText;
    private TextInputEditText commentText;
    private TextView dateText;


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
        Measurement measurement = (Measurement) intent.getSerializableExtra("data");

        systolicText.setText(Integer.toString(measurement.getSystolic()));
        heartrateText.setText(Integer.toString(measurement.getHeartrate()));
        diastolicText.setText(Integer.toString(measurement.getDiastolic()));

        DateFormat dateFormat = new SimpleDateFormat("HH:mm | yyyy-MM-dd");
        dateText.setText(dateFormat.format(measurement.getDateAndTime().getTime()));
        commentText.setText(measurement.getComment());
    }
}
