
package cs.nchee.nchee_cardiobook;


import java.util.Calendar;

/**
 *  Measurement class that stores information about each measurement entered by the user
 */

public class Measurement {
    private Calendar dateAndTime;
    private int systolic;
    private int heartrate;
    private int diastolic;
    private String comment;

    public Measurement(Calendar _dateAndTime, int _systolic, int _diastolic, int _heartrate, String _comment) {
        dateAndTime = _dateAndTime;
        systolic = _systolic;
        diastolic = _diastolic;
        heartrate = _heartrate;
        comment = _comment;
    }

    public Calendar getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Calendar _dateAndTime) {
        dateAndTime = _dateAndTime;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int _diastolic) {
        diastolic = _diastolic;
    }

    public int getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(int _heartrate) {
        heartrate = _heartrate;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int _systolic) {
        systolic = _systolic;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String _comment) {
        comment = _comment;
    }

}
