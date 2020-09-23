package com.aliaj.amarildo.studylife.object;

import java.util.Date;

/**
 * Oggetto Verifica
 */

public class Test {

    private Subject subject;
    private Date date;

    private Date dateWarning;
    private int hour, minute;

    public Test(Subject subject, Date date, Date dateWarning, int hour, int minute){
        this.subject = subject;
        this.date = date;
        this.dateWarning = dateWarning;
        this.hour = hour;
        this.minute = minute;
    }

    public Subject getSubject() {
        return subject;
    }

    public Date getDate() {
        return date;
    }

    public Date getDateWarning() {
        return dateWarning;
    }
}