package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomArrayAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.persistencia.GestorSharedP;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.DialogActivity;

import java.util.ArrayList;

public class SugerenciaDeAlimentoActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<ComponenteListaCompra> alimentosSugeridos;
    private ArrayList<String> nuevaLista;
    private CustomArrayAdapter customArrayAdapter;
    private GestorSharedP gsp;//Instacia para gestionar el Shared Preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugerencia_de_alimento);
        gsp = new GestorSharedP();
        //Cogemos la referecencia al fab
        FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        //alimentosSugeridos = getIntent().getStringArrayListExtra("AlimentosSugeridos");
        alimentosSugeridos = (ArrayList<ComponenteListaCompra>)getIntent().getSerializableExtra("AlimentosSugeridos");
        //customArrayAdapter = new CustomArrayAdapter(this, alimentosSugeridos);
        nuevaLista = new ArrayList<String>();

        listView = findViewById(R.id.listView);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customArrayAdapter);

        //Hay que hacer lo de obtener la nueva lista
        //customArrayAdapter.getNuevaLista();

        //Añadimos el onclick al fab
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Borramos el sp
                gsp.borrarSP();
                //Añadimos los elementos a la lista de la compra.
            }
        });
    }




}
