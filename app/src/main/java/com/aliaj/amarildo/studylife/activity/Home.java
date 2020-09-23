package com.aliaj.amarildo.studylife.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aliaj.amarildo.studylife.fragment.F_Notes;
import com.aliaj.amarildo.studylife.fragment.F_Recordings;
import com.aliaj.amarildo.studylife.fragment.F_Schedule;
import com.aliaj.amarildo.studylife.fragment.F_Subjects;
import com.aliaj.amarildo.studylife.fragment.F_Tests;
import com.aliaj.amarildo.studylife.R;

/**
 * Activity principale che gestisce 5 fragment diversi sulla base del
 * click nel NavigationBar da parte dell'utente
 *
 * @see Activity Le Activity sono uno dei blocchi fondamentali delle applicazioni Android.
 *          Servono come punto di ingresso per l'interazione di un utente con un'applicazione.
 *          Un Activity è una cosa singola e focalizzata che l'utente può fare.
 *          Le activity interagiscono con l'utente, quindi la classe Activity si occupa della
 *          creazione di una finestra in cui è possibile inserire la propria UI(interfaccia utente)
 *          con setContentView(View).
 * @see Fragment Un Fragment rappresenta un comportamento o una porzione dell'interfaccia utente
 *          in un Activity. È possibile combinare più fragment in un'unica attività per creare
 *          un'interfaccia utente a più riquadri e riutilizzare un frammento in più attività.
 * @see Context Interfaccia alle informazioni globali su un ambiente applicativo. Questa è una
 *          classe astratta la cui implementazione è fornita dal sistema Android. Consente
 *          l'accesso a risorse e classi specifiche per l'applicazione, nonché le richieste di
 *          operazioni a livello di applicazione come il lancio di Activity, la trasmissione e
 *          la ricezione di Intent, ecc.
 * @see AppCompatActivity Classe base per le Activity che hanno bisogno di un supporto per
 *          la Action Bar.
 * @see NavigationView Rappresenta un menu di navigazione standard per l'applicazione.
 * @see DrawerLayout Il Navigation Drawer è un pannello che mostra le principali opzioni
 *          di navigazione dell'app e sul lato sinistro dello schermo.
 *          È nascosto per la maggior parte del tempo, ma viene rivelato quando l'utente
 *          passa un dito dal bordo sinistro dello schermo oppure, quando l'utente
 *          tocca l'icona dell'applicazione nella barra degli strumenti.
 * @see Toolbar Nella sua forma di base, la barra di azione visualizza il titolo
 *          dell'activity da un lato e un menu di overflow dall'altro.
 *          Anche in questa forma semplice, la barra delle applicazioni fornisce informazioni
 *          utili agli utenti.
 **/

