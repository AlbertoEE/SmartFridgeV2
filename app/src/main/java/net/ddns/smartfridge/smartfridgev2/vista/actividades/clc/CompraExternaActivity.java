//Activity para mostrar las opciones de alimentos de la bbdd externa
package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapterRevistaMain;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;

import java.util.ArrayList;

public class CompraExternaActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ANTERIOR = 5465;//Código de respuesta para el activity que lo llamó
    private static final int REQUEST_CODE_SIGUIENTE = 468;//Código de respuesta para el activity que al que va a llamar, el detalle de cada elemento
    private static final int REQUEST_CODE_CARRO = 834;//Código de respuesta para el activity que al que va a llamar, el carro de la compra
    private static final String[] categorias = {"Verdura", "Carne", "Fruta", "Pescado", "Bebida", "Embutido", "Frutos secos", "Desayuno"};//Array con los nombres
    //de las categorías de alimentos para coger el seleccionado por el usuario
    private String seleccion;//Seleccion hecha por el usuario
    private Intent intent;//Para pasar información entre activitys
    private ArrayList<ComponenteListaCompra> alimentosExternos;//Para almacenar el resultado de la seleccion del usuario y pasárselo al intent que nos llamó
    private ArrayList<ComponenteListaCompra> alimentosExternosTotales;//Para almacenar todos los alimentos y enviarlos a la lista de la compra
    private CustomRecyclerViewAdapterRevistaMain adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_externa);
        alimentosExternos = new ArrayList<ComponenteListaCompra>();
        alimentosExternosTotales = new ArrayList<ComponenteListaCompra>();
        adapter = new CustomRecyclerViewAdapterRevistaMain(this, this);
        recyclerView = findViewById(R.id.rvCompraExterna);
        layoutManager = new GridLayoutManager(this, 2);
       recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Programamos el método que va a haber en cada botón. Programamos el de las verduras
    public void abrirCategoria(int categoria){
        seleccion = categorias[categoria];
        intent = new Intent(this, CategoriaActivity.class);
        //Le asignamos la categoría
        intent.putExtra("Categoria", seleccion);
        startActivityForResult(intent, REQUEST_CODE_SIGUIENTE);
    }

    //Programamos el onActivityResult para recoger el arraylist con los datos que ha seleccionado el usuario
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("componente", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        //Comrpobamos el intent que viene de vuelta
        if (requestCode == REQUEST_CODE_SIGUIENTE) {
            Log.d("componente", "onActivityResult dentro del if");
            // Vemos que el resultado esté correcto
            if (resultCode == RESULT_OK) {
                //Recogemos los datos del intent y se los asignamos al ArrayList
                alimentosExternos = (ArrayList<ComponenteListaCompra>) data.getSerializableExtra("result");
                Log.d("componente", "tamaño alimentosExternos: " + alimentosExternos.size());
                for(int i=0; i<alimentosExternos.size(); i++){
                    //alimentosExternosTotales.add(alimentosExternos.get(i));
                    comprobarRepetidos(alimentosExternosTotales, alimentosExternos.get(i));
                }
            /*    intent = getIntent();
                intent.putExtra("Categorias", alimentosExternosTotales);
                //Devolvemos el ArrayList con el request_code del intent
                setResult(REQUEST_CODE_ANTERIOR, intent);*/
            }
        } else if(requestCode == REQUEST_CODE_CARRO){/*
            // Vemos que el resultado esté correcto
            if (resultCode == RESULT_OK) {
                //Recogemos los datos del intent y se los asignamos al ArrayList
                alimentosExternos = (ArrayList<ComponenteListaCompra>) data.getSerializableExtra("result");
                for(int i=0; i<alimentosExternos.size(); i++){
                    alimentosExternosTotales.add(alimentosExternos.get(i));
                }
                intent = getIntent();
                intent.putExtra("Categorias", alimentosExternosTotales);
                //Devolvemos el ArrayList con el request_code del intent
                setResult(REQUEST_CODE_ANTERIOR, intent);
            }*/
        } else {
            Log.d("componente", "onActivityResult else");
        }
    }

    /**
     * @see 'https://developer.android.com/reference/android/app/Activity.html?hl=es-419#onCreateOptionsMenu(android.view.Menu)'
     */
    //Para crear el ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_categoria_shopping_carg, menu);
        return true;
    }

    /**
     * @see 'https://developer.android.com/reference/android/app/Activity.html?hl=es-419#onOptionsItemSelected(android.view.MenuItem)'
     */
    //Programamos el botón del ActionBAr
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Programamos el botón de compartir
            case R.id.shoppingCart:
                //Cuando pulsemos en el carro, se nos mostrará la lista de los alimentos seleccionados para añadir añadir a la lista de la compra
                intent = new Intent(this, DetalleListaExternaActivity.class);
                intent.putExtra("componentes", alimentosExternosTotales);
                Log.d("componente", "tamaño alimentosExternosTotales: " + alimentosExternosTotales.size());
                for(int i=0; i<alimentosExternosTotales.size();i++) {
                    Log.d("componente", "nombre cuando se pulsa el carro: " + alimentosExternosTotales.get(i).getNombreElemento());
                }
                startActivityForResult(intent, REQUEST_CODE_CARRO);
                return true;
            //Botón de retroceso
            case R.id.homeAsUp:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Método para comprobar si hay algún alimento repetido en la lista y no ponerlo
    public void comprobarRepetidos(ArrayList<ComponenteListaCompra> comp, ComponenteListaCompra componenteNuevo){
        int contador=0;
        for (ComponenteListaCompra c : comp){
            if(componenteNuevo.getNombreElemento().equals(c.getNombreElemento())){
                contador++;
            }
        }
        if(contador==0){
            alimentosExternosTotales.add(componenteNuevo);
        }
    }
}
