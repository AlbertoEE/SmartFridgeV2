package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.R;

/**
 * Activity para crear una nueva lista de la compra.
 */
public class CreadorListaCompraActivity extends AppCompatActivity {
    private Intent intent;//Lo vamos a usar para recoger el intent de otros activitys, así como para lanzar activitys nuevos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_lista_compra);
        intent = getIntent();
    }
}
