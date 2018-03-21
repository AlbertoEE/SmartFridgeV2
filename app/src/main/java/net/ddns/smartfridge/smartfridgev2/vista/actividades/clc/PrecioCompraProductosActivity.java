package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewSuper;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Precio;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Activity para ver el detalle de los precios por cada supermercado, por producto
 */
public class PrecioCompraProductosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomRecyclerViewSuper adapter;
    private ArrayList<Precio> precios;
    private String superMercado;
    private Double total;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precio_compra_productos);
        DecimalFormat f = new DecimalFormat("##.00");

        precios = (ArrayList<Precio>) getIntent().getExtras().getSerializable("Precios");
        superMercado = (String) getIntent().getExtras().getString("Super");
        total = getIntent().getExtras().getDouble("Total");

        tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(f.format(total) + "€");

        recyclerView = findViewById(R.id.rvProductosPrecios);
        adapter = new CustomRecyclerViewSuper(this, precios, superMercado);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
