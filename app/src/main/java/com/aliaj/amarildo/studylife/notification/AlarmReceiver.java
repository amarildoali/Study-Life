package com.aliaj.amarildo.studylife.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.aliaj.amarildo.studylife.R;
import com.aliaj.amarildo.studylife.activity.SplashScreen;

/**
 * Riceve un Intent e invia una notifica non appena si raggiunge la data decisa
 * dall'utente
 *
 * @see BroadcastReceiver Classe base per la ricezione e la gestione di Intent Broadcast inviati
 *          da sendBroadcast(Intent).
 *          È possibile registrare dinamicamente un'istanza di questa classe con
 *          Context.registerReceiver () o dichiarare staticamente un'implementazione con
 *          il tag <receiver> nel AndroidManifest.xml.
 * @see NotificationCompat Classe per l'accesso alle funzionalità di notifica.
 * @see NotificationCompat.Builder Builder class per gli oggetti NotificationCompat. Permette
 *          un più facile controllo su tutti i flag, e contribuisce a costruire i layout tipici
 *          di notifica.
 * @see NotificationManager Classe per notificare l'utente di un evento accaduto. Questo è come
 *          si dice all'utente che qualcosa è accaduto in background.
 */

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * Questo metodo è chiamato quando il BroadcastReceiver riceve un Intent Broadcast.
     * @param context contesto dove il chiamante sta eseguendo
     * @param intent l'Intent ricevuto
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent appIntent = new Intent(context, SplashScreen.class);
        PendingIntent p = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notif = new NotificationCompat.Builder(context);
        notif.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.book)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker("Si avvicina l'esame!")
                .setContentTitle("Study Life")
                .setContentText("Non dimenticarti dell'esame")
                .setContentIntent(p);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notif.build());
    }
}