package net.ddns.smartfridge.smartfridgev2.vista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Programamos el botón que va a abrir toda la parte del Control de Alimentos
    public void abrirControlAlimentos(View v){
        Intent i = new Intent(this, ControlAlimentosActivity.class);
        startActivity(i);
    }

    //Programamos el botón que va a abrir toda la parte de Crear Lista de la Compra
    public void abrirCrearListaCompra(View v){
        Intent i = new Intent(this, CrearListaCompraActivity.class);
        startActivity(i);
    }

    //Programamos el botón que va a abrir toda la parte de Sugerir receta
    public void abrirSugerirReceta(View v){
        Intent i = new Intent(this, SugerirRecetaActivity.class);
        startActivity(i);
    }

    //Programamos el botón que va a abrir toda la parte de Planificar Menú
    public void abrirPlanificarMenu(View v){
        Intent i = new Intent(this, PlanificarMenuActivity.class);
        startActivity(i);
    }
}
