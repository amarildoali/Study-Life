package com.aliaj.amarildo.studylife.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.aliaj.amarildo.studylife.fragment.F_Subjects;
import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.Adapter;
import com.aliaj.amarildo.studylife.utility.DataManagment;
import com.aliaj.amarildo.studylife.object.Graphic;
import com.aliaj.amarildo.studylife.object.Subject;
import com.aliaj.amarildo.studylife.utility.Variables;
import com.aliaj.amarildo.studylife.object.Vote;
import com.db.chart.view.LineChartView;

import java.util.Date;

/**
 * Activity che mostra le statistiche(voti, grafico, media) di una determinata Materia
 */

public class SubjectInfo extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private int position;   // posizione della materia selezionata
    LineChartView lineChartView;    // grafico della media
    TextView average, target, toTake;   // view che stampano informazioni riguardanti l'andamento
    Subject subject;    // materia corrente

    ListView listView;  // utilizzata per "listare" i voti
    Adapter adapterVotes;   // adatattore necessario per la ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Acquisisco i dati passati dalla listview in base all'elemento selezionato
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            position = bundle.getInt("POSIZIONE");
        }

        subject = F_Subjects.arrayListSubjects.get(position);

        setTitle(subject.getName());

        SearchViewObjects();

        CheckNumberOfVotes();

        ListVotes();

        listView.setOnItemClickListener(this);
    }

    /**
     * Inizializza il contenuto del menu delle opzioni standard del Activity.
     * Le voci vanno posizionate in R.menu
     * @param menu Il menu delle opzioni in cui inserire i propri Item specificati in R.menu
     * @return true per visualizzare il menu, in caso contrario non verrà visualizzato
     *
     * @see MenuInflater
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_subject_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        switch (item.getItemId()) {
            case R.id.aggiungi_voto:
                InsertVote();
                return true;

            case R.id.imposta_obiettivo:
                SetTarget();
                return true;

            case R.id.elimina:
                Toast.makeText(this,
                        subject.getName() + " Eliminato",
                        Toast.LENGTH_LONG).show();

                F_Subjects.arrayListSubjects.remove(position);
                F_Subjects.subjectsAdapter.notifyDataSetChanged();

                DataManagment.Save(getBaseContext(), F_Subjects.arrayListSubjects, 1);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Utilizzato non appena l'utente modifica qualche parametro di una materia, per aggiornare le
     * statistiche della pagina
     */
    void Refresh(){
        subject.ComputeAverage();
        subject.ComputeToTake();
        subject.SortVotes();

        DataManagment.Save(getBaseContext(), F_Subjects.arrayListSubjects, 1);

        Intent intent = new Intent(SubjectInfo.this, SubjectInfo.class);
        intent.putExtra("POSIZIONE", position);
        finish();
        startActivity(intent);
    }

    /**
     * Metodo che cerca tutte le View nel layout di questa activity
     */
    void SearchViewObjects(){

        lineChartView = (LineChartView) findViewById(R.id.linechart);
        average = (TextView) findViewById(R.id.textView_voto_medio);
        target = (TextView) findViewById(R.id.testView_voto_obiettivo);
        toTake = (TextView) findViewById(R.id.textView_prossimo_voto);
    }

    /**
     * Metodo che verifica che la materia contenga voti, in caso negativo nasconde tutte
     * le View e aziona un Toast di segnalazione
     *
     * @see Toast Fornisce un semplice feedback in un piccolo popup.
     */
    void CheckNumberOfVotes(){

        if(subject.getVotes().size() != 0){
            SetGraphic();
            SetStatistics();

        } else {
            lineChartView.setVisibility(View.INVISIBLE);

            average.setVisibility(View.INVISIBLE);
            target.setVisibility(View.INVISIBLE);
            toTake.setVisibility(View.INVISIBLE);

            TextView view_media = (TextView) findViewById(R.id.textView0);
            TextView view_obiettivo = (TextView) findViewById(R.id.textView1);
            TextView view_devi = (TextView) findViewById(R.id.textView2);
            view_media.setVisibility(View.INVISIBLE);
            view_obiettivo.setVisibility(View.INVISIBLE);
            view_devi.setVisibility(View.INVISIBLE);

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_statistiche);
            linearLayout.setBackgroundColor(getResources().getColor(R.color.windowBackground));

            Toast.makeText(getBaseContext(), "Non ci sono voti", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Istanzia una classe di Grafico nel cui costruttore
     * vengono impostati tutti i parametri grafici, e che infine lo stampa
     */
    void SetGraphic(){
        Graphic grafico = new Graphic(getBaseContext(), subject, lineChartView);
        grafico.Show(grafico.animation, lineChartView);
    }

    /**
     * Metodo che riempe le textView(media, obiettivo, deviPrendere)
     * in base ai voti che l'utente inserisce per la data materia
     */
    void SetStatistics(){

        Float f;

        // Media
        f = subject.getAverage();
        average.setText(Float.toString(f));

        // Obiettivo
        f = subject.getTarget();
        if (f == 0){
            target.setText(f.toString());
        } else {
            target.setText(Float.toString(f));
        }

        // Devi prendere almeno
        f = subject.getToTake();
        if (f == 0){
            toTake.setText(f.toString());
        } else {
            toTake.setText(Float.toString(f));
        }
    }

    /**
     * Crea un Dialog che permette di scegliere il voto
     * da inserire tramite un NumberPicker
     *
     * @see Dialog é una piccola finestra che permette all'utente di prendere delle decisioni, o
     *          inserire informazioni.
     * @see NumberPicker View che permette la scelta di un numero
     */
    void InsertVote(){
        final Dialog d = new Dialog(SubjectInfo.this);
        d.setTitle("Seleziona Voto");
        d.setContentView(R.layout.dialog_seleziona_voto);

        Button select = (Button) d.findViewById(R.id.bottone_seleziona_voto);
        Button annul = (Button) d.findViewById(R.id.bottone_annulla_voto);
        select.setText("Scegli");
        annul.setText("Annulla");

        final NumberPicker numberPicker = (NumberPicker) d.findViewById(R.id.numberPicker1);
        final String valori[] = {
                "0", "0.25", "0.5", "0.75",
                "1", "1.25", "1.5", "1.75",
                "2", "2.25", "2.5", "2.75",
                "3", "3.25", "3.5", "3.75",
                "4", "4.25", "4.5", "4.75",
                "5", "5.25", "5.5", "5.75",
                "6", "6.25", "6.5", "6.75",
                "7", "7.25", "7.5", "7.75",
                "8", "8.25", "8.5", "8.75",
                "9", "9.25", "9.5", "9.75", "10"};
        numberPicker.setMaxValue(valori.length - 1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(valori);
        numberPicker.setWrapSelectorWheel(false);

        //evito che i valori possano essere modificati dall'utente
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = numberPicker.getValue();
                String string = valori[pos];
                float vote = Float.parseFloat(string);
                d.dismiss();

                // Chiamiamo la funzione per inserire la data
                InsertDate(vote);
            }
        });

        annul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    /**
     * Crea un nuovo Dialog inserendoci un DataPicker
     * in modo tale che l'utente possa selezionare la data del
     * voto precedentemente inserito e passato come parametro a
     * questo metodo
     *
     * @param vote voto scelto dall'utente
     * @see DatePicker View che permette di selezionare una data
     */
    void InsertDate(final float vote){
        final Dialog dialog = new Dialog(SubjectInfo.this);
        dialog.setTitle("Seleziona Data");
        dialog.setContentView(R.layout.dialog_seleziona_data);

        Button select = (Button) dialog.findViewById(R.id.bottone_seleziona_data);
        Button annul = (Button) dialog.findViewById(R.id.bottone_annulla_data);
        select.setText("Scegli");
        annul.setText("Annulla");

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                date.setYear(datePicker.getYear());
                date.setMonth(datePicker.getMonth());
                date.setDate(datePicker.getDayOfMonth());
                dialog.dismiss();

                //Chiamo il metodo per inserire il voto
                AddVote(vote, date);
            }
        });
        annul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Metodo utilizzato per inserire un nuovo voto nella materia aperta. Dopo averlo inserito
     * nell' ArrayList dei voti lo ordina in base alla data del voto(ordine crescente),
     * e successivamente lo salva. Solo alla fine stampa a video il risultato(positivo o negativo)
     * dell'implementazione del nuovo voto.
     *
     * @param vote voto selezionato dall'utente nel Dialog apposito
     * @param date data selezionata dall'utente nella Dialog apposita
     */
    void AddVote(float vote, Date date){
        if(subject.getVotes().size() < Variables.N_MAX_VOTES) {
            subject.getVotes().add(new Vote(vote, date));

            DataManagment.Save(getBaseContext(), F_Subjects.arrayListSubjects, 1);

            Toast.makeText(this, vote + " in " + subject.getName() + " inserito",
                    Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(this, "Non è permesso inserire più di 20 voti",
                    Toast.LENGTH_SHORT).show();

        Refresh();
    }

    /**
     * Metodo che crea un Dialog che permette all'utente
     * di scegliere il proprio obiettivo di voto finale.
     * Una volta inserito calcola la media, calcola anche
     * il prossimo voto da prendere per raggiungere tale
     * obiettivo, e infine salva tutto
     */
    void SetTarget(){

        final Dialog d = new Dialog(SubjectInfo.this);
        d.setTitle("Seleziona Voto");
        d.setContentView(R.layout.dialog_imposta_obiettivo);

        Button select = (Button) d.findViewById(R.id.bottone_seleziona_obiettivo);
        Button annul = (Button) d.findViewById(R.id.bottone_annulla_obiettivo);
        select.setText("Scegli");
        annul.setText("Annulla");

        final NumberPicker numberPicker = (NumberPicker) d.findViewById(R.id.numberPicker_obiettivo);
        final String valori[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        numberPicker.setMaxValue(valori.length - 1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(valori);
        numberPicker.setWrapSelectorWheel(false);

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pos = numberPicker.getValue();
                String in_stringa = valori[pos];
                float obiet = Float.parseFloat(in_stringa);
                d.dismiss();

                subject.setTarget(obiet);

                Refresh();
            }
        });

        annul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    /**
     * Metodo che riempe la listview con i voti della materia
     * selezionata tramite un adapter personalizzato
     */
    void ListVotes(){
        listView = (ListView) findViewById(R.id.lista_voti);

        adapterVotes = new Adapter(this, subject.getVotes(), 2);
        listView.setAdapter(adapterVotes);
    }

    /**
     * Gestisce il click sulla listView dei voti per eliminarli(interfaccia
     * AdapterView.OnItemClickListener).
     *
     * @param parent L'AdapterView in cui è avvenuto il click
     * @param view La view all'interno del AdapterView che è stato cliccato
     * @param position posizione dell'elemento selezionato
     * @param id L'id della riga dell'elemento che è stato cliccato.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        final Dialog dialog = new Dialog(SubjectInfo.this);
        dialog.setTitle(subject.getVotes().get(position).VoteToString() +
                " del " +
                subject.getVotes().get(position).getDate().getDate() +
                " " +
                Variables.MONTHS[subject.getVotes().get(position).getDate().getMonth()]);
        dialog.setContentView(R.layout.dialog_modifica_elimina_voto);

        Button delete = (Button) dialog.findViewById(R.id.button_elimina);
        Button annul = (Button) dialog.findViewById(R.id.button_modifica);

        delete.setText("Elimina");
        annul.setText("Annulla");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.getVotes().remove(position);

                Toast.makeText(getBaseContext(), "Eliminato", Toast.LENGTH_SHORT).show();

                dialog.dismiss();

                Refresh();
            }
        });

        annul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}