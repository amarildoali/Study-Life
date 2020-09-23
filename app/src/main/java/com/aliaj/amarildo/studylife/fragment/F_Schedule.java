package com.aliaj.amarildo.studylife.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.DataManagment;
import com.aliaj.amarildo.studylife.object.Subject;

import java.util.ArrayList;

/**
 * Fragment con l'orario settimanale
 */

public class F_Schedule extends Fragment implements View.OnClickListener {

    // Arrays di bottoni per i giorni della settimana
    ImageButton Mon[];
    ImageButton Tue[];
    ImageButton Wed[];
    ImageButton Thu[];
    ImageButton Fri[];
    ImageButton Sat[];

    // ArrayList dei giorni della settimana
    public static ArrayList<Subject> monList;
    public static ArrayList<Subject> tueList;
    public static ArrayList<Subject> wedList;
    public static ArrayList<Subject> thuList;
    public static ArrayList<Subject> friList;
    public static ArrayList<Subject> satList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitializesButtons(view);
        InitializesArrayList();

        if (!DataManagment.Load(getContext(), 2)) {
            // Se il caricamento fallisce
            InitializesArrayList();
        }

        SetImages();
    }

    /**
     * Metodo che cerca gli Id delle ImageButton, imposta il background di tutte a 0 (trasparente)
     * e ne setta la cliccabilità
     */
    void InitializesButtons(View view) {

        Mon = new ImageButton[6];
        Tue = new ImageButton[6];
        Wed = new ImageButton[6];
        Thu = new ImageButton[6];
        Fri = new ImageButton[6];
        Sat = new ImageButton[6];

        Mon[0] = (ImageButton) view.findViewById(R.id.Lun8_9);
        Mon[1] = (ImageButton) view.findViewById(R.id.Lun9_10);
        Mon[2] = (ImageButton) view.findViewById(R.id.Lun10_11);
        Mon[3] = (ImageButton) view.findViewById(R.id.Lun11_12);
        Mon[4] = (ImageButton) view.findViewById(R.id.Lun12_13);
        Mon[5] = (ImageButton) view.findViewById(R.id.Lun13_14);

        Tue[0] = (ImageButton) view.findViewById(R.id.Mar8_9);
        Tue[1] = (ImageButton) view.findViewById(R.id.Mar9_10);
        Tue[2] = (ImageButton) view.findViewById(R.id.Mar10_11);
        Tue[3] = (ImageButton) view.findViewById(R.id.Mar11_12);
        Tue[4] = (ImageButton) view.findViewById(R.id.Mar12_13);
        Tue[5] = (ImageButton) view.findViewById(R.id.Mar13_14);

        Wed[0] = (ImageButton) view.findViewById(R.id.Mer8_9);
        Wed[1] = (ImageButton) view.findViewById(R.id.Mer9_10);
        Wed[2] = (ImageButton) view.findViewById(R.id.Mer10_11);
        Wed[3] = (ImageButton) view.findViewById(R.id.Mer11_12);
        Wed[4] = (ImageButton) view.findViewById(R.id.Mer12_13);
        Wed[5] = (ImageButton) view.findViewById(R.id.Mer13_14);

        Thu[0] = (ImageButton) view.findViewById(R.id.Gio8_9);
        Thu[1] = (ImageButton) view.findViewById(R.id.Gio9_10);
        Thu[2] = (ImageButton) view.findViewById(R.id.Gio10_11);
        Thu[3] = (ImageButton) view.findViewById(R.id.Gio11_12);
        Thu[4] = (ImageButton) view.findViewById(R.id.Gio12_13);
        Thu[5] = (ImageButton) view.findViewById(R.id.Gio13_14);

        Fri[0] = (ImageButton) view.findViewById(R.id.Ven8_9);
        Fri[1] = (ImageButton) view.findViewById(R.id.Ven9_10);
        Fri[2] = (ImageButton) view.findViewById(R.id.Ven10_11);
        Fri[3] = (ImageButton) view.findViewById(R.id.Ven11_12);
        Fri[4] = (ImageButton) view.findViewById(R.id.Ven12_13);
        Fri[5] = (ImageButton) view.findViewById(R.id.Ven13_14);

        Sat[0] = (ImageButton) view.findViewById(R.id.Sab8_9);
        Sat[1] = (ImageButton) view.findViewById(R.id.Sab9_10);
        Sat[2] = (ImageButton) view.findViewById(R.id.Sab10_11);
        Sat[3] = (ImageButton) view.findViewById(R.id.Sab11_12);
        Sat[4] = (ImageButton) view.findViewById(R.id.Sab12_13);
        Sat[5] = (ImageButton) view.findViewById(R.id.Sab13_14);


        for (int i = 0; i < 6; i++) {
            Mon[i].setBackgroundResource(0);
            Tue[i].setBackgroundResource(0);
            Wed[i].setBackgroundResource(0);
            Thu[i].setBackgroundResource(0);
            Fri[i].setBackgroundResource(0);
            Sat[i].setBackgroundResource(0);

            Mon[i].setPadding(25, 25, 25, 25);
            Tue[i].setPadding(25, 25, 25, 25);
            Wed[i].setPadding(25, 25, 25, 25);
            Thu[i].setPadding(25, 25, 25, 25);
            Fri[i].setPadding(25, 25, 25, 25);
            Sat[i].setPadding(25, 25, 25, 25);

            Mon[i].setOnClickListener(this);
            Tue[i].setOnClickListener(this);
            Wed[i].setOnClickListener(this);
            Thu[i].setOnClickListener(this);
            Fri[i].setOnClickListener(this);
            Sat[i].setOnClickListener(this);
        }

    }

    /**
     * Metodo che istanza gli arraylist per i giorni della settimana dove successivamente verranno
     * inserite le materie in base all'orario scelto dall'utente
     */
    void InitializesArrayList(){

        monList = new ArrayList<>();
        tueList = new ArrayList<>();
        wedList = new ArrayList<>();
        thuList = new ArrayList<>();
        friList = new ArrayList<>();
        satList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            monList.add(i, new Subject("Nullo", 0, 0));
            tueList.add(i, new Subject("Nullo", 0, 0));
            wedList.add(i, new Subject("Nullo", 0, 0));
            thuList.add(i, new Subject("Nullo", 0, 0));
            friList.add(i, new Subject("Nullo", 0, 0));
            satList.add(i, new Subject("Nullo", 0, 0));
        }
    }

    /**
     * Metodo che prima rende tutte le gli ImageButton con immagine trasparente(disegna un
     * quadrato vuoto) e successivamente in base al contenuto caricato e inserito nell'arraylist
     * dei giorni della settimana imposta le rispettive imamgini.
     */
    void SetImages(){

        for (int i = 0; i < 6; i++) {
            Mon[i].setImageResource(R.drawable.quadrato);
            Tue[i].setImageResource(R.drawable.quadrato);
            Wed[i].setImageResource(R.drawable.quadrato);
            Thu[i].setImageResource(R.drawable.quadrato);
            Fri[i].setImageResource(R.drawable.quadrato);
            Sat[i].setImageResource(R.drawable.quadrato);
        }


        for (int i = 0; i < 6; i++) {

            if (!monList.get(i).getName().equals("Nullo")) {
                Mon[i].setImageResource(monList.get(i).getImage());
                Mon[i].setBackgroundColor(getResources().getColor(monList.get(i).getColor()));
            }

            if (!tueList.get(i).getName().equals("Nullo")) {
                Tue[i].setImageResource(tueList.get(i).getImage());
                Tue[i].setBackgroundColor(getResources().getColor(tueList.get(i).getColor()));
            }

            if (!wedList.get(i).getName().equals("Nullo")) {
                Wed[i].setImageResource(wedList.get(i).getImage());
                Wed[i].setBackgroundColor(getResources().getColor(wedList.get(i).getColor()));
            }

            if (!thuList.get(i).getName().equals("Nullo")) {
                Thu[i].setImageResource(thuList.get(i).getImage());
                Thu[i].setBackgroundColor(getResources().getColor(thuList.get(i).getColor()));
            }

            if (!friList.get(i).getName().equals("Nullo")) {
                Fri[i].setImageResource(friList.get(i).getImage());
                Fri[i].setBackgroundColor(getResources().getColor(friList.get(i).getColor()));
            }

            if (!satList.get(i).getName().equals("Nullo")) {
                Sat[i].setImageResource(satList.get(i).getImage());
                Sat[i].setBackgroundColor(getResources().getColor(satList.get(i).getColor()));
            }

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.Lun8_9:
                ClickOnImageButton(monList, 0, 0);
                break;
            case R.id.Lun9_10:
                ClickOnImageButton(monList, 1, 0);
                break;
            case R.id.Lun10_11:
                ClickOnImageButton(monList, 2, 0);
                break;
            case R.id.Lun11_12:
                ClickOnImageButton(monList, 3, 0);
                break;
            case R.id.Lun12_13:
                ClickOnImageButton(monList, 4, 0);
                break;
            case R.id.Lun13_14:
                ClickOnImageButton(monList, 5, 0);
                break;


            case R.id.Mar8_9:
                ClickOnImageButton(tueList, 0, 1);
                break;
            case R.id.Mar9_10:
                ClickOnImageButton(tueList, 1, 1);
                break;
            case R.id.Mar10_11:
                ClickOnImageButton(tueList, 2, 1);
                break;
            case R.id.Mar11_12:
                ClickOnImageButton(tueList, 3, 1);
                break;
            case R.id.Mar12_13:
                ClickOnImageButton(tueList, 4, 1);
                break;
            case R.id.Mar13_14:
                ClickOnImageButton(tueList, 5, 1);
                break;


            case R.id.Mer8_9:
                ClickOnImageButton(wedList, 0, 2);
                break;
            case R.id.Mer9_10:
                ClickOnImageButton(wedList, 1, 2);
                break;
            case R.id.Mer10_11:
                ClickOnImageButton(wedList, 2, 2);
                break;
            case R.id.Mer11_12:
                ClickOnImageButton(wedList, 3, 2);
                break;
            case R.id.Mer12_13:
                ClickOnImageButton(wedList, 4, 2);
                break;
            case R.id.Mer13_14:
                ClickOnImageButton(wedList, 5, 2);
                break;


            case R.id.Gio8_9:
                ClickOnImageButton(thuList, 0, 3);
                break;
            case R.id.Gio9_10:
                ClickOnImageButton(thuList, 1, 3);
                break;
            case R.id.Gio10_11:
                ClickOnImageButton(thuList, 2, 3);
                break;
            case R.id.Gio11_12:
                ClickOnImageButton(thuList, 3, 3);
                break;
            case R.id.Gio12_13:
                ClickOnImageButton(thuList, 4, 3);
                break;
            case R.id.Gio13_14:
                ClickOnImageButton(thuList, 5, 3);
                break;


            case R.id.Ven8_9:
                ClickOnImageButton(friList, 0, 4);
                break;
            case R.id.Ven9_10:
                ClickOnImageButton(friList, 1, 4);
                break;
            case R.id.Ven10_11:
                ClickOnImageButton(friList, 2, 4);
                break;
            case R.id.Ven11_12:
                ClickOnImageButton(friList, 3, 4);
                break;
            case R.id.Ven12_13:
                ClickOnImageButton(friList, 4, 4);
                break;
            case R.id.Ven13_14:
                ClickOnImageButton(friList, 5, 4);
                break;


            case R.id.Sab8_9:
                ClickOnImageButton(satList, 0, 5);
                break;
            case R.id.Sab9_10:
                ClickOnImageButton(satList, 1, 5);
                break;
            case R.id.Sab10_11:
                ClickOnImageButton(satList, 2, 5);
                break;
            case R.id.Sab11_12:
                ClickOnImageButton(satList, 3, 5);
                break;
            case R.id.Sab12_13:
                ClickOnImageButton(satList, 4, 5);
                break;
            case R.id.Sab13_14:
                ClickOnImageButton(satList, 5, 5);
                break;
        }
    }

    /**
     * Metodo che gestisce che tipo di Dialog creare in base a che spazio
     * della matrice l'utente ha cliccato, e in base a quello gestisce l'inserimento
     * o l'eliminazione di una materia in un dato giorno della settimana e un dato
     * orario
     *
     * @param arrayList giorno della settimana scelto
     * @param position ora del giorno
     * @param gio variabile utilizzata per il titolo del Dialog
     */
    void ClickOnImageButton(final ArrayList<Subject> arrayList, final int position, int gio){

        String day = "";
        String hour = "";
        switch (position){
            case 0:
                hour = "8-9";
                break;
            case 1:
                hour = "9-10";
                break;
            case 2:
                hour = "10-11";
                break;
            case 3:
                hour = "11-12";
                break;
            case 4:
                hour = "12-13";
                break;
            case 5:
                hour = "13-14";
                break;
        }

        switch (gio){
            case 0:
                day = "Lun";
                break;
            case 1:
                day = "Mar";
                break;
            case 2:
                day = "Mer";
                break;
            case 3:
                day = "Gio";
                break;
            case 4:
                day = "Ven";
                break;
            case 5:
                day = "Sab";
                break;
        }

        // Creo l'AlterDialog per domandare ad utente cosa fare //
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert);

        alertDialogBuilder.setTitle(day + " " + hour);

        if(arrayList.get(position).getName().equals("Nullo")){
            // Significa che lo spazio è vuoto

            alertDialogBuilder
                    .setMessage("Vuoi aggiungere una materia per questa ora?")
                    .setCancelable(false)
                    .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(F_Subjects.arrayListSubjects.size() == 0){
                                Toast.makeText(getActivity(), "Non ci sono materie", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                SelectSubjectToInsert(arrayList, position);
                            }
                        }
                    })
                    .setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

        } else {
            // Significa che lo spazio non è vuoto

            alertDialogBuilder
                    .setMessage("Vuoi rimuovere questa materia per questa ora?")
                    .setCancelable(false)
                    .setPositiveButton("Elimina",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            arrayList.get(position).setName("Nullo");
                            arrayList.get(position).setColor(0);
                            arrayList.get(position).setImage(0);
                            DataManagment.Save(getContext(), arrayList, 2);
                            Refresh();
                        }
                    })
                    .setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

        }

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    /**
     * Metodo che apre un nuovo Dialog e permette all'utente di selezionare
     * la materia da aggiungere in quel determinato orario, ovvero di aggiungerla
     * nell'arraylist del giorno scelto all' orario scelto
     *
     * @param arrayList arraylist del giorno della settimana
     * @param posizine posizione in base all'orario nell'arraylist
     */
    void SelectSubjectToInsert(final ArrayList<Subject> arrayList, final int posizine){

        final Dialog dialog = new Dialog(getContext());
        dialog.setTitle("Seleziona Materia");
        dialog.setContentView(R.layout.dialog_lista_materie_orario);

        Button select = (Button) dialog.findViewById(R.id.bottone_seleziona_materia_ok);
        Button annul = (Button) dialog.findViewById(R.id.bottone_annulla_materia_ok);

        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.list_materie_da_aggiungere);
        String valori[] = new String[F_Subjects.arrayListSubjects.size()];
        for (int i = 0; i < F_Subjects.arrayListSubjects.size(); i++) {
            valori[i] = F_Subjects.arrayListSubjects.get(i).getName();
        }

        numberPicker.setMaxValue(valori.length - 1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(valori);
        numberPicker.setWrapSelectorWheel(false);

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = numberPicker.getValue();
                arrayList.get(posizine).setName(F_Subjects.arrayListSubjects.get(pos).getName());
                arrayList.get(posizine).setColor(F_Subjects.arrayListSubjects.get(pos).getColor());
                arrayList.get(posizine).setImage(F_Subjects.arrayListSubjects.get(pos).getImage());

                DataManagment.Save(getContext(), arrayList, 2);
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

    /**
     * Metodo che intercetta il fragmento di id "orario" lo chiude e lo riapre. Metodo utilizzato
     * per aggiornare la grafica della pagina
     */
    void Refresh(){

        Fragment frg = getFragmentManager().findFragmentByTag("orario");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}