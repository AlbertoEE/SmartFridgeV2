package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import net.ddns.smartfridge.smartfridgev2.R;

public class MostrarProductosListaActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_productos_lista);

        intent = getIntent();
    }

    private void cargarAdapter(){
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>();
    }
}
