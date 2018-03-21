package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

/**
 * Activity para ver los detalles de los alimentos seleccinados a través del catálogo
 */
public class DetalleListaExternaActivity extends AppCompatActivity {
    private ArrayList<ComponenteListaCompra> componentes;//Para cargar los componentes seleccionados
    private ArrayList<ComponenteListaCompra> componentesAdapter;//Para cargar los componentes seleccionados en el adapter
    private CustomArrayAdapterNuevaLista adapter;//Adapter para la vista
    private int sort = 1;
    private Intent intent;//Para recoger los datos del activity que de origen
    private boolean editando = false;//Booleano para mostrar y ocultar los checkboxes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_lista_externa);
        //Cogemos la referencia a los floating action buttons
        com.getbase.floatingactionbutton.FloatingActionButton botonEditar = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.editar);
        //com.getbase.floatingactionbutton.FloatingActionButton botonAceptar = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.aceptar);
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
                if(adapter.getSize() > 0){
                    editando = !editando;
                    if(editando && adapter.getSize() > 0){
                        adapter.mostrarCheckboxes();
                    }else if (!editando  && adapter.getSize() > 0){
                        adapter.ocultarrCheckboxes();
                        adapter.confirmarCambios();
                    }
                }
            }
        });
    }

    /**
     * Método para cargar el adapter con el arraylist de los datos
     */
    private void cargarAdapter(){
        if(componentesAdapter != null){
            adapter = new CustomArrayAdapterNuevaLista(this, componentesAdapter, this);
        } else {
            adapter = new CustomArrayAdapterNuevaLista(this, new ArrayList<ComponenteListaCompra>(), this);
        }
        ListView listView = (ListView)findViewById(R.id.detalle_lista);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSortAll:
                sort *= -1;
                adapter.sortRecyclerView(sort);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Codificamos el envío de los datos al activity CompraExternaActivity
    @Override
    public void onBackPressed() {
        intent = new Intent();
        intent.putExtra("elementosLista", componentesAdapter);
        intent.putExtra("clasePadre", "Detalle");
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
