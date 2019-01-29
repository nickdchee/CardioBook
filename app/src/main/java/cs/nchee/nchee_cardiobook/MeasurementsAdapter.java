package cs.nchee.nchee_cardiobook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class MeasurementsAdapter extends RecyclerView.Adapter<MeasurementsAdapter.MyViewHolder> {

    private MainActivity mainActivity;          // the entire activity to get information

    public MeasurementsAdapter(MainActivity _mainActivity) {
        mainActivity = _mainActivity;
    }

    // MyViewHolder handles measurements recycler view's objects.
    // provides access to all the views
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dateAndTimeTextView;
        private TextView systolicTextView;
        private TextView diastolicTextView;
        private TextView heartrateTextView;
        private TextView commentTextView;
        private ImageButton option;                     // options button for Edit/Delete
        private FloatingActionButton addButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            dateAndTimeTextView = (TextView) itemView.findViewById(R.id.tvDateAndTime);
            systolicTextView = (TextView) itemView.findViewById(R.id.tvSystolic);
            diastolicTextView = (TextView) itemView.findViewById(R.id.tvDiastolic);
            heartrateTextView = (TextView) itemView.findViewById(R.id.tvHeartrate);
            commentTextView = (TextView) itemView.findViewById(R.id.tvComment);
            option = (ImageButton) itemView.findViewById(R.id.ibOption);
            addButton = (FloatingActionButton) itemView.findViewById(R.id.fabMeasurement);
        }

        @Override
        public void onClick(View v){
            int position = getAdapterPosition();
        }

    }

    // create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View measurementView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.measurements_card, parent, false);
        return new MyViewHolder(measurementView);
    }

    // initializes an entry with values in views
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        // TODO: Change measurements to private and use getter instead.
        Measurement currentMeasurement = mainActivity.measurements.get(position);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm | yyyy-MM-dd");
        Calendar dateAndTime = currentMeasurement.getDateAndTime();
        String f_DateAndTime = dateFormat.format(dateAndTime.getTime());
        holder.dateAndTimeTextView.setText(f_DateAndTime);
        holder.systolicTextView.setText(String.format("%d mmHg", currentMeasurement.getSystolic()));
        holder.diastolicTextView.setText(String.format("%d mmHg", currentMeasurement.getDiastolic()));
        holder.heartrateTextView.setText(String.format("%d bpm", currentMeasurement.getHeartrate()));
        holder.commentTextView.setText(currentMeasurement.getComment());

        // create event listener for option button
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);

                // inflate popup menu from an xml
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
                popup.show();
            }

            void clickEdit() {
                // create intents here and start new activity
                Intent intent = new Intent(holder.itemView.getContext(),
                        ViewEditMeasurementActivity.class);
                intent.putExtra("data", mainActivity.measurements.get(holder.getAdapterPosition()));

                holder.itemView.getContext().startActivity(intent);
                notifyItemChanged(holder.getAdapterPosition());
            }

            void clickDelete() {
                // delete entry based on position
                // TODO: Make this get an instance of the measurement from backend.
                mainActivity.getMeasurements().remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainActivity.measurements.size();
    }
}
