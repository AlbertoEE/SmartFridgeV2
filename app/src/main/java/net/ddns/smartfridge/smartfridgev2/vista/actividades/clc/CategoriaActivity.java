package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.R;

public class CategoriaActivity extends AppCompatActivity {
    private Intent i;//Para pasar datos entre Activitys
    private String categoria;//Para hacer la búsqueda en la bbdd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        i = getIntent();
        categoria = i.getStringExtra("Categoria");
    }
    //Programamos el onclick de los botones
    public void agregar(View view){

    }
}
