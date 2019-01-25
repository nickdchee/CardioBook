package cs.nchee.nchee_cardiobook;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        private ImageButton option;                     // options button for Add/Edit/Delete

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            dateAndTimeTextView = (TextView) itemView.findViewById(R.id.tvDateAndTime);
            systolicTextView = (TextView) itemView.findViewById(R.id.tvSystolic);
            diastolicTextView = (TextView) itemView.findViewById(R.id.tvDiastolic);
            heartrateTextView = (TextView) itemView.findViewById(R.id.tvHeartrate);
            commentTextView = (TextView) itemView.findViewById(R.id.tvComment);
            option = (ImageButton) itemView.findViewById(R.id.ibOption);
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Measurement currentMeasurement = mainActivity.measurements.get(position);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm | yyyy-MM-dd");
        Calendar dateAndTime = currentMeasurement.getDateAndTime();
        String f_DateAndTime = dateFormat.format(dateAndTime.getTime());
        holder.dateAndTimeTextView.setText(f_DateAndTime);
        holder.systolicTextView.setText(Integer.toString(currentMeasurement.getSystolic()));
        holder.diastolicTextView.setText(Integer.toString(currentMeasurement.getDiastolic()));
        holder.heartrateTextView.setText(Integer.toString(currentMeasurement.getHeartrate()));
        holder.commentTextView.setText(currentMeasurement.getComment());
    }

    @Override
    public int getItemCount() {
        return mainActivity.measurements.size();
    }
}
