package net.ddns.smartfridge.smartfridgev2.vista;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import net.ddns.smartfridge.smartfridgev2.R;

public class CaducidadAlimento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caducidad_alimento);
        //Cogemos el drawable de la carpeta de recursos
        Drawable elemento = getResources().getDrawable(R.drawable.uno);
        //Convertimos el drawable a bitmap
        Bitmap bitmap = ((BitmapDrawable) elemento).getBitmap();
        //Hacemos ese bitmap redondeado
        RoundedBitmapDrawable redondo = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        //Redondeamos los bordes
        redondo.setCornerRadius(bitmap.getHeight());
        //Cogemos la referencia al imageView
        ImageView iv1 = (ImageView)findViewById(R.id.ivCad1);
        iv1.setImageDrawable(redondo);
        ImageView iv2 = (ImageView)findViewById(R.id.ivCad2);
        iv2.setImageDrawable(redondo);
        ImageView iv3 = (ImageView)findViewById(R.id.ivCad3);
        iv3.setImageDrawable(redondo);
        ImageView iv4 = (ImageView)findViewById(R.id.ivCad4);
        iv4.setImageDrawable(redondo);
        ImageView iv5 = (ImageView)findViewById(R.id.ivCad5);
        iv5.setImageDrawable(redondo);
        ImageView iv6 = (ImageView)findViewById(R.id.ivCad6);
        iv6.setImageDrawable(redondo);
        ImageView iv7 = (ImageView)findViewById(R.id.ivCad7);
        iv7.setImageDrawable(redondo);
        ImageView iv8 = (ImageView)findViewById(R.id.ivCadMas);
        iv8.setImageDrawable(this.getResources().getDrawable(R.drawable.uno));
    }
}
