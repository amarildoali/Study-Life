package com.aliaj.amarildo.studylife.object;

import java.util.Date;

/**
 * Oggetto Voto
 */

public class Vote {

    private float vote;
    Date date;

    /**
     * Costruttore
     * @param vote voto in floating point
     * @param date data in Date
     */
    public Vote(float vote, Date date){
        this.vote = vote;
        this.date = date;
    }

    /**
     * Ritorna il voto in formato stringa
     */
    public String VoteToString(){
        return String.valueOf(getVote());
    }

    public float getVote() {
        return vote;
    }

    public Date getDate() {
        return date;
    }

}
