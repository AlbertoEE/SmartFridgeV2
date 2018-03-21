package net.ddns.smartfridge.smartfridgev2.vista.actividades.sr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.R;

/**
 * Activity para filtrar por recetas
 */
public class SugeridorRecetaModuloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerir_receta);
    }

    /**
     * Método para abrir el intent correspondiente en función de la selección del usuario
     *
     * @param v Vista sobre la que actúa el onClick
     */
    public void abrirSeleccionar(View v){
        //Abrirmos el intent
        Intent i = new Intent(this, FiltroRecetaActivity.class);
        startActivity(i);
    }
}
