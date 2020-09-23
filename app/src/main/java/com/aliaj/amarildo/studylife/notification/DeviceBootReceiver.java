package com.aliaj.amarildo.studylife.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aliaj.amarildo.studylife.utility.DataManagment;

import java.util.Calendar;

/**
 * Metodo che gestisce il caso di spegnimento del dispositivo e permette di "recuperare"
 * la notifica programmata dall'utente
 */

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Quando il dispositivo ha completato il boot recupero la data in cui notificare
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = DataManagment.LoadCalendar(context);
            // se non ci sono salvataggi non inviare notifica(anche perchè fallirebbe)
            if (calendar == null) return;

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
