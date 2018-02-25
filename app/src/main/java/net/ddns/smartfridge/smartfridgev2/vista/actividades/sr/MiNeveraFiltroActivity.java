package net.ddns.smartfridge.smartfridgev2.vista.actividades.sr;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapterNeveraRecetas;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;

import java.util.ArrayList;

public class MiNeveraFiltroActivity extends AppCompatActivity {
    private AlimentoDB alimentoDB;
    private Cursor cursor;
    private RecyclerView rvMiNevera;
    private CustomRecyclerViewAdapterNeveraRecetas adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private static ArrayList<Bitmap> imagenesDetalles;
    private static final int DETALLES_ACTIVITY = 16;
    private int sort = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imagenesDetalles = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_nevera_filtro);
        imagenesDetalles = new ArrayList<>();
        alimentoDB = new AlimentoDB(this);
        cursor = alimentoDB.getAlimentos();
        //Cargamos el recyclerView
        iniciarRecyclerView();
    }

    private void iniciarRecyclerView() {
        rvMiNevera = (RecyclerView) findViewById(R.id.rvMiNevera);
        layoutManager = new GridLayoutManager(this, 2);
        //Sustituir por el recycler
        //recyclerViewAdapter = new CustomRecyclerViewAdapter(cursor, this);
        cursor.close();
        rvMiNevera.setLayoutManager(layoutManager);
        rvMiNevera.setAdapter(adapter);
        //recyclerViewAdapter.notifyDataSetChanged();
    }

    public static ArrayList<Bitmap> getImagenDetalles() {
        return imagenesDetalles;
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
                adapter.filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSort:
                if (sort == 1) {
                    sort = -1;
                } else if (sort == -1) {
                    sort = 1;
                }
                adapter.sortRecyclerView(sort);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Cerramos la conexión a la bbdd en el onDestroy

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alimentoDB.cerrarConexion();
    }
}
