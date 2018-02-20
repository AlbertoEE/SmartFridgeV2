package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapterRecetas;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
//import net.ddns.smartfridge.smartfridgev2.modelo.servicios.RecetasIntentService;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.sr.FiltroRecetaActivity;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainSr extends Fragment {
    private ParallaxRecyclerView recyclerView;
    private CustomRecyclerViewAdapterRecetas adapter;
    //private RecetasIntentService service;
    private ArrayList<Receta> recetas;
    private static final int REQUEST_FILTRO = 506;
    private CustomDialogProgressBar customDialogProgressBar;
    private MySQLHelper myHelper;

    public MainSr() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_sr, container, false);
        //service = new RecetasIntentService();
        //service.setMainSr(this);
        //getContext().startService(new Intent(getContext(), RecetasIntentService.class));
        recyclerView = (ParallaxRecyclerView) view.findViewById(R.id.rvRecetas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new mostrarRecetas().execute();
        com.getbase.floatingactionbutton.FloatingActionButton botonFiltro = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.filtros);
        com.getbase.floatingactionbutton.FloatingActionButton botonAleatorio = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.aleatorio);
        com.getbase.floatingactionbutton.FloatingActionButton botonNevera = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.miNevera);
        botonFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FiltroRecetaActivity.class);
                startActivityForResult(i, REQUEST_FILTRO);
            }
        });
        botonAleatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        botonNevera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        customDialogProgressBar =  new CustomDialogProgressBar(getActivity());
        return view;
    }

    public void crearAdapter(ArrayList<Receta> recetas){
        this.recetas = recetas;
        adapter = new CustomRecyclerViewAdapterRecetas(recetas, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class mostrarRecetas extends AsyncTask<Void, Void, ArrayList<Receta>>{

        @Override
        protected ArrayList<Receta> doInBackground(Void... voids) {
            myHelper = new MySQLHelper();
            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos las recetas una a una
                recetas = myHelper.recogerRecetas();
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
            }
        /*    for(int i = 0;i<recetas.size(); i++){
                Log.d("intentService", "Receta en intentService: " + recetas.get(i).getTituloReceta());
            }*/
            return recetas;
        }

        @Override
        protected void onPostExecute(ArrayList<Receta> recetas) {
            super.onPostExecute(recetas);
            try {
                myHelper.cerrarConexion();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
        }
    }
}
