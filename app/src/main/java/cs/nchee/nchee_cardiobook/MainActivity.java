package cs.nchee.nchee_cardiobook;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String FILENAME = "data.sav";
    private RecyclerView rvMeasurements;
    private MeasurementsAdapter measurementsAdapter;
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

            //  nothing was returned
            if (new_measurement == null) {
                measurementsAdapter.onActivityResult(requestCode, resultCode, data);
                saveInFile();
            }

            else {
                measurements.add(new_measurement);
                measurementsAdapter.notifyDataSetChanged();
                saveInFile();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        measurementsAdapter.notifyDataSetChanged();
        saveInFile();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
    }

    // getter for dataset
    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }

    // setter for dataset
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Measurement>>() {}.getType();
            measurements = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            measurements = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(measurements, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