public class Home extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    public static int navIndex = 0; // Indice del fragment

    // Tag utilizzati per la navigazione attraverso i fragment
    private static final String TAG_SUBJECTS = "materie";
    private static final String TAG_SCHEDULE = "orario";
    private static final String TAG_TESTS = "verifiche";
    private static final String TAG_RECORDINGS = "registrazioni";
    private static final String TAG_NOTES = "note";
    public static String CURRENT_TAG = TAG_SUBJECTS;

    private String[] fragmentTitles; // Lista dei titoli dei Fragment

    private Handler handler; // Utilizzato per le transazioni tra Fragment

    /**
     * Primo metodo che viene chiamato alla creazione dell'activity,
     * nonchè inizio del ciclo di vita dell'applicazione
     *
     * @param savedInstanceState Se si salva lo stato dell'applicazione in un bundle (in genere
     *          non persistenti, i dati dinamici in onSaveInstanceState), può essere passato
     *          a onCreate se l'attività deve essere ricreata (ad esempio, la modifica
     *          dell'orientamento) in modo da non perdere informazioni precedenti.
     *          Se non sono stati forniti dati, savedInstanceState è nullo.
     * @see Bundle I bundles vengono generalmente usati per passare dati tra diverse
     *          activity Android. Dipende da quale tipo di valori si desidera passare,
     *          ma i bundle possono contenere tutti i tipi di valori e passarli alla nuova attività.
     * @see Handler Un handler consente di inviare ed elaborare messaggi e Runnable associati
     *          a alla coda dei messaggi di un thread. Ogni istanza Handler è associata a un
     *          singolo thread e alla coda dei messaggi di tale thread.
     * @see Message Definisce un messaggio contenente una descrizione di un oggetto dati arbitrario
     *          che può essere inviato a un handler. Questo oggetto contiene due campi int,
     *          in più un campo Object aggiuntivo che ci evita allocazioni di memoria ulteriori.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        fragmentTitles = getResources().getStringArray(R.array.nav_item_fragment_titles);

        SetUpNavigationView();

        if (savedInstanceState == null) {
            navIndex = 0;
            CURRENT_TAG = TAG_SUBJECTS;
            loadHomeFragment();
        }
    }

    /**
     * Ritorna il fragment rispettivo al click
     * effettuato dall'utente nel navigation menu
     *
     * @see Runnable L'interfaccia Runnable dovrebbe essere implementata da qualsiasi classe
     *          le cui istanze sono destinate ad essere eseguite da un thread.
     *          La classe deve definire un metodo con nessun argomento chiamato run.
     * @see FragmentTransaction Classe utilizzata per la gestione delle transazioni tra diversi
     *          fragment
     */
    private void loadHomeFragment() {
        selectNavMenu();

        setToolbarTitle();

        // Se l'utente seleziona nel menù il fragment gia aperto non fare nulla
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();
            return;
        }

        Runnable pendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (pendingRunnable != null) {
            // Esegue il Runnable sul thread a cui questo handler è collegato.
            handler.post(pendingRunnable);
        }

        drawerLayout.closeDrawers();

        // Dichiaro che il menù è cambiato, quindi deve essere ricreato(e "stampato").
        invalidateOptionsMenu();
    }

    /**
     * Richiama il Fragment Home in base
     * al valore di navItemIndex
     *
     * @return il fragment selezionato
     */
    private Fragment getHomeFragment() {
        switch (navIndex) {
            case 0:
                return new F_Subjects();

            case 1:
                return new F_Schedule();

            case 2:
                return new F_Tests();

            case 3:
                return new F_Recordings();

            case 4:
                return new F_Notes();

            default:
                return new F_Subjects();
        }
    }

    /**
     * Setta il titolo del Fragment
     */
    private void setToolbarTitle() {
        getSupportActionBar().setTitle(fragmentTitles[navIndex]);
    }

    /**
     * "Ascolta" il click sul Menù di Navigazione(Drawer) e lo imposta su "premuto"
     */
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navIndex).setChecked(true);
    }

    /**
     * Impostazioni Navigation View
     *
     * @see Intent Un intent è una descrizione astratta di un'operazione da eseguire.
     *          Può essere utilizzato con startActivity per lanciare un'attività,
     *          broadcastIntent per inviarlo a qualsiasi componente interessato di
     *          BroadcastReceiver e startService(Intent) o bindService(Intent,
     *          ServiceConnection, int) per comunicare con un servizio di sfondo.
     *          Il suo uso più significativo è nel lancio di attività, dove può essere considerato
     *          come la colla tra le attività. È fondamentalmente una struttura di dati passiva
     *          contenente una descrizione astratta di un'azione da eseguire.
     * @see ActionBarDrawerToggle Questa classe fornisce un modo per collegare le funzionalità
     *          di DrawerLayout e il framework ActionBar per implementare il design consigliato
     *          per i navigation drawers.
     */
    private void SetUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // Questo metodo innescherà il click sul navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_materie:
                        navIndex = 0;
                        CURRENT_TAG = TAG_SUBJECTS;
                        break;

                    case R.id.nav_orario:
                        navIndex = 1;
                        CURRENT_TAG = TAG_SCHEDULE;
                        break;

                    case  R.id.nav_verifiche:
                        navIndex = 2;
                        CURRENT_TAG = TAG_TESTS;
                        break;

                    case R.id.nav_registrazioni:
                        navIndex = 3;
                        CURRENT_TAG = TAG_RECORDINGS;
                        break;

                    case R.id.nav_note:
                        navIndex = 4;
                        CURRENT_TAG = TAG_NOTES;
                        break;

                    // Singolo click e azione diretta //

                    case R.id.nav_segnala_bug:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"amarild.aliaj@gmail.com"});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Segnalazione Bug");
                        try {
                            startActivity(Intent.createChooser(intent, "Invio mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(Home.this, "Non ci sono client mail installati", Toast.LENGTH_SHORT).show();
                        }
                        return  true;

                    default:
                        navIndex = 0;
                }

                // Controlla se è stato premuto in caso negativo impostare su premuto
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openDrawer, R.string.closeDrawer) {

            // Chiamato quando un drawer si è "messo" nello stato chiuso.
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            // Chiamato quando un drawer si è impostato nello stato aperto.
            // Il Drawer è interattivo a questo punto.
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Aggiunge lo specifico "ascoltatore"(listeners) alla lista degli "ascolatatori" che
        // verranno notificati sugli eventi del Drawer
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Sincronizza lo stato del dell'indicatore del drawer con il DrawerLayout collegato.
        actionBarDrawerToggle.syncState();
    }

    /**
     * Viene chiamato quando l'activity rileva che l'utente ha premuto
     * la back key
     *
     * @see GravityCompat Compatibilità per accedere a funzionalità più recenti di Gravity.
     * @see Gravity Costanti e strumenti standard per l'immissione di un oggetto all'interno
     *          di un contenitore potenzialmente più grande.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        // Controllo se l'utente è in un fragment diverso da quello principale(indice = 0)
        if (navIndex != 0) {
            navIndex = 0;
            CURRENT_TAG = TAG_SUBJECTS;
            loadHomeFragment();
            return;
        }

        super.onBackPressed();
    }
}