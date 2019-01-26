package cs.nchee.nchee_cardiobook;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;

public class AddMeasurementActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonDone;              // TODO: Change to app bar.
    private TextInputEditText systolicText;
    private TextInputEditText heartrateText;
    private TextInputEditText diastolicText;
    private TextInputEditText commentText;
    ArrayList<Measurement> measurements;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement);

        buttonDone = findViewById(R.id.bDone);
        buttonDone.setOnClickListener(this);

        systolicText = findViewById(R.id.userSystolic);
        heartrateText = findViewById(R.id.userHeartrate);
        diastolicText = findViewById(R.id.userDiastolic);
        commentText = findViewById(R.id.userComment);
        //measurements = (ArrayList<Measurement>) getIntent().getExtras().get("Pos");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDone:
                Calendar dateAndTime = Calendar.getInstance();
                int systolic = Integer.parseInt(systolicText.getText().toString());
                int diastolic = Integer.parseInt(diastolicText.getText().toString());
                int heartrate = Integer.parseInt(heartrateText.getText().toString());
                String comment = commentText.getText().toString();
                addMeasurement(dateAndTime, systolic, diastolic, heartrate, comment);
                break;
        }
    }

    private void addMeasurement(Calendar _dateAndTime, int _systolic
            ,int _diastolic, int _heartrate, String _comment) {

        Measurement measurement = new Measurement(_dateAndTime,
                _systolic, _diastolic, _heartrate, _comment);

        finish();
    }

}
