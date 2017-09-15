package fatihdemirag.net.dersprogram;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class DersEkle extends Activity {

    DbHelper db;
    EditText dersAdi;
    TimePicker baslangicSaat,bitisSaat;
    Button dersEkle;
    TextView gun;
    private void TostMesaj(String mesaj)
    {
        Toast.makeText(getApplicationContext(), mesaj, Toast.LENGTH_SHORT).show();
    }

    private void KayitEkle(String dersAdi,String gun,String baslangicSaat,String bitisSaat)
    {
       try {

           if (db.insertData(dersAdi,gun,baslangicSaat,bitisSaat))
               TostMesaj("Ders Eklendi");
       }catch (SQLException s)
       {
           TostMesaj("Ders Eklenemedi");
           s.printStackTrace();
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_ekle);
        db=new DbHelper(this);
        dersAdi=(EditText)findViewById(R.id.dersAdi);
        baslangicSaat=(TimePicker)findViewById(R.id.baslangicSaat);
        bitisSaat=(TimePicker)findViewById(R.id.bitisSaat);
        dersEkle=(Button)findViewById(R.id.dersEkle);
        gun=(TextView)findViewById(R.id.gun);

        Bundle bundle=getIntent().getExtras();
        gun.setText(bundle.getString("Gün"));

        baslangicSaat.setIs24HourView(true);
        bitisSaat.setIs24HourView(true);
        baslangicSaat.clearFocus();
        bitisSaat.clearFocus();
        dersAdi.requestFocus();
        dersEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String baslangicH=String.valueOf(baslangicSaat.getCurrentHour());
                String baslangicM=String.valueOf(baslangicSaat.getCurrentMinute());
                if (baslangicH.length()==1)
                    baslangicH="0"+baslangicH;
                if(baslangicM.length()==1)
                    baslangicM="0"+baslangicM;
                String baslangic = baslangicH + ":" + baslangicM;

                String bitisH=String.valueOf(bitisSaat.getCurrentHour());
                String bitisM=String.valueOf(bitisSaat.getCurrentMinute());
                if (bitisH.length()==1)
                    bitisH="0"+bitisH;
                if(bitisM.length()==1)
                    bitisM="0"+bitisM;
                String bitis = bitisH + ":" + bitisM;



                if (Integer.parseInt(String.valueOf(baslangicSaat.getCurrentHour())) > Integer.parseInt(String.valueOf(bitisSaat.getCurrentHour())))
                    TostMesaj("Bitiş Saati Başlangıç Saatinden Önce Olamaz");
                else if (Integer.parseInt(String.valueOf(baslangicSaat.getCurrentHour())) == Integer.parseInt(String.valueOf(bitisSaat.getCurrentHour())) &&
                        Integer.parseInt(String.valueOf(baslangicSaat.getCurrentMinute())) == Integer.parseInt(String.valueOf(bitisSaat.getCurrentMinute())))
                    TostMesaj("Bitiş Saati Ve Başlangıç Saati Aynı Olamaz");
                else {
                    if (dersAdi.getText().length() == 0)
                        TostMesaj("Ders Adı Boş Geçilemez!");
                    else {
                        String dAd = Character.toUpperCase(dersAdi.getText().charAt(0)) + dersAdi.getText().toString().substring(1);
                        KayitEkle(dAd, gun.getText().toString(), baslangic, bitis);
                    }
                }
            }
        });
    }
}
