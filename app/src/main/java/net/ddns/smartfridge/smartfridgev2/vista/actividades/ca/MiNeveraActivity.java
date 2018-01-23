package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapter;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;

import java.io.Serializable;

public class MiNeveraActivity extends AppCompatActivity {
    private AlimentoDB alimentoDB;
    private Cursor cursor;
    private RecyclerView rvMiNevera;
    private CustomRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private static Bitmap imagenDetalles;
    private static final int DETALLES_ACTIVITY = 16;
    private int sort = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imagenDetalles = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_nevera);

        alimentoDB = new AlimentoDB(this);
        cursor = alimentoDB.getAlimentos();

        iniciarRecyclerView();
    }

    private void iniciarRecyclerView(){
        rvMiNevera = (RecyclerView)findViewById(R.id.rvMiNevera);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerViewAdapter = new CustomRecyclerViewAdapter(cursor, this);

        rvMiNevera.setLayoutManager(layoutManager);
        rvMiNevera.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public static void setImagenDetalles(Bitmap bitmap) {
        imagenDetalles = bitmap;
    }

    public void iniciardetalles(Alimento alimento){
        this.imagenDetalles = alimento.getImagen();
        alimento = new Alimento(
                alimento.getId(),
                alimento.getNombreAlimento(),
                alimento.getCantidad(),
                alimento.getDias_caducidad(),
                alimento.getFecha_registro(),
                alimento.getFecha_caducidad(),
                null);
        Intent intent = new Intent(this, DetallesActivity.class);
        intent.putExtra("Alimento", alimento);
        intent.putExtra("ClasePadre", "MiNeveraActivity");
        startActivityForResult(intent, DETALLES_ACTIVITY);
    }

    public static Bitmap getImagenDetalles(){
        return imagenDetalles;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(true);
        searchView.onActionViewCollapsed();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerViewAdapter.filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                    recyclerViewAdapter.filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == DETALLES_ACTIVITY ){
                cursor = alimentoDB.getAlimentos();
                recyclerViewAdapter.setCursor(cursor);
                recyclerViewAdapter.cargarArray();
                recyclerViewAdapter.notifyDataSetChanged();
            }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSort:
                if(sort == 1){
                    sort = -1;
                } else if(sort == -1){
                    sort = 1;
                }
                recyclerViewAdapter.sortRecyclerView(sort);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
