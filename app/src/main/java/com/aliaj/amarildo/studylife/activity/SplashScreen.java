package com.aliaj.amarildo.studylife.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.Variables;

import java.io.File;

/**
 * Splash screen dell'applicazione della durata di 5 secondi.
 * Crea(se non è gia presente) una cartella dove successivamente verrano inseriti
 * tutti i file audio di registrazione creati dall'utente
 *
 * @see Animation Astrazione per un animazione che può essere applicata a View o altri oggetti.
 * @see AnimationSet Rappresenta un gruppo di animazioni che dovrebbero essere riprodotte insieme.
 * @see AlphaAnimation Un animazione che controlla il livello alfa di un oggetto.
 *          Utile per sbiadire le cose dentro e fuori. Questa animazione finisce per cambiare la
 *          proprietà alfa di una Transformation.
 * @see Transformation Definisce la trasformazione da applicare in un determinato istante di
 *          tempo di un animazione.
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView imageView = (ImageView) findViewById(R.id.logo);
        final TextView textView = (TextView) findViewById(R.id.nomeApp);


        // controllo se non esiste gia la dir principale
        File f = new File(Variables.PATH);
        if(!(f.isDirectory())){
            // Non esiste quindi la creo
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Log.d("App", "No SDCARD");
            } else {
                File directory = new File(Variables.PATH);
                directory.mkdirs();
            }
        }


        // Logo
        Animation fadeOut1 = new AlphaAnimation(1, 0);
        fadeOut1.setInterpolator(new AccelerateInterpolator());
        fadeOut1.setStartOffset(4000);
        fadeOut1.setDuration(1000);
        final AnimationSet animation1 = new AnimationSet(false);
        animation1.addAnimation(fadeOut1);

        // Nome app
        Animation fadeIn1 = new AlphaAnimation(0, 1);
        fadeIn1.setInterpolator(new DecelerateInterpolator());
        fadeIn1.setStartOffset(1500);
        fadeIn1.setDuration(2500);
        Animation fadeOut2 = new AlphaAnimation(1, 0);
        fadeOut2.setInterpolator(new AccelerateInterpolator());
        fadeOut2.setStartOffset(4000);
        fadeOut2.setDuration(1000);
        final AnimationSet animation2 = new AnimationSet(false);
        animation2.addAnimation(fadeIn1);
        animation2.addAnimation(fadeOut2);

        Thread timer = new Thread() {
            public void run() {
                try {
                    imageView.setAnimation(animation1);
                    textView.setAnimation(animation2);
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(getApplication(), Home.class));
                    SplashScreen.this.finish();
                }
            }
        };
        timer.start();
    }
}