package com.aliaj.amarildo.studylife.fragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.aliaj.amarildo.studylife.notification.AlarmReceiver;
import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.Adapter;
import com.aliaj.amarildo.studylife.utility.DataManagment;
import com.aliaj.amarildo.studylife.object.Subject;
import com.aliaj.amarildo.studylife.object.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

/**
 * Fragment contente gli esami
 */

public class F_Tests extends Fragment {

    public static Calendar calendar;

    public static ArrayList<Test> arrayListTests; // arrayList degli esami
    Adapter adapterTests;   // adatattore per listView
    ListView listView;  // listView contenente le verifiche

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tests, container, false);
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
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.Fragment_Tests_ListView);
        arrayListTests = new ArrayList<>();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long id) {
                // APRIRE DOMANDA PER CHIEDERE SE LO SI VUOLE ELIMINARE

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                        R.style.Theme_AppCompat_Dialog_Alert);

                builder.setTitle("Vuoi rimuovere?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayListTests.remove(pos);
                        Toast.makeText(getActivity(), "Rimosso", Toast.LENGTH_SHORT);

                        DataManagment.Save(getContext(), arrayListTests, 3);
                        Refresh();
                    }
                });

                builder.setNegativeButton("No", null);
                builder.show();
                return true;
            }
        });

        if(DataManagment.Load(getContext(), 3)){
            CheckDateTests();
            adapterTests = new Adapter(getActivity(), arrayListTests, 3);
            listView.setAdapter(adapterTests);
        } else {
            arrayListTests = new ArrayList<>();
            adapterTests = new Adapter(getActivity(), arrayListTests, 3);
            listView.setAdapter(adapterTests);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tests, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.aggiungi_verifica:
                if(F_Subjects.arrayListSubjects.size() > 0)
                    SelectSubjectToInsert();
                else {
                    Toast toast = Toast.makeText(getActivity(), "Non ci sono materie",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Utilizzato non appena viene aperta questa activity che controlla che nell'arraylist
     * delle verifiche tutte le verifiche siano ancora valide(ovvero che non siano già
     * passate in termini di gioni), in tal caso le elimina dall'arraylist, e salva il tutto.
     */
    void CheckDateTests(){

        Calendar calender = Calendar.getInstance();
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int month = calender.get(Calendar.MONTH) + 1;
        int year = calender.get(Calendar.YEAR);

        for(Iterator<Test> iterator = arrayListTests.iterator(); iterator.hasNext(); ) {

            Test test = iterator.next();

            if (year > test.getDate().getYear()) {
                // SE L'ANNO ATTUALE é MAGGIORE DI QUELLO DELLA VERIFICA ELIMINARE
                iterator.remove();
                Toast.makeText(getActivity(),
                        "Rimossa verifica superata " + test.getSubject().getName(),
                        Toast.LENGTH_SHORT)
                        .show();

            } else if (year == test.getDate().getYear()) {
                // I DUE ANNI COMBACIANO

                if (month > (test.getDate().getMonth() + 1)) {
                    // IL MESE ATTUALE é MAGGIORE DI QUELLO DELLA VERIFICA ELIMINARE
                    iterator.remove();
                    Toast.makeText(getActivity(),
                            "Rimossa verifica superata " + test.getSubject().getName(),
                            Toast.LENGTH_SHORT)
                            .show();

                } else if (month == (test.getDate().getMonth() + 1)) {
                    // I DUE MESI COMBACIANO

                    if (day > test.getDate().getDate()) {
                        // IL GIORNO ATTUALE é MAGGIORE DI QUELLO DELLA VERIFICA ELIMINARE
                        iterator.remove();
                        Toast.makeText(getActivity(),
                                "Rimossa verifica superata " + test.getSubject().getName(),
                                Toast.LENGTH_SHORT)
                                .show();

                    } else if (day == test.getDate().getDate()) {
                        // GIORNO ESATTO

                        Toast toast = Toast.makeText(getActivity(),
                                "Oggi hai la verifica di " + test.getSubject().getName(),
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        }

        DataManagment.Save(getContext(), arrayListTests, 3);
    }

    /**
     * Apre un nuovo Dialog e permette all'utente di selezionare la materia per la quale
     * si vuole inserire una verifica, ovvero di aggiungerla nell'arraylist delle verifiche.
     */
    void SelectSubjectToInsert(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Seleziona Materia");
        dialog.setContentView(R.layout.dialog_lista_materie_orario);

        Button select = (Button) dialog.findViewById(R.id.bottone_seleziona_materia_ok);
        Button annul = (Button) dialog.findViewById(R.id.bottone_annulla_materia_ok);

        final NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.list_materie_da_aggiungere);
        String values[] = new String[F_Subjects.arrayListSubjects.size()];
        for (int i = 0; i < F_Subjects.arrayListSubjects.size(); i++) {
            values[i] = F_Subjects.arrayListSubjects.get(i).getName();
        }

        numberPicker.setMaxValue(values.length - 1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(values);
        numberPicker.setWrapSelectorWheel(false);

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = numberPicker.getValue();
                dialog.dismiss();
                SelectDayTest(F_Subjects.arrayListSubjects.get(pos));
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
     * Crea un nuovo Dialog inserendoci un DataPicker in modo tale che l'utente possa selezionare
     * la data del voto precedentemente inserito e passato come parametro a questo metodo.
     */
    void SelectDayTest(final Subject subject){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Seleziona Data Esame");
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

                //Chiedo all'utente se vuole un promemoria
                QuestionForWarning(subject, date);
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
     * Chiede all'utente se vuole che gli sia ricordato dell'esame con una notifica, tramite
     * un AlertDialog
     * @param subject materia di cui aggiungere una verifica
     * @param date data della verifica
     */
    void QuestionForWarning(final Subject subject, final Date date){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert);

        alertDialogBuilder
                .setTitle("Vuoi un promemoria?")
                .setMessage("Nota: scegliendo di si eliminerai un eventuale promemoria precedentemente programmato")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Faccio selezionare all'utente il giorno
                        SelectDayWarning(subject, date);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                        CompletionInsertionTest(subject, date, null, 0, 0);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Fa selezionare all'utente il giorno in cui preferisce essere avvertito
     * @param subject materia di cui aggiungere una verifica
     * @param date data della verifica
     */
    void SelectDayWarning(final Subject subject, final Date date){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Data promemoria");
        dialog.setContentView(R.layout.dialog_seleziona_data);

        Button select = (Button) dialog.findViewById(R.id.bottone_seleziona_data);
        Button annul = (Button) dialog.findViewById(R.id.bottone_annulla_data);
        select.setText("Scegli");
        annul.setText("Annulla");

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dateWarning = new Date();
                dateWarning.setYear(datePicker.getYear());
                dateWarning.setMonth(datePicker.getMonth());
                dateWarning.setDate(datePicker.getDayOfMonth());
                dialog.dismiss();

                //Chiedo all'utente l'orario dell'avvertimento
                SelectTimeWarning(subject, date, dateWarning);
            }
        });

        annul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CompletionInsertionTest(subject, date, null, 0, 0);
            }
        });
        dialog.show();
    }

    /**
     * Seleziona l'ora della notifica
     * @param subject materia di cui aggiungere una verifica
     * @param date giorno della verifica
     * @param dateWarning giorno in cui l'utente vuole farsi avvertire
     */
    void SelectTimeWarning(final Subject subject, final Date date, final Date dateWarning){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Orario promemoria");
        dialog.setContentView(R.layout.dialog_seleziona_ora_notifica);

        Button select = (Button) dialog.findViewById(R.id.bottone_seleziona_ora);
        Button annul = (Button) dialog.findViewById(R.id.bottone_annulla_ora);
        select.setText("Scegli");
        annul.setText("Annulla");

        final NumberPicker numberPickerHour = (NumberPicker) dialog.findViewById(R.id.numberPicker_ore);
        final NumberPicker numberPickerMinute = (NumberPicker) dialog.findViewById(R.id.numberPicker_minuti);

        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);

        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);

        numberPickerHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPickerMinute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = numberPickerHour.getValue();
                int minute = numberPickerMinute.getValue();

                dialog.dismiss();

                //Completare l'inserimento
                CompletionInsertionTest(subject, date, dateWarning, hour, minute);
            }
        });

        annul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CompletionInsertionTest(subject, date, null, 0, 0);
            }
        });
        dialog.show();
    }

    /**
     * Metodo che raccoglie in definitiva i dati da inserire nella nuova verifica(creata qua
     * dentro). Inoltre ordina le verifiche in base alla data(dalla più vicina alla più lontana).
     * @param subject materia della verifica
     * @param date giorno della verifica
     * @param dateWarning giorno della notifica
     * @param hour ora della notifica
     * @param minute minuto della notifica
     */
    void CompletionInsertionTest(Subject subject, Date date, Date dateWarning,
                                 int hour, int minute) {
        Test test = new Test(subject, date, dateWarning, hour, minute);
        arrayListTests.add(test);


        // Sort
        Collections.sort(arrayListTests, new Comparator<Test>() {
            @Override
            public int compare(Test t1, Test t2) {
                return t1.getDate().compareTo(t2.getDate());
            }
        });

        DataManagment.Save(getContext(), arrayListTests, 3);
        Refresh();

        if (test.getDateWarning() != null) {
            // Se l'utente ha voluto una notifica
            int year = test.getDateWarning().getYear();
            int month = test.getDateWarning().getMonth();
            int day = test.getDateWarning().getDate();
            SaveDateNotification(year, month, day, hour, minute);
        }
    }
    /**
     * Salva la data in cui l'utente vuole essere notificato
     * @param year anno della notifica
     * @param month mese della notifica
     * @param day giorno(del mese) della notifica
     * @param hour ora della notifica
     * @param minute minuto della notifica
     *
     * @see PendingIntent Un PendingIntent è un token che si assegna ad un'applicazione estera
     *          (ad esempio NotificationManager, AlarmManager, AppWidgetManager di Home Screen
     *          o altre applicazioni di terze parti) che consente all'applicazione estera di
     *          utilizzare le autorizzazioni della tua applicazione per eseguire un codice
     *          predefinito.
     */
    void SaveDateNotification(int year, int month, int day, int hour, int minute){

        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);    // 1-12 mesi
        calendar.set(Calendar.DAY_OF_MONTH, day);   // giorni variabili in base al mese
        calendar.set(Calendar.HOUR_OF_DAY, hour); // 0-23 ore
        calendar.set(Calendar.MINUTE, minute);  // 0-59 minuti
        calendar.set(Calendar.SECOND, 1);

        // Pianifico un allarme.
        // RTC_WAKEUP per azionare il display in caso di stand-by
        // INTERVAL_DAY intervallo in millisecondi tra ripetizioni successive dello stello allarme
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar
                .getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        DataManagment.SaveCalendar(getContext(), calendar);
    }

    /**
     * Metodo che intercetta il fragmento di id "verifiche" lo chiude e lo riapre. Metodo
     * utilizzato per aggiornare la grafica della pagina.
     */
    void Refresh(){
        Fragment frg = getFragmentManager().findFragmentByTag("verifiche");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}
