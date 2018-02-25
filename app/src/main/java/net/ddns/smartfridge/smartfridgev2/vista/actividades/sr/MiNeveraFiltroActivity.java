package net.ddns.smartfridge.smartfridgev2.vista.actividades.sr;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnCloseClickListener;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapterNeveraRecetas;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;

import java.sql.SQLException;
import java.util.ArrayList;

public class MiNeveraFiltroActivity extends AppCompatActivity {
    private MySQLHelper myHelper;//Para trabajar con la bbdd de mysql
    private AlimentoDB alimentoDB;
    private Cursor cursor;
    private RecyclerView rvMiNevera;
    private CustomRecyclerViewAdapterNeveraRecetas adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private int sort = 1;
    private LinearLayout llChipsFiltro;
    private ArrayList<Receta> recetas;
    private Intent intent;

    private Context context;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_nevera_filtro);
        myHelper = new MySQLHelper();
        context = this;
        activity = this;
        alimentoDB = new AlimentoDB(this);
        cursor = alimentoDB.getAlimentos();
        //Cargamos el recyclerView
        llChipsFiltro = findViewById(R.id.llChipsFiltro);
        iniciarRecyclerView();

        findViewById(R.id.ibFiltrarMiNevera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new mostrarRecetasFiltroString().execute(adapter.getAlimentosSeleccionados());
            }
        });

    }

    public void addChip(String text){
        final Chip chip = new Chip(this);
        chip.setClosable(true);
        chip.setChipText(text);
        chip.setOnCloseClickListener(new OnCloseClickListener() {
            private Chip chipRef = chip;
            @Override
            public void onCloseClick(View v) {
                adapter.eliminarSeleccionado(chipRef.getChipText());
                llChipsFiltro.removeView(chipRef);
            }
        });
        llChipsFiltro.addView(chip);
    }

    private void iniciarRecyclerView() {
        rvMiNevera = (RecyclerView) findViewById(R.id.rvMiNevera);
        layoutManager = new GridLayoutManager(this, 2);
        adapter = new CustomRecyclerViewAdapterNeveraRecetas(cursor, this, this);
        rvMiNevera.setLayoutManager(layoutManager);
        rvMiNevera.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        cursor.close();
        alimentoDB.cerrarConexion();
    }

    public class mostrarRecetasFiltroString extends AsyncTask<ArrayList<String>, Void, ArrayList<Receta>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ustomDialogProgressBar.showDialogCuadrado();
        }

        @Override
        protected ArrayList<Receta> doInBackground(ArrayList<String>... voids) {
            recetas = new ArrayList<>();
            myHelper = new MySQLHelper();
            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Vemos si tenemos que hacer la consulta en función del título o de los spinner
                Log.d("nepeee", "doInBackground: " + voids[0]);
                recetas = myHelper.filtrarRecetaMiNevera(voids[0]);
                Log.d("nepeee", "doInBackground: " +recetas.size());

                Log.d("intentServiced", "doInBackground: " + recetas.size());
                for(int i = 0;i<recetas.size(); i++){
                    Log.d("intentServiced", "Receta en intentService: " + recetas.get(i).getTituloReceta());
                }
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
            }
            return recetas;
        }

        @Override
        protected void onPostExecute(ArrayList<Receta> recetas) {
            super.onPostExecute(recetas);
            if (recetas.size()>0) {
                Log.d("otros", "receta: " + recetas.get(0).getTituloReceta());
                ArrayList<Bitmap> imagenes = new ArrayList<>();
                for (Receta item : recetas) {
                    imagenes.add(item.getImagenReceta());
                    item.setImagenReceta(null);
                }
                Intent intent = new Intent();
                intent.putExtra("filtro", recetas);
                intent.putExtra("filtroImagenes" , imagenes);
                activity.setResult(activity.RESULT_OK, intent);
            } else {
                //Si no hay coincidencias, se mostrará un toast al usuario
                Toast.makeText(context, "No hay resultados", Toast.LENGTH_SHORT).show();
            }
            try {
                myHelper.cerrarConexion();

                activity.finish();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
        }
    }
}
