package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;

import java.util.ArrayList;

/**
 * Activity donde se muestran todos los alimentos almacenados en la bbdd interna, llamada MiNevera
 */
public class MiNeveraActivity extends AppCompatActivity {
    private AlimentoDB alimentoDB;
    private Cursor cursor;
    private RecyclerView rvMiNevera;
    private CustomRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private static ArrayList<Bitmap> imagenesDetalles;
    private static final int DETALLES_ACTIVITY = 16;
    private int sort = 1;
    private static final int VALOR=16;//Valor para redimensionar la imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imagenesDetalles = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_nevera);
        imagenesDetalles = new ArrayList<>();
        alimentoDB = new AlimentoDB(this);
        cursor = alimentoDB.getAlimentos();
        iniciarRecyclerView();
        mostrarTutorial();
    }

    /**
     * Método para iniciar el RecyclerView que muestra los alimentos
     */
    private void iniciarRecyclerView() {
        rvMiNevera = (RecyclerView) findViewById(R.id.rvMiNevera);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerViewAdapter = new CustomRecyclerViewAdapter(cursor, this);
        cursor.close();
        rvMiNevera.setLayoutManager(layoutManager);
        rvMiNevera.setAdapter(recyclerViewAdapter);
    }

    /**
     * Método para inciar un activity mostrando los detalles de la fila que se haya pulsado
     *
     * @param alimentos Lista con los alimentos a mostrar
     * @param position  posicion del elemento pulsado
     */
    public void iniciardetalles(ArrayList<Alimento> alimentos, int position) {
        ArrayList<Alimento> alimentosSinFoto = new ArrayList<>();
        imagenesDetalles.clear();
        for (Alimento item : alimentos) {
            this.imagenesDetalles.add(item.getImagen());
            alimentosSinFoto.add(
                    new Alimento(
                            item.getId(),
                            item.getNombreAlimento(),
                            item.getCantidad(),
                            item.getDias_caducidad(),
                            item.getFecha_registro(),
                            item.getFecha_caducidad(),
                            null));
        }

        Intent intent = new Intent(this, DetallesActivity.class);
        intent.putExtra(getString(R.string.ali_no_foto), alimentosSinFoto);
        intent.putExtra(getString(R.string.posicion), position);
        intent.putExtra(getString(R.string.clasePadre), getString(R.string.mi_nevera));
        startActivityForResult(intent, DETALLES_ACTIVITY);
    }

    /**
     * Gets imagen detalles.
     *
     * @return the imagen detalles
     */
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
        if (requestCode == DETALLES_ACTIVITY) {
            cursor = alimentoDB.getAlimentos();
            recyclerViewAdapter.setCursor(cursor);
            recyclerViewAdapter.cargarArray();
            recyclerViewAdapter.notifyDataSetChanged();
            cursor.close();
        }

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
                recyclerViewAdapter.sortRecyclerView(sort);
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

    /**
     * Método para mostrar el tutorial al usuario
     */
    private void mostrarTutorial(){
        final SharedPreferences tutorialShowcases = getSharedPreferences(getString(R.string.tutorialSP), MODE_PRIVATE);
        boolean run;
        run = tutorialShowcases.getBoolean(getString(R.string.tutorial5), true);

        if(run){//Comprobamos si ya se ha mostrado el tutorial en algún momento

            //Creamos un nuevo LayoutParms para cambiar el botón de posición
            final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lps.addRule(RelativeLayout.CENTER_HORIZONTAL);
            // Ponemos márgenes al botón
            int margin = ((Number) (getResources().getDisplayMetrics().density * VALOR)).intValue();
            lps.setMargins(margin, margin, margin, margin);

            //Creamos el ShowCase
            final ShowcaseView s = new ShowcaseView.Builder(this)
                    .setTarget( new ViewTarget( ((View) findViewById(R.id.rvMiNevera)) ) )
                    .setContentTitle(getString(R.string.pulsa_detalle))
                    .hideOnTouchOutside()
                    .build();
            s.setButtonText(getString(R.string.siguiente));
            s.setButtonPosition(lps);
            //Comprobamos que el botón del showCase se pulsa para hacer el switch. Se va acomprobar el contador para ver si se muestra el siguiente showcas
            s.overrideButtonClick(new View.OnClickListener() {
                int contadorS = 0;
                Target targetOrdenar = new Target() {
                    @Override
                    public Point getPoint() {
                        return new ViewTarget(searchView.findViewById(R.id.menuSort)).getPoint();
                    }
                };
                @Override
                public void onClick(View v) {
                    contadorS++;
                    switch (contadorS) {
                        case 1:
                           //Cambiamos la variable en el sharedPreferences para que no se vuelva a mostrar el tutorial
                            SharedPreferences.Editor tutorialShowcasesEdit = tutorialShowcases.edit();
                            tutorialShowcasesEdit.putBoolean(getString(R.string.tutorial5), false);
                            tutorialShowcasesEdit.apply();
                            s.hide();
                            break;
                    }
                }
            });
        }
    }
}
