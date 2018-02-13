package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;

import java.util.ArrayList;
import java.util.List;

public class MostrarProductosListaActivity extends AppCompatActivity {
    private Intent intent;
    private ListView listView;
    private ShareActionProvider mShareActionProvider;
    private ArrayList<String> alimentos;
    private ArrayList<ComponenteListaCompra> componentesListaCompra;

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

    private void cargarAdapter(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, (R.layout.fila_producto),
                (R.id.tvNombreProductoFilaProducto),
                alimentos);

        listView = (ListView) findViewById(R.id.lvMostrarProductos);

        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
//        getMenuInflater().inflate(R.menu.menu_mostrar_productos, menu);

        // Locate MenuItem with ShareActionProvider
      //  MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
//        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
