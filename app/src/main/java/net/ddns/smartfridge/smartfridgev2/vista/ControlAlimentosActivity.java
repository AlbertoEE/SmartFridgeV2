package net.ddns.smartfridge.smartfridgev2.vista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.persistencia.MiNeveraDB;

public class ControlAlimentosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_alimentos);
        Intent intent= getIntent();
    }

    //Al pulsar el botón se abrirá el activity de identificar alimento para la lectura del código de barras
    public void abrirIdentificarAlimento(View v){
        Intent intent = new Intent (this, IdentificarAlimentoActivity.class);
        startActivity(intent);

    }
}
