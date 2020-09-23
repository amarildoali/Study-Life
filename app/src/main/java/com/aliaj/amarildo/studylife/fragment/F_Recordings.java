package com.aliaj.amarildo.studylife.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.utility.Adapter;
import com.aliaj.amarildo.studylife.object.Record;
import com.aliaj.amarildo.studylife.utility.Variables;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Fragment delle registrazioni
 *
 * @see MediaRecorder Utilizzato per registrare audio e video. Il controllo della registrazione
 *          è basato su una semplice macchina di stato.
 * @see MediaPlayer La classe MediaPlayer può essere utilizzata per controllare la riproduzione
 *          di file audio e video e streams.
 * @see FloatingActionButton I FloatingActionButton vengono utilizzati per un tipo speciale di
 *          azione. Sono distinti da un'icona a cerchio che galleggia sopra l'UI e
 *          presenta comportamenti di movimento specifici relativi al morphing, al lancio e al
 *          punto di ancoraggio di trasferimento.
 */

public class F_Recordings extends Fragment implements AdapterView.OnItemClickListener{

    ArrayList<Record> arrayListRecord;    // arraylist registrazioni
    ListView listView;  // listview che conterrà le registazioni
    FloatingActionButton floatingActionButton; // floatingButton per registare
    Adapter adapter;  // adattatore per listview

    // Stati utilizzati per la gestione della registrazione
    final String IN_USE = "azione";
    final String IN_PAUSE = "pausa";
    String STATE = IN_PAUSE;

    String AudioSavePath = null;    // percorso su cui salvare le registrazioni
    MediaRecorder mediaRecorder;    // recorder utilizzato per la registrazione
    MediaPlayer mediaPlayer;    // player utilizzato per la riproduzione audio
    File arrayFiles[];  // array dei file presenti nella cartella specificata

    public ImageButton imageButton; // pulsante per azionare il player della registrazione
    int counter; // contatore utilizzato per dare il nome al file(ordine crescente)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recordings, container, false);
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


        listView = (ListView) view.findViewById(R.id.Fragment_Recordings_ListView);
        SetFloatingButton(view);
        arrayListRecord = new ArrayList<>();

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        counter = appSharedPrefs.getInt("contatore", 0);

        CheckFile();

