/*
 * Developed by Nicholas Chee on 03/02/19 12:44 AM.
 * Last Modified 03/02/19 12:44 AM.
 * Copyright (c) 2019. All rights reserved.
 */

package cs.nchee.nchee_cardiobook;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

// for serialization
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



/**
 * Main activity for which the user opens to when first launching the app.
 * This activity displays the measurements in a RecyclerView, where each view holder
 * of the list contains a CardView, which in itself contains a measurement's date of entry,
 * diastolic, systolic, heart rate, and optional comments. We serialize and deserialize
 * data here, since the only time a user needs to save his entries will be in this activity.
 * We also pass data to and from this activity through intents.
 */

public class MainActivity extends AppCompatActivity {

    // measurements data to bind adapter to, and to serialize
    private ArrayList<Measurement> measurements;
    private RecyclerView rvMeasurements;
    private MeasurementsAdapter measurementsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    // where we save the measurements to
    private static final String FILENAME = "data.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize all our main variables here

        // find RecyclerView in XML
        rvMeasurements = (RecyclerView) findViewById(R.id.rvMeasurements);

        // creating a linear layout manager for RecyclerView
        layoutManager = new LinearLayoutManager(this);

        // creating an adapter to bind data to XML with a linear layout manager
        measurementsAdapter = new MeasurementsAdapter(this);
        rvMeasurements.setLayoutManager(layoutManager);
        rvMeasurements.setAdapter(measurementsAdapter);
        measurements = new ArrayList<>();

        // button for adding new measurements to the RecyclerView
        FloatingActionButton fab = findViewById(R.id.fabMeasurement);

        /**
         * Passes intent to the AddMeasurement activity, using StartActivityForResult
         * to get back the results of an activity.
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), AddMeasurementActivity.class);

                    // using this to get back the results of an intent
                    // Source: https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
                    startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * Method fired after activity has finished being fired. This is
     * used to get data back from the activity and serialize it to a file.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // get the encoded serialized data using the "result" string as a key
        if (resultCode == Activity.RESULT_OK) {
            Measurement new_measurement = (Measurement) data.getSerializableExtra("result");

            //  nothing was returned, which means that the activity sending the data
            // was from the EditMeasurement activity, which is fired from the adapter class
            if (new_measurement == null) {
                measurementsAdapter.onActivityResult(requestCode, resultCode, data);
                saveInFile();
            } else {
                measurements.add(new_measurement);
                measurementsAdapter.notifyDataSetChanged();
                saveInFile();
            }
        }
    }

    /**
     * Used to notify that something in the data set may or may not have changed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        measurementsAdapter.notifyDataSetChanged();
        saveInFile();
    }

    /**
     * Fired when the app first launches. We need this to deserialize data from the file.
     */
    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
    }

    /**
     * Getter that returns the measurements array list.
     */
    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }

    /**
     * Loads a file using FileInputStream, using gson to serialize data.
     * Source: https://github.com/ta301-ks/lonelyTwitter/
     */
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

    /**
     * Saves data from the measurements ArrayList using FileOutputStream,
     * using gson to serialize data.
     * Source: https://github.com/ta301-ks/lonelyTwitter/
     */
    public void saveInFile() {
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
