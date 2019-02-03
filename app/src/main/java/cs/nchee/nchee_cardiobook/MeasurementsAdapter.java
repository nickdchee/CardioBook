/*
 * Developed by Nicholas Chee on 03/02/19 12:52 AM.
 * Last Modified 30/01/19 7:15 PM.
 * Copyright (c) 2019. All rights reserved.
 */

package cs.nchee.nchee_cardiobook;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Measurements adapter for connecting the measurements data to
 * the RecyclerView in our MainActivity activity. This also handles a lot
 * of the actions for each CardView item, such as highlighting the colors
 * of alerted systolic and diastolic readings, as well as editing measurements from
 * a drop down PopupMenu.
 */

class MeasurementsAdapter extends RecyclerView.Adapter<MeasurementsAdapter.MyViewHolder> {

    // passed reference to entire activity to get access to all its member variables
    private MainActivity mainActivity;
    private int currentPos;

    public MeasurementsAdapter(MainActivity _mainActivity) {
        mainActivity = _mainActivity;
    }

    /** MyViewHolder handles measurements recycler view's objects,
     * provides access to all the views, and implements OnClickListeners for the
     * options drop down PopupMenu.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // all objects in the XML that need reference to
        private TextView dateAndTimeTextView;
        private TextView systolicTextView;
        private TextView diastolicTextView;
        private TextView heartrateTextView;
        private TextView commentTextView;
        private ImageButton option;                         // options button for Edit/Delete

        /** MyViewHolder constructor class that takes a view. Note: I use a CardView to house
         * my TextView items.
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            // gets all the view objects from the CardView
            dateAndTimeTextView = (TextView) itemView.findViewById(R.id.tvDateAndTime);
            systolicTextView = (TextView) itemView.findViewById(R.id.tvSystolic);
            diastolicTextView = (TextView) itemView.findViewById(R.id.tvDiastolic);
            heartrateTextView = (TextView) itemView.findViewById(R.id.tvHeartrate);
            commentTextView = (TextView) itemView.findViewById(R.id.tvComment);
            option = (ImageButton) itemView.findViewById(R.id.ibOption);
        }

        /**
         * Handles clicks for the ViewHolder of the RecyclerView. Note that
         * this is left blank because I didn't find a use for this.
         */
        @Override
        public void onClick(View v){
        }

    }

    /**
     * When a ViewHolder is created, we inflate its contents with the measurements_card CardView
     * in the XML file.
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View measurementView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.measurements_card, parent, false);
        return new MyViewHolder(measurementView);
    }

    /**
     * Called when we bind the ViewHolder. Here, we assign the the TextView objects
     * their respective entries to show in the MainActivity activity.
     */
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        // assigning items to the TextView objects

        // get the current Measurement's entries, and format each entry to be
        // place into the TextView objects
        Measurement currentMeasurement = mainActivity.measurements.get(position);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm | yyyy-MM-dd");
        Calendar dateAndTime = currentMeasurement.getDateAndTime();
        String f_DateAndTime = dateFormat.format(dateAndTime.getTime());

        // assigning to TextView here
        holder.dateAndTimeTextView.setText(f_DateAndTime);
        holder.systolicTextView.setText(String.format("%d mmHg", currentMeasurement.getSystolic()));

        // color the text red if systolic is noteworthy and set it
        if (currentMeasurement.getSystolic() < 90 || currentMeasurement.getSystolic() > 140) {
            holder.diastolicTextView.setTextColor(Color.RED);
        }

        holder.diastolicTextView.setText(String.format("%d mmHg", currentMeasurement.getDiastolic()));

        // color the text red if systolic is noteworthy and set it
        if (currentMeasurement.getDiastolic() < 60 || currentMeasurement.getDiastolic() > 90) {
            holder.systolicTextView.setTextColor(Color.RED);
        }

        holder.heartrateTextView.setText(String.format("%d bpm", currentMeasurement.getHeartrate()));

        // set the comment
        holder.commentTextView.setText(currentMeasurement.getComment());

        /**
         * Handles on clicks for the options button on the top right corner of the
         * ViewHolder. Here, we create a new PopupMenu object and inflate it using
         * the measurements_option menu.
         */
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);

                // inflate PopupMenu from XML
                popup.inflate(R.menu.menu_measurements_option);

                // adding listeners for each item in the popup
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itemViewEdit:
                                clickEdit();
                                return true;
                            case R.id.itemDelete:
                                clickDelete();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                // show the popup afterwards
                popup.show();
            }

            /**
             * Called when Edit is clicked on the PopupMenu. This creates an intent
             * to the EditMeasurements activity and passes in the Measurements object that the
             * PopupMenu object is referenced in.
             */
            void clickEdit() {
                // create intents here and start new activity
                Intent intent = new Intent(holder.itemView.getContext(),
                        ViewEditMeasurementActivity.class);
                currentPos = holder.getAdapterPosition();

                // pass Measurement object that user is trying to edit
                // with key = "edit"
                intent.putExtra("edit",
                        mainActivity.measurements.get(holder.getAdapterPosition()));
                // this gets called in MainActivity, which invokes onActivityResult
                // in this class
                mainActivity.startActivityForResult(intent, 1);
            }

            /**
             * Called when the user clicks on the Delete option in the PopupMenu object.
             * We notify the adapter that the data set has changed and serialize to the file.
             */
            void clickDelete() {
                // delete entry based on position
                // TODO: Make this get an instance of the measurement from backend.
                mainActivity.getMeasurements().remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                mainActivity.saveInFile();
            }
        });
    }

    /**
     * Uses the same requestCode, resultCode and data from the mainActivity. Note: This
     * is used as a workaround since adapters can't use startActivityForResult.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Measurement edit_measurement = (Measurement) data.getSerializableExtra("edit");
        mainActivity.getMeasurements().set(currentPos, edit_measurement);
        notifyDataSetChanged();
    }

    /**
     * Returns the size of the measurements ArrayList.
     */
    @Override
    public int getItemCount() {
        return mainActivity.measurements.size();
    }
}
