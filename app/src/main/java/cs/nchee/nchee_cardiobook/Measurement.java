
/*
 * Developed by Nicholas Chee on 03/02/19 12:45 AM.
 * Last Modified 30/01/19 2:26 AM.
 * Copyright (c) 2019. All rights reserved.
 */

package cs.nchee.nchee_cardiobook;

import java.io.Serializable;
import java.util.Calendar;

/**
 *  Measurement class that stores information about each measurement entered by the user. The
 *  This class extends the Serializable class, since we want to be able to pass Measurement objects
 *  from one activity to another using intents.
 */

public class Measurement implements Serializable {
    private Calendar dateAndTime;
    private int systolic;
    private int heartrate;
    private int diastolic;
    private String comment;

    /**
     * Measurement constructor.
     */
    public Measurement(Calendar _dateAndTime, int _systolic,
                       int _diastolic, int _heartrate, String _comment) {
        dateAndTime = _dateAndTime;
        systolic = _systolic;
        diastolic = _diastolic;
        heartrate = _heartrate;
        comment = _comment;
    }

    public Calendar getDateAndTime() {
        return dateAndTime;
    }


    public int getDiastolic() {
        return diastolic;
    }


    public int getHeartrate() {
        return heartrate;
    }


    public int getSystolic() {
        return systolic;
    }


    public String getComment() {
        return comment;
    }

}
