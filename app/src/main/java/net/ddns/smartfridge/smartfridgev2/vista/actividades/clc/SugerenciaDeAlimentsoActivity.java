package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomArrayAdapter;

import java.util.ArrayList;

public class SugerenciaDeAlimentsoActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> alimentosSugeridos;
    private ArrayList<String> nuevaLista;
    private CustomArrayAdapter customArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerencia_de_alimentso);

        alimentosSugeridos = new ArrayList<String>();
        alimentosSugeridos.add("Patata");
        alimentosSugeridos.add("Tomate");

        customArrayAdapter = new CustomArrayAdapter(this, alimentosSugeridos);
        nuevaLista = new ArrayList<String>();

        listView = findViewById(R.id.listView);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customArrayAdapter);

        //Hay que hacer lo de obtener la nueva lista
        //customArrayAdapter.getNuevaLista();
    }

}
