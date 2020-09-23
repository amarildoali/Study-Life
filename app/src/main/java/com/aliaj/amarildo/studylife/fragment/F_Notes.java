package com.aliaj.amarildo.studylife.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aliaj.amarildo.studylife.activity.NewNote;
import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.Adapter;
import com.aliaj.amarildo.studylife.utility.DataManagment;
import com.aliaj.amarildo.studylife.object.Note;

import java.util.ArrayList;

/**
 * Fragment dove vengono mostrate le note
 *
 * @see AlertDialog Una sottoclasse di Dialog che può visualizzare uno, due o tre pulsanti.
 *          Se si desidera visualizzare solo una stringa in questa finestra di dialogo, utilizzare
 *          il metodo setMessage().
 */

public class F_Notes extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static ListView listView;    // conviene l'elenco delle note
    public static ArrayList<Note> arrayListNote;    // arrayList delle note
    public static Adapter adapterNotes; // adatattore per la listView

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * Chiamato per avere il Fragment istantaneo della propria view dell'interfaccia utente.
     * Ciò è facoltativo e i Fragment non grafici possono restituire null (che è l'implementazione
     * predefinita). Questo sarà chiamato tra onCreate(Bundle) e onActivityCreated(Bundle).
     * Se restituisci una View da qui, ti verrà successivamente chiamata inDestroyView() quando
     * la View viene rilasciata.
     * @param inflater L'oggetto LayoutInflater che può essere utilizzato per "gonfiare" qualsiasi
     *          views nel Fragment.
     * @param container se non-null, questo è il padre a cui il Fragment UI deve essere arraccato.
     * @param savedInstanceState se non-null, questo fragment viene ricostruito da uno stato
     *          precedentemente salvato come qui indicato.
     * @return La View per l'UI del fragment, oppure null.
     *
     * @see ViewGroup è una vista speciale che può contenere altre viste (chiamati children).
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    /**
     * Chiamato quando un Fragment viene collegato al suo context. OnCreate(Bundle) verrà chiamato
     * dopo questo metodo.
     * @param context Contesto a cui essere attaccato
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    /**
     * Chiamato quando il Fragment non è più collegato alla sua Activity. Questo viene chiamato
     * dopo onDestroy(), eccetto nei casi in cui l'istanza di Fragment viene mantenuta attraverso
     * la ri-creazione dell'Activity (vedi setRetainInstance (boolean)), in questo caso viene
     * chiamata dopo onStop().
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_notes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aggiungi_nota:
                startActivity(new Intent(getActivity(), NewNote.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Ciò è utile se si sa che un Fragment è stato inserito nella gerarchia della View in modo
     * che l'utente non possa vederlo, quindi non dovrebbero essere visualizzate anche
     * le voci di menu che ha.
     * @param visible Il valore predefinito è true, il che significa che il menu del Fragment
     *          verrà mostrato come al solito. Se è falso, l'utente non vedrà il menu.
     */
    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible) {
            Refresh();
        }
        super.setMenuVisibility(visible);
    }

    /**
     * Chiamato immediatamente dopo che onCreateView(LayoutInflater, ViewGroup, Bundle) è ritornato,
     * ma prima che sia stato ripristinato uno stato salvato nella View. In questo modo le
     * sottoclassi hanno la possibilità di inizializzarsi una volta che sanno che la loro
     * gerarchia di visione è stata creata.
     * @param view TLa View ritornata da onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState Se non-null, questo Fragment viene ricostruito da uno stato
     *          precedentemente salvato come qui indicato.
     */
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.Fragment_Notes_ListView);
        arrayListNote = new ArrayList<>();

        if(DataManagment.Load(getContext(), 4)) {
            adapterNotes = new Adapter(getActivity(), arrayListNote, 5);
            listView.setAdapter(adapterNotes);
        } else {
            arrayListNote = new ArrayList<>();
            adapterNotes = new Adapter(getActivity(), arrayListNote, 5);
            listView.setAdapter(adapterNotes);
        }

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    /**
     * Metodo di richiamata da invocare quando è stato fatto clic su un elemento in questa
     * AdapterView.
     * @param parent La AdapterView dove è avvenuto il click
     * @param view La View all'interno del AdapterView che è stato cliccato
     * @param position posizione dell'elemento cliccato
     * @param id id dell'elemento cliccato
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), NewNote.class);
        intent.putExtra("POSIZIONE_NOTA", position);
        startActivity(intent);
    }

    /**
     * Metodo di richiamata da invocare quando è tenuto premuto su un elemento in questa
     * AdapterView.
     * @param parent La AdapterView dove ciò è avvenuto
     * @param view La View all'interno del AdapterView
     * @param position posizione dell'elemento tenuto premuto
     * @param id id dell'elemento tenuto premuto
     * @return true se il lungo click è stato "consumato", altrimenti false
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert);

        alertDialogBuilder.setTitle(arrayListNote.get(position).getTitle());

        alertDialogBuilder
                .setMessage("Vuoi eliminare questa nota?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Toast.makeText(getContext(), "Eliminato " + arrayListNote.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                        arrayListNote.remove(position);
                        DataManagment.Save(getContext(), arrayListNote, 4);
                        Refresh();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return true;
    }

    /**
     * Metodo che intercetta il fragmento di id "note" lo chiude e lo riapre. Metodo utilizzato
     * per aggiornare la grafica della pagina.
     */
    public void Refresh(){
        Fragment frg = getFragmentManager().findFragmentByTag("note");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}

