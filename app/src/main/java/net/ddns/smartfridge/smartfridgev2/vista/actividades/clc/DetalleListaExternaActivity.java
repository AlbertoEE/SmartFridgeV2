package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomArrayAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomArrayAdapterNuevaLista;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Fecha;

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
        //Cogemos la referencia a los floating action buttons
        com.getbase.floatingactionbutton.FloatingActionButton botonEditar = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.editar);
        com.getbase.floatingactionbutton.FloatingActionButton botonAceptar = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.aceptar);
        componentesAdapter = new ArrayList<ComponenteListaCompra>();
        componentes = (ArrayList<ComponenteListaCompra>) getIntent().getSerializableExtra("componentes");
        for(int i=0; i<componentes.size();i++) {
            Log.d("componente", "nombre3: " + componentes.get(i).getNombreElemento());
            componentesAdapter.add(componentes.get(i));
            Log.d("componente", "total: " + componentesAdapter.get(i).getNombreElemento());
        }
        cargarAdapter();
        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(adapter.getSize() > 0){
                    editando = !editando;
                    if(editando && adapter.getSize() > 0){
                        adapter.mostrarCheckboxes();
                    }else if (!editando  && adapter.getSize() > 0){
                        adapter.ocultarrCheckboxes();
                        adapter.confirmarCambios();
                    }
                }*/
            }
        });
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.putExtra("elementosLista", componentesAdapter);
                intent.putExtra("clasePadre", "Detalle");
                setResult(RESULT_OK, intent);
                Toast.makeText(DetalleListaExternaActivity.this, "Guardando selección...", Toast.LENGTH_SHORT).show();
                Log.d("vuelta", "volvemos al activity que le llamó");
                finish();
            }
        });
    }
    //Método para cargar el adapter con el arraylist de los datos
    private void cargarAdapter(){
        if(componentesAdapter != null){
            adapter = new CustomArrayAdapterNuevaLista(this, componentesAdapter, this);
        } else {
            adapter = new CustomArrayAdapterNuevaLista(this, new ArrayList<ComponenteListaCompra>(), this);
        }
        ListView listView = (ListView)findViewById(R.id.detalle_lista);
        listView.setAdapter(adapter);
    }
}
