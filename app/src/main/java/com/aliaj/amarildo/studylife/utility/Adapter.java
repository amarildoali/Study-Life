package com.aliaj.amarildo.studylife.utility;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.object.Note;
import com.aliaj.amarildo.studylife.object.Record;
import com.aliaj.amarildo.studylife.object.Subject;
import com.aliaj.amarildo.studylife.object.Test;
import com.aliaj.amarildo.studylife.object.Vote;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Adatattore per le listView
 *
 * @see LayoutInflater La classe LayoutInflater viene utilizzata per istanziare il file XML di
 *          layout nei relativi View object. In altre parole, prende come input un file XML e
 *          costruisce le View Object da quello.
 * @see BaseAdapter Classe di base per implementazione di un adattatore che può essere utilizzato
 *          sia in ListView (implementando l'interfaccia specializzata ListAdapter) e Spinner
 *          (implementando l'interfaccia SpinnerAdapter specializzata).
 */

public class Adapter extends BaseAdapter {

    private ArrayList<?> arrayList; // arrayList che riceve i dati
    private static LayoutInflater inflater = null;
    private int index;  // indice utilizzato per capire di quale listView si tratta
    /**
     * 1 = Subjects
     * 2 = Votes
     * 3 = Tests
     * 4 = Recordings
     * 5 = Notes
     * */

    // Costruttore
    public Adapter(Activity activity, ArrayList<?> list, int index) {
        this.arrayList = list;
        this.index = index;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * (Definita in BaseAdapter)Prende una View che visualizza i dati nella posizione specificata
     * nel set di dati. È possibile creare una vista manualmente o da un file di layout XML.
     *
     * @param position La posizione dell'elemento all'interno del set di dati dell'adattatore
     *                 dell'elemento di cui vogliamo la View.
     * @param convertView La vecchia View da riutilizzare, se possibile. Nota: è necessario
     *                    verificare che questa views sia not-null e di un tipo appropriato
     *                    prima di utilizzarlo.
     * @param parent Il parent che a cui questa view sarà eventualmente collegata
     * @return Una view corrispondente ai dati nella posizione specificata.
     */
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        View view = convertView;
        if(convertView == null)
            switch (index){
                case 1:
                    view = inflater.inflate(R.layout.item_list_subjects, null);
                    break;

                case 2:
                    view = inflater.inflate(R.layout.item_list_votes, null);
                    break;

                case 3:
                    view = inflater.inflate(R.layout.item_list_subjects, null);
                    break;

                case 4:
                    view = inflater.inflate(R.layout.item_list_recordings, null);
                    break;

                case 5:
                    view = inflater.inflate(R.layout.item_list_notes, null);
                    break;
            }


        switch (index){
            case 1:
                // Subjects

                Subject subject = (Subject) arrayList.get(position);

                TextView textView = (TextView) view.findViewById(R.id.textView);
                ImageView imageView = (ImageView) view.findViewById(R.id.immagine_listview);

                textView.setText(subject.getName());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.f);

                imageView.setImageResource(subject.getImage());
                imageView.setPadding(50,50,50,50);
                imageView.setBackgroundColor(view.getResources().getColor(subject.color));
                break;

            case 2:
                // Votes

                Vote vote = (Vote) arrayList.get(position);

                TextView voteTextView = (TextView) view.findViewById(R.id.item_list_voti_voto);
                TextView dateTextView = (TextView) view.findViewById(R.id.item_list_voti_data);
                voteTextView.setText(vote.VoteToString());

                String day = String.valueOf(vote.getDate().getDate());
                String month = Variables.MONTHS[vote.getDate().getMonth()];
                day = day + " " + month + " " +String.valueOf(vote.getDate().getYear());
                dateTextView.setText(day);

                if(vote.getVote() >= 6){
                    voteTextView.setTextColor(view.getResources().getColor(R.color.Verde700));
                } else {
                    voteTextView.setTextColor(view.getResources().getColor(R.color.Rosso700));
                }
                break;

            case 3:
                // Tests

                Test test = (Test) arrayList.get(position);

                TextView textView_test = (TextView) view.findViewById(R.id.textView);
                ImageView imageView_test = (ImageView) view.findViewById(R.id.immagine_listview);
                String day_test = String.valueOf(test.getDate().getDate());
                String month_test = Variables.MONTHS[test.getDate().getMonth()];
                day_test = day_test + " " + month_test + " " +String.valueOf(test.getDate().getYear());
                textView_test.setText(day_test);

                imageView_test.setImageResource(test.getSubject().getImage());
                imageView_test.setPadding(50, 50, 50, 50);
                imageView_test.setBackgroundColor(view.getResources().getColor(test.getSubject().color));
                break;

            case 4:
                // Recordings

                Record record = (Record) arrayList.get(position);

                ImageView imageView_record = (ImageView) view.findViewById(R.id.imageView_Foto_Registrazione);
                TextView name = (TextView) view.findViewById(R.id.textView_Nome_Registrazione);
                TextView time = (TextView) view.findViewById(R.id.textView_Data_Registrazione);
                TextView duration = (TextView) view.findViewById(R.id.textView_Durata_Registrazione);

                imageView_record.setImageResource(R.mipmap.ic_play_circle_filled_white_48dp);
                imageView_record.setColorFilter(R.color.colorAccent);
                name.setText("Name:\t\t" + record.getName());
                int year = record.getDate().getYear() + 1900;
                time.setText("Date:\t\t" + record.getDate().getDate()
                        + "/"
                        + record.getDate().getMonth()
                        + "/"
                        + year);
                int milliseconds = record.getDuration();
                String result = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(milliseconds),
                        TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                        TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
                );
                duration.setText(String.valueOf("Length:\t\t" + result));
                break;

            case 5:
                // Notes

                Note note = (Note) arrayList.get(position);

                TextView title = (TextView) view.findViewById(R.id.textView_titolo_notes);
                TextView body = (TextView) view.findViewById(R.id.textView_corpo_Notes);

                title.setText(note.getTitle());
                body.setText(note.getBody());
                break;
        }


        return view;
    }

    /**
     * Quanti elementi ci sono nel set dei dati rappresentati da questo Adapter.
     * @return numero di elementi
     */
    @Override
    public int getCount() {
        return arrayList.size();
    }

    /**
     * Prende i dati associati alla posizione specificata nel set di dati.
     * @param position Posizione da cui prendere i dati
     * @return I dati nella posizione specificata.
     */
    @Override
    public Object getItem(int position) {
        return position;
    }

    /**
     * Ottieni l'id della riga associata alla posizione specificata nell'elenco.
     * @param position posizione dell'elemento da prendere
     * @return id dell'elemento alla posizione specificata
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
}
