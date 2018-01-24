package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomPageAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.servicios.ComprobarCaducidadIntentService;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;

import java.util.ArrayList;
import java.util.List;

public class DetallesActivity extends FragmentActivity {
    private ViewPager viewPager;
    private ArrayList<Alimento> alimentos;
    private int posicion;
    private ArrayList<Bitmap> imagenes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_viewpager);
        Log.d("SWIPE", "onCreate: estoy aqui en el detalles activity" );
        viewPager = findViewById(R.id.viewpager);
        alimentos = (ArrayList<Alimento>) getIntent().getSerializableExtra("alimentosSinFoto");
        posicion = getIntent().getExtras().getInt("posicion");
        imagenes = MiNeveraActivity.getImagenDetalles();

        Log.d("SWIPE", "onCreate: primigenio2 " + alimentos.size());

        CustomPageAdapter pageAdapter = new CustomPageAdapter(getSupportFragmentManager(), alimentos, imagenes);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(posicion);
    }
}
