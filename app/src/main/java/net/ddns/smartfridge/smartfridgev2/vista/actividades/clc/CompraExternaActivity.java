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

/**
 * Activity que muestra una lista con las categorias de clasificación de los alimentos en la bbdd externa para poder añadirlos a la lista
 */
public class CompraExternaActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SIGUIENTE = 468;//Código de respuesta para el activity que al que va a llamar, el detalle de cada elemento
    private static final int REQUEST_CODE_CARRO = 834;//Código de respuesta para el activity que al que va a llamar, el carro de la compra
    private static final String[] categorias = {"Verdura", "Carne", "Fruta", "Pescado", "Bebida", "Embutido", "Frutos secos", "Desayuno", "Otros"};//Array con los nombres
    //de las categorías de alimentos para coger el seleccionado por el usuario
    private String seleccion;//Seleccion hecha por el usuario
    private Intent intent;//Para pasar información entre activitys
    private ArrayList<ComponenteListaCompra> alimentosExternos;//Para almacenar el resultado de la seleccion del usuario y pasárselo al intent que nos llamó
    private ArrayList<ComponenteListaCompra> alimentosExternosTotales;//Para almacenar todos los alimentos y enviarlos a la lista de la compra
    private ArrayList<ComponenteListaCompra> alimentosDevueltos;//Para recoger los alimentos que se van a devolver para incorporarlos a la lista de la compra
    private ArrayList<ComponenteListaCompra> alimentosDevueltosCarro;//Para recoger los alimentos que se van a devolver para incorporarlos a la lista de la compra y que provienen del carro
    private CustomRecyclerViewAdapterRevistaMain adapter; //Adaptador
    private RecyclerView.LayoutManager layoutManager; //LayoutManager para el recyclerview
    private RecyclerView recyclerView;//RecyclerView para visualizar los elementos
    private String clasePadre;//Para ver de qué clase tenemos que enviar el array: si viene del carro de la compra, lo indicará, ya que podrá estar modificado al borrar algún elemento

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_externa);
        alimentosExternos = new ArrayList<ComponenteListaCompra>();
        alimentosExternosTotales = new ArrayList<ComponenteListaCompra>();
        alimentosDevueltos = new ArrayList<ComponenteListaCompra>();
        alimentosDevueltosCarro = new ArrayList<ComponenteListaCompra>();
        adapter = new CustomRecyclerViewAdapterRevistaMain(this, this);
        recyclerView = findViewById(R.id.rvCompraExterna);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * Método que abre un intent con los datos de los ingredientes según la categoría indicada
     *
     * @param categoria Categoria para filtrar
     */
    public void abrirCategoria(int categoria){
        seleccion = categorias[categoria];
        intent = new Intent(this, CategoriaActivity.class);
        //Le asignamos la categoría
        intent.putExtra(getString(R.string.categoria), seleccion);
        startActivityForResult(intent, REQUEST_CODE_SIGUIENTE);
    }

    //Programamos el onActivityResult para recoger el arraylist con los datos que ha seleccionado el usuario
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("componente", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        //Comrpobamos el intent que viene de vuelta del activity CategoriaActivity
        if (requestCode == REQUEST_CODE_SIGUIENTE) {
            Log.d("componente", "onActivityResult dentro del if");
            // Vemos que el resultado esté correcto
            if (resultCode == RESULT_OK) {
                //Recogemos los datos del intent y se los asignamos al ArrayList
                alimentosExternos = (ArrayList<ComponenteListaCompra>) data.getSerializableExtra(getString(R.string.result));
                Log.d("componente", "tamaño alimentosExternos: " + alimentosExternos.size());
                //Comprobamos si hay alguno repetido para enviárselo al activity del carro
                for(int i=0; i<alimentosExternos.size(); i++){
                    //alimentosExternosTotales.add(alimentosExternos.get(i));
                    comprobarRepetidos(alimentosExternosTotales, alimentosExternos.get(i));
                }
                //Para el Array de esta clase
                for(int i=0; i<alimentosExternos.size();i++) {
                    comprobarRepetidos(alimentosDevueltos, alimentosExternos.get(i));
                    Log.d("componente", "alimentos que van a la lista o al carro: " + alimentosExternos.get(i).getNombreElemento());
                    //alimentosDevueltos.add(alimentosExternos.get(i));
                    Log.d("componente", "alimento para incorporar a la lista de la compra: " + alimentosExternos.get(i).getNombreElemento());
                }
                clasePadre = data.getStringExtra("clasePadre");
            }
            //Comprobamos el intent que viene de vuelta del activity DetalleListaActitivity
        } else if(requestCode == REQUEST_CODE_CARRO){
            // Vemos que el resultado esté correcto
            if (resultCode == RESULT_OK) {
                //Recogemos los datos del intent y se los asignamos al ArrayList
                alimentosDevueltosCarro = (ArrayList<ComponenteListaCompra>) data.getSerializableExtra(getString(R.string.ele_lista));
                for(int i=0; i<alimentosDevueltosCarro.size(); i++){
                    Log.d("vuelta", "alimentos: " + alimentosDevueltosCarro.get(i).getNombreElemento());
                }
                clasePadre = data.getStringExtra(getString(R.string.clase_padre_minusculas));
                Log.d("vuelta", clasePadre);
            }
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
                intent.putExtra(getString(R.string.comp), alimentosExternosTotales);
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

    /**
     * Método para comprobar si hay algún alimento repetido en la lista y no ponerlo
     *
     * @param comp            Lista con todos los componentes que hay en ella
     * @param componenteNuevo nuevo componente que queremos incluir en la lista
     */
    public void comprobarRepetidos(ArrayList<ComponenteListaCompra> comp, ComponenteListaCompra componenteNuevo){
        int contador=0;
        for (ComponenteListaCompra c : comp){
            if(componenteNuevo.getNombreElemento().equals(c.getNombreElemento())){
                contador++;
            }
        }
        if(contador==0){
            alimentosExternosTotales.add(componenteNuevo);
            alimentosDevueltos.add(componenteNuevo);
        }
    }

    //Hacemos el setResult del intent aquí
    @Override
    public void onBackPressed() {
        intent = new Intent();
        if(clasePadre!=null) {
            if (clasePadre.equals(getString(R.string.carro))) {
                intent.putExtra(getString(R.string.ali_sel), alimentosDevueltos);
            } else if (clasePadre.equals(getString(R.string.detalle))) {
                intent.putExtra(getString(R.string.ali_sel), alimentosDevueltosCarro);
            }
        }
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
