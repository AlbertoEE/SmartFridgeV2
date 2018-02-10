package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.ddns.smartfridge.smartfridgev2.R;

import java.util.List;

public class MostrarProductosListaActivity extends AppCompatActivity {
    private Intent intent;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_productos_lista);

        intent = getIntent();
        cargarAdapter();
    }

    private void cargarAdapter(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, (R.layout.fila_producto),
                (R.id.tvNombreProductoFilaProducto),
                intent.getStringArrayExtra("ListaProductos"));

        listView = (ListView) findViewById(R.id.lvMostrarProductos);

        listView.setAdapter(arrayAdapter);
    }
}
