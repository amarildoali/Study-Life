package com.aliaj.amarildo.studylife.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.aliaj.amarildo.studylife.fragment.F_Notes;
import com.aliaj.amarildo.studylife.fragment.F_Schedule;
import com.aliaj.amarildo.studylife.fragment.F_Subjects;
import com.aliaj.amarildo.studylife.fragment.F_Tests;
import com.aliaj.amarildo.studylife.object.Note;
import com.aliaj.amarildo.studylife.object.Subject;
import com.aliaj.amarildo.studylife.object.Test;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * Salva e Carica i dati in base al tipo(luogo) della chiamata a funzione
 * @see SharedPreferences Interfaccia per l'accesso e la modifica dei dati restituiti da
 *          getSharedPreferences (String, int). Per ogni particolare serie di preferences,
 *          esiste un'unica istanza di questa classe che tutti i client condividono.
 *          Le modifiche alle freference devono passare attraverso un oggetto
 *          SharedPreferences.Editor per garantire che i valori delle preference rimangano in
 *          uno stato coerente quando sono impegnati per la memorizzazione.
 *          Gli oggetti restituiti dai vari metodi di ricezione devono essere trattati come
 *          immutabili dall'applicazione.
 * @see Gson Gson è una libreria Java che può essere utilizzata per convertire oggetti Java nella
 *          loro rappresentazione JSON. Può anche essere utilizzata per convertire una stringa JSON
 *          in un oggetto Java equivalente. Gson può lavorare con oggetti Java arbitrati inclusi
 *          oggetti preesistenti che non hanno codice sorgente.
 * @see Type è un interfaccia comune a tutti i tipi del linguaggio Java. Questi includono tipi
 *          grezzi, tipi parametrizzati, tipi di array, variabili di tipo e tipi primitivi.
 */

public class DataManagment extends Fragment{

    static SharedPreferences sharedPreferences; // utilizzato per salvare e caricare le stringhe
            // json
    static Type listType;   // utilizzato per salvare il tipo dell arrayList che si vuole salvare
    static Gson gson;   // utilizzato per convertire l'arraylist in una stringa Json
    static String json; // stringa che ospita il json da salvare

    static String jsonLun = null;
    static String jsonMar = null;
    static String jsonMer = null;
    static String jsonGio = null;
    static String jsonVen = null;
    static String jsonSab = null;

    /**
     * Metodo utilizzato per salvare tramite GSON, che permette di convertire
     * l'intero arraylist in una stringa JSON che poi verrà salvata
     * con SharedPreferences.
     *
     * @param context contesto chiamante dell'applicazione per salvare
     * @param index parametro per la gestione del tipo di salvataggio:
     *              1 = Subjects
     *              2 = Schedule
     *              3 = Tests
     *              4 = Notes
     */
    public static void Save(Context context, ArrayList<?> arrayList, int index){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        gson = new Gson();

        switch (index){
            case 1:
                // Materie
                listType = new TypeToken<ArrayList<Subject>>() {}.getType();
                break;
            case 2:
                // Orario
                listType = new TypeToken<ArrayList<Subject>>() {}.getType();
                break;

            case 3:
                // Verifiche
                listType = new TypeToken<ArrayList<Test>>() {}.getType();
                break;

            case 4:
                // Note
                listType = new TypeToken<ArrayList<Note>>() {}.getType();
                break;
        }

        if(index == 2){
            jsonLun = gson.toJson(F_Schedule.monList, listType);
            jsonMar = gson.toJson(F_Schedule.tueList, listType);
            jsonMer = gson.toJson(F_Schedule.wedList, listType);
            jsonGio = gson.toJson(F_Schedule.thuList, listType);
            jsonVen = gson.toJson(F_Schedule.friList, listType);
            jsonSab = gson.toJson(F_Schedule.satList, listType);
        } else
            json = gson.toJson(arrayList, listType);

        switch (index){
            case 1:
                editor.putString("SalvataggioMaterie", json);
                break;
            case 2:
                editor.putString("Lun", jsonLun);
                editor.putString("Mar", jsonMar);
                editor.putString("Mer", jsonMer);
                editor.putString("Gio", jsonGio);
                editor.putString("Ven", jsonVen);
                editor.putString("Sab", jsonSab);
                break;

            case 3:
                editor.putString("SalvataggioVerifiche", json);
                break;

            case 4:
                editor.putString("SalvataggioNote", json);
                break;
        }

        editor.apply();
    }

    /**
     * Metodo Utilizzato per caricare tutte le materie salvate precedentemente (se ce ne sono)
     * @param context Contesto da cui viene invocata questo metodo(OnCreate)
     * @return Ritorna true se è riuscito a trovare qualche materia salvata in precedenza
     */
    public static boolean Load(Context context, int index){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
        json = null;

        switch (index){
            case 1:
                json = sharedPreferences.getString("SalvataggioMaterie", "");
                listType = new TypeToken<ArrayList<Subject>>(){}.getType();
                F_Subjects.arrayListSubjects = gson.fromJson(json, listType);
                return F_Subjects.arrayListSubjects  != null;

            case 2:
                jsonLun = sharedPreferences.getString("Lun", "");
                jsonMar = sharedPreferences.getString("Mar", "");
                jsonMer = sharedPreferences.getString("Mer", "");
                jsonGio = sharedPreferences.getString("Gio", "");
                jsonVen = sharedPreferences.getString("Ven", "");
                jsonSab = sharedPreferences.getString("Sab", "");

                listType = new TypeToken<ArrayList<Subject>>(){}.getType();

                F_Schedule.monList = gson.fromJson(jsonLun, listType);
                F_Schedule.tueList = gson.fromJson(jsonMar, listType);
                F_Schedule.wedList = gson.fromJson(jsonMer, listType);
                F_Schedule.thuList = gson.fromJson(jsonGio, listType);
                F_Schedule.friList = gson.fromJson(jsonVen, listType);
                F_Schedule.satList = gson.fromJson(jsonSab, listType);

                return
                        F_Schedule.monList != null || F_Schedule.tueList != null ||
                        F_Schedule.wedList != null || F_Schedule.thuList != null ||
                        F_Schedule.friList != null || F_Schedule.satList != null;

            case 3:
                json = sharedPreferences.getString("SalvataggioVerifiche", "");
                listType = new TypeToken<ArrayList<Test>>(){}.getType();
                F_Tests.arrayListTests = gson.fromJson(json, listType);
                return F_Tests.arrayListTests != null;

            case 4:
                json = sharedPreferences.getString("SalvataggioNote", "");
                listType = new TypeToken<ArrayList<Note>>(){}.getType();
                F_Notes.arrayListNote = gson.fromJson(json, listType);
                return F_Notes.arrayListNote != null;
        }

        return true;
    }

    /**
     * Salva la data della notifica
     * @param context contesto in cui esegue l'applicazione
     * @param calendar data da salvare
     */
    public static void SaveCalendar(Context context, Calendar calendar){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        listType = new TypeToken<Calendar>() {}.getType();
        String json = gson.toJson(calendar, listType);
        editor.putString("Calendario", json);
        editor.apply();
    }

    /**
     * Carica la data della notifica da riprodurre. (chiamato dopo un riavvio del telefono)
     * @param context contesto in cui si esegue
     * @return data della notifica
     */
    public static Calendar LoadCalendar(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
        json = sharedPreferences.getString("Calendario", null);
        listType = new TypeToken<Calendar>(){}.getType();
        return gson.fromJson(json, listType);
    }
}
