package com.aliaj.amarildo.studylife.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aliaj.amarildo.studylife.fragment.F_Subjects;
import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.DataManagment;
import com.aliaj.amarildo.studylife.utility.Variables;

/**
 * Classe(Activity) utilizzata per aggiungere/modificare materie
 */

public class NewSubject extends AppCompatActivity implements View.OnClickListener {

    private EditText titleEditText; // view per l'inserimento del titolo
    private ImageButton colorImageButton[]; // array dei colori
    private ImageButton imageImageButton[]; // array delle immagini
    private Button insert;  // bottone per completare l'inserimento


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subject);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SearchViewObjects();
        SetObjects();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NewSubject.this.finish();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Cerca tramite gli id tutti gli elementi grafici
     * da visualizzare in questa activity
     */
    void SearchViewObjects() {
        titleEditText = (EditText) findViewById(R.id.New_Subject_EditText_Nome);

        colorImageButton = new ImageButton[8];
        imageImageButton = new ImageButton[8];

        colorImageButton[0] = (ImageButton) findViewById(R.id.rosso);
        colorImageButton[1] = (ImageButton) findViewById(R.id.rosa);
        colorImageButton[2] = (ImageButton) findViewById(R.id.viola);
        colorImageButton[3] = (ImageButton) findViewById(R.id.indaco);
        colorImageButton[4] = (ImageButton) findViewById(R.id.blue);
        colorImageButton[5] = (ImageButton) findViewById(R.id.verde);
        colorImageButton[6] = (ImageButton) findViewById(R.id.giallo);
        colorImageButton[7] = (ImageButton) findViewById(R.id.arancione);

        imageImageButton[0] = (ImageButton) findViewById(R.id.arte);
        imageImageButton[1] = (ImageButton) findViewById(R.id.atomo);
        imageImageButton[2] = (ImageButton) findViewById(R.id.computer);
        imageImageButton[3] = (ImageButton) findViewById(R.id.libro);
        imageImageButton[4] = (ImageButton) findViewById(R.id.matematica);
        imageImageButton[5] = (ImageButton) findViewById(R.id.musica);
        imageImageButton[6] = (ImageButton) findViewById(R.id.palla);
        imageImageButton[7] = (ImageButton) findViewById(R.id.storia);

        insert = (Button) findViewById(R.id.New_Subject_Button);
    }

    /**
     * Imposta le immagini con le rispettive foto,
     * colora i quadrati e imposta i suggerimenti da mostrare
     * all'utente per l'inserimento del testo
     */
    void SetObjects() {
        titleEditText.setHint(R.string.suggerimento_nome_materia);

        colorImageButton[0].setImageResource(R.color.rosso);
        colorImageButton[1].setImageResource(R.color.rosa);
        colorImageButton[2].setImageResource(R.color.viola);
        colorImageButton[3].setImageResource(R.color.indaco);
        colorImageButton[4].setImageResource(R.color.blue);
        colorImageButton[5].setImageResource(R.color.verde);
        colorImageButton[6].setImageResource(R.color.giallo);
        colorImageButton[7].setImageResource(R.color.arancione);

        imageImageButton[0].setImageResource(R.drawable.arte);
        imageImageButton[1].setImageResource(R.drawable.atomo);
        imageImageButton[2].setImageResource(R.drawable.computer);
        imageImageButton[3].setImageResource(R.drawable.libro);
        imageImageButton[4].setImageResource(R.drawable.matematica);
        imageImageButton[5].setImageResource(R.drawable.musica);
        imageImageButton[6].setImageResource(R.drawable.palla);
        imageImageButton[7].setImageResource(R.drawable.storia);

        // Ciclo utilizzato per settere la dimensione, il colore e il padding delle immagini
        for (int i = 0; i < imageImageButton.length; i++) {
            colorImageButton[i].setAdjustViewBounds(true);
            imageImageButton[i].setAdjustViewBounds(true);

            colorImageButton[i].setBackgroundColor(0);
            imageImageButton[i].setBackgroundColor(0);

            colorImageButton[i].setPadding(20, 20, 20, 20);
            imageImageButton[i].setPadding(20, 20, 20, 20);
        }

        insert.setOnClickListener(this);

        for (int i = 0; i < colorImageButton.length; i++) {
            colorImageButton[i].setOnClickListener(this);
            imageImageButton[i].setOnClickListener(this);
        }
    }

    /**
     * Controlla che il titolo inserito non sia vuoto,
     * e stampa un Toast in caso negativo
     *
     * @return lunghezza del titolo
     */
    boolean CheckTitle() {
        int test = titleEditText.getText().length();

        if (test != 0)
            return true;
        else {
            Toast.makeText(this, R.string.errore_titolo, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Controlla che un colore sia stato SELEZIONATO,
     * ovvero impostata la sua visibilita su 0.5f dove:
     * 0f = trasparente
     * 1f = visibile
     * Stampa un Toast di errore in caso contrario
     *
     * @return l'indice del colore in caso positivo, altrimenti -1
     */
    int CheckColor() {
        int n = 0;
        for (ImageButton i : colorImageButton) {
            if (i.getAlpha() == Variables.SELECTED)
                return n;
            n++;
        }

        Toast.makeText(this, R.string.errore_colore, Toast.LENGTH_SHORT).show();
        return -1;
    }

    /**
     * Controlla che un immagine sia stata selezionata, in caso negativo
     * stampa un Toast
     *
     * @return n l'indice dell'immagine selezionata in caso positivo,
     * altrimenti -1
     */
    int CheckImage() {
        int n = 0;
        for (ImageButton i : imageImageButton) {
            if (i.getAlpha() == Variables.SELECTED)
                return n;
            n++;
        }

        Toast.makeText(this, R.string.errore_immagine, Toast.LENGTH_SHORT).show();
        return -1;
    }

    /**
     * Controlla tutti i settaggi, e in caso tutti fossero tutti
     * positivi aggiunge una materia alla lista materie e stampa
     * un Toast di conferma
     */
    public void Add() {
        boolean a = CheckTitle();
        int c = CheckColor();
        int d = CheckImage();

        if (a && (c != -1) && (d != -1)) {

            F_Subjects.AddSubject(titleEditText.getText().toString(), c, d);
            Toast.makeText(this, "Inserito " + titleEditText.getText().toString(), Toast.LENGTH_SHORT).show();

            DataManagment.Save(getBaseContext(), F_Subjects.arrayListSubjects, 1);

            this.finish();
        }
    }

    /**
     * Funzione dichiarata nell'interfaccia View.OnClickListener implementata
     * in questa classe, che rileva il click di un Button/ImageButton.
     * Viene chiamata quando una View è stata cliccata.
     *
     * @param v View cliccata
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.New_Subject_Button:
                Add();
                break;

            // COLORI
            case R.id.rosso:
                SelectColorButton(0);
                break;
            case R.id.rosa:
                SelectColorButton(1);
                break;
            case R.id.viola:
                SelectColorButton(2);
                break;
            case R.id.indaco:
                SelectColorButton(3);
                break;
            case R.id.blue:
                SelectColorButton(4);
                break;
            case R.id.verde:
                SelectColorButton(5);
                break;
            case R.id.giallo:
                SelectColorButton(6);
                break;
            case R.id.arancione:
                SelectColorButton(7);
                break;

            // IMMAGINI
            case R.id.arte:
                SelectImageButton(0);
                break;
            case R.id.atomo:
                SelectImageButton(1);
                break;
            case R.id.computer:
                SelectImageButton(2);
                break;
            case R.id.libro:
                SelectImageButton(3);
                break;
            case R.id.matematica:
                SelectImageButton(4);
                break;
            case R.id.musica:
                SelectImageButton(5);
                break;
            case R.id.palla:
                SelectImageButton(6);
                break;
            case R.id.storia:
                SelectImageButton(7);
                break;
        }
    }

    /**
     * Deseleziona (imposta la loro visibilità a 1f [massima]) tutte le imageButton dei colori.
     * Poi imposta la visibilita a 0.2f (per indicare il selezionamento da parte
     * dell'utente) di quella alla posizione passata come parametro.
     *
     * @param posizione posizione dell'elemento selezionato dall'utente
     */
    void SelectColorButton(int posizione) {
        for (ImageButton i : colorImageButton) {
            i.setAlpha(1f);
        }
        colorImageButton[posizione].setAlpha(Variables.SELECTED);
    }

    /**
     * Deseleziona (imposta la loro visibilità a 1f[massima]) tutte le imageButton
     * delle immagini. Poi imposta come premuta (imposta la visibilità a 0.2f)
     * di quella alla posizione passata come parametro
     *
     * @param posizione posizione dell'elemento selezionato dall'utente
     */
    void SelectImageButton(int posizione) {
        for (int i = 0; i < colorImageButton.length; i++) {
            imageImageButton[i].setAlpha(1f);
        }

        imageImageButton[posizione].setAlpha(Variables.SELECTED);
    }
}
