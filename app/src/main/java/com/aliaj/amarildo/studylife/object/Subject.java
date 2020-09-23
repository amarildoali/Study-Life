package com.aliaj.amarildo.studylife.object;

import com.aliaj.amarildo.studylife.utility.Variables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Oggetto Materia
 */

public class Subject {

    private String name;

    public int color;
    private int image;

    private ArrayList<Vote> votes;
    private float average;
    private float target;
    private float toTake;

    public Subject(String title, int color, int image){
        this.name = title;
        SetColor(color);
        SetImage(image);
        votes = new ArrayList<>();
    }

    /**
     * Imposta il colore in base al click effettuato dall'utente nella view
     * @param color il numero(0-7) del colore selezionato
     */
    private void SetColor(int color){
        switch (color){
            case 0:
                this.color = Variables.RED;
                break;

            case 1:
                this.color = Variables.ROSE;
                break;

            case 2:
                this.color = Variables.PURPLE;
                break;

            case 3:
                this.color = Variables.INDIGO;
                break;

            case 4:
                this.color = Variables.BLUE;
                break;

            case 5:
                this.color = Variables.GREEN;
                break;

            case 6:
                this.color = Variables.YELLOW;
                break;

            case 7:
                this.color = Variables.ORANGE;
                break;
        }
    }

    /**
     * Imposta l'immagine in base al click effettuato
     * dall'utente nell'interfaccia grafica
     * @param image il numero(0-7) dell'immagine selezionata
     */
    private void SetImage(int image){
        switch (image){
            case 0:
                this.image= Variables.ART;
                break;

            case 1:
                this.image = Variables.ATOM;
                break;

            case 2:
                this.image = Variables.COMPUTER;
                break;

            case 3:
                this.image = Variables.BOOK;
                break;

            case 4:
                this.image = Variables.MATH;
                break;

            case 5:
                this.image = Variables.MUSIC;
                break;

            case 6:
                this.image = Variables.BALL;
                break;

            case 7:
                this.image = Variables.HISTORY;
                break;
        }
    }

    /**
     * Orina l'arraylist in base al voto
     * @see Collections Questa classe è costituita esclusivamente da metodi statici che operano
     *          sulle Collections. Contiene algoritmi polimorfici che operano sulle Collections,
     *          "wrappers", che restituiscono una nuova Collections sostenuta da una raccolta
     *          specificata e altre probabilità.
     * @see Comparable Questa interfaccia impone un ordine totale sugli oggetti di ogni classe
     *          che lo implementa.
     * @see Comparator Una funzione di confronto, che impone un ordine totale su una certa
     *          collezione di oggetti.
     */
    public void SortVotes(){
        // Ordina l'elenco specificato in base all'ordine indotto dal comparatore specificato.
        Collections.sort(votes, new Comparator<Vote>() {
            @Override
            public int compare(Vote vote1, Vote vote2) {
                return  vote1.date.compareTo(vote2.date);
            }
        });
    }

    /**
     * Calcola la media dei voti della materia scelta
     */
    public void ComputeAverage(){
        float m = 0;
        for (int i = 0; i < votes.size(); i++) {
            m += votes.get(i).getVote();
        }

        if (votes.size() == 0) {
            average = 0;
        } else {
            average = m / votes.size();
        }
    }

    /**
     * Implementa un algoritmo che calcola il voto che devi prendere per rientrare nel
     * tuo obiettivo
     */
    public void ComputeToTake(){
        float x;

        x = (votes.size() + 1)* target;
        for (int i = 0; i < votes.size(); i++) {
            x -= votes.get(i).getVote();
        }

        if (x > 10)
            x = 10;

        toTake = x;
    }

    /**
     * Getter e Setter di tutti i parametri
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public float getAverage() {
        return average;
    }

    public float getTarget() {
        return target;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public float getToTake() {
        return toTake;
    }
}
