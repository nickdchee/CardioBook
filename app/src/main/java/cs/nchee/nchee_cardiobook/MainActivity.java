package cs.nchee.nchee_cardiobook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMeasurements;
    private RecyclerView.Adapter measurementsAdapter;
    private RecyclerView.LayoutManager layoutManager;
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
        measurementsAdapter.notifyDataSetChanged();
    }
}
