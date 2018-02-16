package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;

import java.util.ArrayList;

public class DetalleListaExternaActivity extends AppCompatActivity {
    private ArrayList<ComponenteListaCompra> componentes;//Para cargar los componentes seleccionados
    private ArrayList<ComponenteListaCompra> componentesAdapter;//Para cargar los componentes seleccionados en el adapter
    //private ComponenteListaCompra c;//Para crear los componentes y pasarlos de un adapter a otro
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
    }
}
