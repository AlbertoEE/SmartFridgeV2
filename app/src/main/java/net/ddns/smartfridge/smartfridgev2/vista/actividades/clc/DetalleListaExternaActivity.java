package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomArrayAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomArrayAdapterNuevaLista;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;

import java.util.ArrayList;

public class DetalleListaExternaActivity extends AppCompatActivity {
    private ArrayList<ComponenteListaCompra> componentes;//Para cargar los componentes seleccionados
    private ArrayList<ComponenteListaCompra> componentesAdapter;//Para cargar los componentes seleccionados en el adapter
    private CustomArrayAdapterNuevaLista adapter;;//Adapter para la vista
    private Intent intent;//Para recoger los datos del activity que de origen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_lista_externa);
        componentesAdapter = new ArrayList<ComponenteListaCompra>();
        componentes = (ArrayList<ComponenteListaCompra>) getIntent().getSerializableExtra("componentes");
        for(int i=0; i<componentes.size();i++) {
            Log.d("componente", "nombre3: " + componentes.get(i).getNombreElemento());
            componentesAdapter.add(componentes.get(i));
            Log.d("componente", "total: " + componentesAdapter.get(i).getNombreElemento());
        }
        cargarAdapter();
    }

    private void cargarAdapter(){
        if(componentesAdapter != null){
            adapter = new CustomArrayAdapterNuevaLista(this, componentesAdapter, this);
        } else {
            adapter = new CustomArrayAdapterNuevaLista(this, new ArrayList<ComponenteListaCompra>(), this);
        }
        //Log.d("elputo", "cargarAdapter: " + crearArray());
        Log.d("elputo", "cargarAdapter: " + adapter);
        ListView listView = (ListView)findViewById(R.id.detalle_lista);
        Log.d("elputo", "cargarAdapter: " + listView);
        listView.setAdapter(adapter);
    }
}
