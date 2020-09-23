package com.aliaj.amarildo.studylife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aliaj.amarildo.studylife.fragment.F_Notes;
import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.DataManagment;
import com.aliaj.amarildo.studylife.object.Note;

/**
 * Activity che permette l'inserimento(o la modifica) di una nuova Nota
 */

public class NewNote extends AppCompatActivity implements View.OnClickListener {

    private int position;   // posizione della nota selezionata
    Note current;   // utilizzata per salvare la nota corrente in "locale"
    EditText title, body;  // Campi in cui inserire il titolo e il corpo della nota
    Button insert;  // Pulsante per completare l'inserimento
    boolean modify = false; // variabile utilizzata per capire se si sta modificando o creando da 0 una nota

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.NewNote_EditText_Titolo_Nota);
        body = (EditText) findViewById(R.id.NewNote_EditText_Corpo_Nota);
        insert = (Button) findViewById(R.id.NewNote_EditText_Button);

        // Acquisisco i dati passati dalla listview in base all'elemento selezionato
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            position = bundle.getInt("POSIZIONE_NOTA");
            current = F_Notes.arrayListNote.get(position);
            modify = true;
            insert.setText("Modifica");
            ShowValues(current);
        }

        insert.setOnClickListener(this);
    }

    /**
     * Questo metodo viene chiamato ogni qualvolta un oggetto
     * nel menù selezionato viene premuto(in questo caso la back key)
     * @param item oggett0 selezionato
     * @return Se ritorna vero l'evento su cui si è cliccato viene eseguito, in caso contrario si
     *          continua a verificare i successivi id.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NewNote.this.finish();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Dichiarata nell'interfaccia View.OnClickListener.
     * Chiamata quando una View viene cliccata.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.NewNote_EditText_Button:
                if(title.getText().length() > 0 && body.getText().length() > 0)
                    Insert();
                else
                    Toast.makeText(getBaseContext(), "Riempire i campi", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     *  Utilizatto nella parte di modifica della nota. Stampa la nota attuale, e la "prepara"
     *  per le eventuali modifiche
     */
    void ShowValues(Note note){
        title.setText(note.getTitle());
        body.setText(note.getBody());
    }

    /**
     * Inserisce(o elimina e poi inserisce (modifica)) una nota nell'arrayList delle note
     */
    void Insert(){
        if(modify){
            F_Notes.arrayListNote.remove(position);
        }
        Note note = new Note(title.getText().toString(), body.getText().toString());
        F_Notes.arrayListNote.add(note);
        DataManagment.Save(getBaseContext(), F_Notes.arrayListNote, 4);
        F_Notes.adapterNotes.notifyDataSetChanged();
        onBackPressed();
    }
}