package com.aliaj.amarildo.studylife.utility;

import android.os.Environment;

import com.aliaj.amarildo.studylife.R;

import java.io.File;

/**
 * Classe che implementa alcune variabili utilizzate nel progetto
 */

public class Variables {

    public static final int RED = R.color.rosso;
    public static final int ROSE = R.color.rosa;
    public static final int PURPLE = R.color.viola;
    public static final int INDIGO = R.color.indaco;
    public static final int BLUE = R.color.blue;
    public static final int GREEN = R.color.verde;
    public static final int YELLOW = R.color.giallo;
    public static final int ORANGE = R.color.arancione;

    public static final int ART = R.drawable.arte;
    public static final int ATOM = R.drawable.atomo;
    public static final int COMPUTER = R.drawable.computer;
    public static final int BOOK = R.drawable.libro;
    public static final int MATH = R.drawable.matematica;
    public static final int MUSIC = R.drawable.musica;
    public static final int BALL = R.drawable.palla;
    public static final int HISTORY = R.drawable.storia;

    public static final int N_MAX_VOTES = 20;

    public static final float SELECTED = 0.20f;

    public static final String PATH = Environment.getExternalStorageDirectory() + File.separator + "Study Life";

    public static final String[] MONTHS = {
            "Gen",
            "Feb",
            "Mar",
            "Apr",
            "Mag",
            "Giu",
            "Lug",
            "Ago",
            "Set",
            "Ott",
            "Nov",
            "Dic"};
}
