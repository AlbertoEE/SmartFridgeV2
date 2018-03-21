package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity para mostrar todos los alimentos que hay en la lista de la compra
 */
public class MostradorProductosListaActivity extends AppCompatActivity {
    private Intent intent;
    private ListView listView;
    //private ShareActionProvider mShareActionProvider;
    private ArrayList<String> alimentos;
    private ArrayList<ComponenteListaCompra> componentesListaCompra;
    private Intent compartir;//Creamos el intent para compartir la lista por mail
    private String sentencia;//El cuero del mensaje que se envía por mail

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_productos_lista);
        alimentos = new ArrayList<>();
        intent = getIntent();
        componentesListaCompra = (ArrayList<ComponenteListaCompra>) intent.getSerializableExtra("ListaProductos");
        for (ComponenteListaCompra item: componentesListaCompra) {
            alimentos.add(item.getNombreElemento());
        }
        cargarAdapter();
    }

    /**
     * Método para cargar el adaptador para ver los alimentos
     */
    private void cargarAdapter(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, (R.layout.fila_producto),
                (R.id.tvNombreProductoFilaProducto),
                alimentos);

        listView = (ListView) findViewById(R.id.lvMostrarProductos);

        listView.setAdapter(arrayAdapter);
    }

    /**
     * @see 'https://developer.android.com/reference/android/app/Activity.html?hl=es-419#onCreateOptionsMenu(android.view.Menu)'
     */
    //Para crear el ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mostrar_productos, menu);
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
            case R.id.miShare:
                //Compartiremos a través de un intent implícito los datos de la lista
                sentencia="";
                //Generamos el listado con los elementos de la lista
                for(String nombre : alimentos){
                    sentencia += ("- " + nombre + "\n");
                }
                Log.d("compartir", "sentencia:" + sentencia);
                //Creamos el intent
                compartir = new Intent();
                compartir.setAction(Intent.ACTION_SEND);
                //compartir.putExtra(Intent.EXTRA_EMAIL, new String[]{"raquel.menciac@gmail.com", "smartfridgespain@gmail.com"});
                compartir.putExtra(Intent.EXTRA_SUBJECT, "Lista de la compra compartida");
                compartir.putExtra(Intent.EXTRA_TEXT, "Hola,\nTe envío la lista de la compra:\n" + sentencia + "Gracias.");
                compartir.setType("*/*");
                startActivity(Intent.createChooser(compartir, "Enviar lista de la compra por Email"));
                Toast.makeText(this, "Preparando el envío.", Toast.LENGTH_SHORT).show();
                Log.d("compartir", "Enviada lista");
                if (compartir.resolveActivity(getPackageManager()) != null) {
                    //Para que elijamos cómo compartir, con qué app
                    startActivity(Intent.createChooser(compartir, "Seleccione aplicación"));
                } else {
                    Toast.makeText(this, "No hay ninguna aplicación disponible para enviar la lista.", Toast.LENGTH_LONG).show();
                }
                return true;
            //Botón de ver cuánto le cuesta la compra
            case R.id.miCompra:
                intent = new Intent(this, PrecioCompraActivity.class);
                //Le pasamos el arrayList con los productos que contiene esta lista de la compra
                intent.putExtra("productos", componentesListaCompra);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
