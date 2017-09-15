package fatihdemirag.net.dersprogram;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

public class DersNotuEkle extends Activity {

    Button resimEkle,ekleButton;
    Intent intent;
    int secilenResim=1;
    ImageView resim;
    Uri getResim;
    InputStream ınputStream;
    EditText konu,not;
    DbHelper dbHelper;
    String ders;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK && data!=null)
        {
                try {
                    getResim=data.getData();
                    ınputStream=getContentResolver().openInputStream(getResim);
                    Bitmap r= BitmapFactory.decodeStream(ınputStream);
                    Bitmap kucukResim=Bitmap.createScaledBitmap(r,800,600,true);
                    resim.setImageBitmap(kucukResim);
                    resim.setVisibility(View.VISIBLE);
                }catch (FileNotFoundException f)
                {
                    Toast.makeText(getApplicationContext(),"Resim Tanınamadı",Toast.LENGTH_SHORT).show();
                    f.printStackTrace();
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_notu_ekle);

        resim=(ImageView)findViewById(R.id.galeriResim);
        resimEkle=(Button)findViewById(R.id.resimEkle);
        resimEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,secilenResim);
            }
        });
        konu=(EditText)findViewById(R.id.konu);
        not=(EditText)findViewById(R.id.not);
        ekleButton=(Button)findViewById(R.id.ekleButton);
        dbHelper=new DbHelper(getApplicationContext());

        Bundle bundle=getIntent().getExtras();
        ders=bundle.getString("Ders");
        Toast.makeText(getApplicationContext(),ders,Toast.LENGTH_SHORT).show();

        ekleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap=((BitmapDrawable)resim.getDrawable()).getBitmap();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] i=stream.toByteArray();
                dbHelper=new DbHelper(getApplicationContext());
                try {
                    if (konu.getText().length()==0)
                        Toast.makeText(getApplicationContext(),"Lütfen Konu Başlığı Giriniz",Toast.LENGTH_SHORT).show();
                    else if(not.getText().length()==0)
                        Toast.makeText(getApplicationContext(),"Lütfen Not Giriniz",Toast.LENGTH_SHORT).show();
                    else
                    {
                        dbHelper.insertData2(konu.getText().toString(),ders,i,not.getText().toString());
                        Toast.makeText(getApplicationContext(),"Not Eklendi",Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Not Eklenemedi", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
