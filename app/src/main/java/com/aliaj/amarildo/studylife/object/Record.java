package com.aliaj.amarildo.studylife.object;

import java.util.Date;

/**
 * Oggetto Registrazione
 */

public class Record {

    private String name;    // nome della registrazione
    private Date date; // data della registrazione
    private int duration; // durata del file audio in millisecondi


    public Record(String name, Date date, int duration){
        this.name = name;
        this.date = date;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public int getDuration() {
        return duration;
    }
}
