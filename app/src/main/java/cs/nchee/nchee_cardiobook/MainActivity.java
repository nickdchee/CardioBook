package cs.nchee.nchee_cardiobook;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMeasurements;
    private RecyclerView.Adapter measurementsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int pos_index;
    public ArrayList<Measurement> measurements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvMeasurements = (RecyclerView) findViewById(R.id.rvMeasurements);

        // creating a linear layout manager for recycler view
        layoutManager = new LinearLayoutManager(this);
        measurementsAdapter = new MeasurementsAdapter(this);
        rvMeasurements.setLayoutManager(layoutManager);
        rvMeasurements.setAdapter(measurementsAdapter);

        measurements = new ArrayList<>();
        measurements.add(new Measurement(Calendar.getInstance(), 1, 1, 1, "Hello world."));
        measurements.add(new Measurement(Calendar.getInstance(), 1, 1, 1, "Hello world."));
        measurementsAdapter.notifyDataSetChanged();

        FloatingActionButton fab = findViewById(R.id.fabMeasurement);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddMeasurementActivity.class);

                // source: https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Measurement new_measurement = (Measurement) data.getSerializableExtra("result");
            measurements.add(new_measurement);
            measurementsAdapter.notifyDataSetChanged();
        }
    }

    // getter for dataset
    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }

    // setter for dataset
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }
}
