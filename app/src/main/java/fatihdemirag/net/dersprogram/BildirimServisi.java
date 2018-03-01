package fatihdemirag.net.dersprogram;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import fatihdemirag.net.dersprogram.DbHelpers.DbHelper;

/**
 * Created by fxd on 15.02.2018.
 */

public class BildirimServisi extends Service {
    public BildirimServisi() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    int sayac = 0;

    @Override
    public void onCreate() {
        Log.e("servis", "Servis Başladı");

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                DersKontrol();
                sayac++;

            }
        };
        timer.schedule(timerTask, 0, 1000);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("servis", "Servis Durdu");
        super.onDestroy();
    }

    void BildirimGonder(String dersUyari) {
        Intent i = new Intent(this, MainPage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uyariSesi = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Ders Takip")
                .setContentText(dersUyari)
                .setSmallIcon(R.drawable.homework)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(uyariSesi)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());

    }

    String bildirimZamani, simdikiSaat;
    int bildirimSuresi = -5;
    int saat, dakika;


    void DersKontrol() {
        Calendar calendar = Calendar.getInstance();

        simdikiSaat = calendar.getTime().getHours() + ":" + calendar.getTime().getMinutes();
        saat = Integer.parseInt(simdikiSaat.substring(0, simdikiSaat.indexOf(':')));
        dakika = Integer.parseInt(simdikiSaat.substring(simdikiSaat.indexOf(':') + 1, simdikiSaat.length()));
        bildirimZamani = saat + ":" + (dakika + bildirimSuresi);

        String ders = "";

        DbHelper dbHelper = new DbHelper(this);
        Cursor cursor = dbHelper.dersKontrol(bildirimZamani);

        Log.e("saat", saat + "");
        Log.e("dakika", dakika + "");
        Log.e("bildirimZamani", bildirimZamani + "");

        while (cursor.moveToNext()) {
            if (cursor.getCount() > 0) {

                if (!cursor.getString(1).equals("--Boş Ders--")) {
                    ders = cursor.getString(1);
                    BildirimGonder(ders + " dersi " + (bildirimSuresi * -1) + " dakika sonra başlayacak.");
                } else {
                    ders = (bildirimSuresi * -1) + " dakika sonra boş dersiniz var. " + new String(Character.toChars(0x1F60D));
                    BildirimGonder(ders);
                }
            }
        }
    }
}