        if(arrayListRecord.size() != 0) {
            adapter = new Adapter(getActivity(), arrayListRecord, 4);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        final Dialog d = new Dialog(getContext());
        final Record record = arrayListRecord.get(position);
        final File currentFile = arrayFiles[position];

        d.setTitle(record.getName());
        d.setContentView(R.layout.dialog_registrazioni);

        imageButton = (ImageButton) d.findViewById(R.id.imageButton_registrazioni);
        imageButton.setImageResource(R.mipmap.ic_play_circle_filled_white_48dp);

        Button rename = (Button) d.findViewById(R.id.dialog_registrazioni_rinomina);
        Button delete = (Button) d.findViewById(R.id.dialog_registrazioni_elimina);
        Button annul = (Button) d.findViewById(R.id.dialog_registrazioni_annulla);
        rename.setText("Rinomina");
        delete.setText("Elimina");
        annul.setText("Annulla");

        final ProgressBar progressBar = (ProgressBar) d.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(record.getDuration());



        String PATH_TO_FILE = Variables.PATH + File.separator + currentFile.getName();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(PATH_TO_FILE);
            mediaPlayer.setVolume(1.0f, 1.0f); // volume massimo
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPlayer.isPlaying()){
                    /* Stoppare e ricominciare */
                    imageButton.setImageResource(R.mipmap.ic_play_circle_filled_white_48dp);
                    progressBar.setProgress(0);
                    mediaPlayer.stop();
                    mediaPlayer.prepareAsync();
                } else {
                    mediaPlayer.start();


                    Thread t = new Thread() {
                        @Override
                        public void run() {

                            while (mediaPlayer.isPlaying())
                                progressBar.setProgress(mediaPlayer.getCurrentPosition());

                            getActivity().runOnUiThread(new Runnable() {
                                /* Per poter modificare l'immagine con questo thread, evitando cosi
                                 * il problema della richiesta della compatibilità con il thread che
                                  * ha creato la view*/
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                    imageButton.setImageResource(R.mipmap.ic_play_circle_filled_white_48dp);
                                }
                            });
                        }
                    };
                    imageButton.setImageResource(R.mipmap.ic_pause_circle_filled_white_48dp);
                    t.start();
                }
            }
        });

        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aprire un altro dialog per rinominare
                mediaPlayer.stop();
                Rename(currentFile);
                Refresh();
                d.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Elimino il file e chiamo la funzione refresh
                mediaPlayer.stop();
                AudioDelete(currentFile);
                Refresh();
                d.dismiss();
            }
        });

        annul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                d.dismiss();
            }
        });

        d.show();
    }

    /**
     * Inizializza il FloatingButton e imposta le cose da fare in base al click e allo stato
     */
    void SetFloatingButton(View view){

        final Date date = new Date();
        final Time now = new Time();
        now.setToNow();

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton_Registrazioni);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(STATE.equals(IN_PAUSE)){
                    floatingActionButton.setImageResource(R.mipmap.ic_pause_white_48dp);
                    STATE = IN_USE;

                    // Faccio partire la registrazione
                    AudioSavePath = Variables.PATH + "/" + counter;
                    counter++;

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putInt("contatore", counter);
                    editor.apply();

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException | IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getActivity(), "Registrazione Iniziata", Toast.LENGTH_LONG).show();
                } else {
                    floatingActionButton.setImageResource(R.mipmap.ic_keyboard_voice_white_48dp);
                    STATE = IN_PAUSE;

                    // Fermo e salvo la registrazione
                    mediaRecorder.stop();
                    Toast.makeText(getActivity(), "Registrazione Finita", Toast.LENGTH_LONG).show();
                    AudioCreator(date);
                }

            }
        });
    }

    /**
     * Controllare i file presenti nel PATH specificato e che gli inserisce nell'arraylist(ListView)
     */
    void CheckFile(){
        File directory = new File(Variables.PATH);
        arrayFiles = directory.listFiles();

        for (File aFile : arrayFiles) {
            MediaPlayer mp = new MediaPlayer();
            FileInputStream fs;
            try {
                fs = new FileInputStream(aFile);
                FileDescriptor fd;
                fd = fs.getFD();
                mp.setDataSource(fd);
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String name = aFile.getName();
            Date date = new Date(aFile.lastModified());
            int duration = mp.getDuration();
            Record record = new Record(name, date, duration);

            arrayListRecord.add(record);
        }
    }

    /**
     * Inizializza il MediaRecorder con i vari parametri di registrazione
     */
    public void MediaRecorderReady(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // formato 3GPP
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // Adaptive Multi-Rate(Narrowband/Banda Stretta)
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePath);
    }

    /**
     * Metodo chiamato una volta finito di registrare l'audio, che lo salva e inserisce
     * nell'arraylist
     * @param date data in cui è stato registrato
     */
    void AudioCreator(Date date){
        int duration = 0;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.getDuration();
        try {
            mediaPlayer.setDataSource(AudioSavePath);
            duration = mediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Record record = new Record(AudioSavePath, date, duration);

        arrayListRecord.add(record);

        Refresh();
    }

    /**
     * Creare un dialog che permette di modificare il nome attuale del file
     * @param file file a cui modificare il nome
     */
    void Rename(final File file){
        final Dialog d = new Dialog(getContext());
        d.setTitle(file.getName());
        d.setContentView(R.layout.dialog_rinomina_registrazioni);

        final EditText editText = (EditText) d.findViewById(R.id.editText_registrazione);
        Button annul = (Button) d.findViewById(R.id.button_modifica_nome_registrazione_annulla);
        Button rename = (Button) d.findViewById(R.id.button_modifica_nome_registrazione);
        annul.setText("Annulla");
        rename.setText("Rinomina");

        annul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.length() != 0){
                    // rinomino

                    String sourcePath = Variables.PATH + "/" + file.getName();
                    File source = new File(sourcePath);

                    String destinationPath = Variables.PATH + "/" + editText.getText();
                    File destination = new File(destinationPath);
                    try {
                        FileUtils.copyFile(source, destination);
                        Toast.makeText(getContext(), "Rinominato", Toast.LENGTH_SHORT).show();
                        source.delete();
                        Refresh();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    d.dismiss();
                }
            }
        });
        d.show();
    }

    /**
     * Elimina un file audio
     * @param file file da eliminare
     */
    void AudioDelete(File file){
        if(file.delete()){
            Toast.makeText(getContext(), file.getName() + " Eliminato", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Metodo che intercetta il fragment di id "registrazioni" lo chiude e lo riapre.
     * Metodo utilizzato per aggiornare la grafica della pagina
     */
    void Refresh(){
        Fragment frg = getFragmentManager().findFragmentByTag("registrazioni");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}