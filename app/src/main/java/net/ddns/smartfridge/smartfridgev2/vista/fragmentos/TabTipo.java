package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapterFiltroTipos;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Tipo;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * The type Tab tipo.
 */
public class TabTipo extends Fragment {
    private static MySQLHelper myHelper;//Para trabajar con la bbdd de MySQL
    private static ArrayList<Receta> recetas;//Para almacenar las recetas recogidas de la bbdd
    //private static ArrayList<Receta> recetasTipo;//Para almacenar las recetas recogidas de la bbdd
    //private static ArrayList<Bitmap> imagenesTipo;//Para almacenar las imágenes de las recetas de la bbdd
    private RecyclerView recyclerView;
    private CustomRecyclerViewAdapterFiltroTipos adapter;
    private Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recetas = new ArrayList<Receta>();
        //recetasTipo = new ArrayList<Receta>();
        //imagenesTipo = new ArrayList<>();
        new SacarTipos().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_tipo, container, false);
        recyclerView = v.findViewById(R.id.rvTabTipo);
        adapter = new CustomRecyclerViewAdapterFiltroTipos(getActivity(), this, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
        return v;
    }

    /**
     * The type Filtrar por tipo.
     */
    public class FiltrarPorTipo extends AsyncTask<Integer, Void, ArrayList<Receta>>{

        @Override
        protected ArrayList<Receta> doInBackground(Integer... ints) {
            try {
                myHelper = new MySQLHelper();
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos todas las recetas
                Log.d("dialogo", "asynctask");
                recetas = myHelper.filtrarRecetaPorTipo(ints[0]);
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
            }
            for(int i = 0;i<recetas.size(); i++){
                Log.d("intentService", "Receta en intentService: " + recetas.get(i).getTituloReceta());
            }
            return recetas;
        }

        @Override
        protected void onPostExecute(ArrayList<Receta> recetas) {
            super.onPostExecute(recetas);
            //recetasTipo = recetas;
            ArrayList<Bitmap> imagenes = new ArrayList<>();
            for (Receta item : recetas) {
                imagenes.add(item.getImagenReceta());
                item.setImagenReceta(null);
            }
            intent = new Intent();
            intent.putExtra("filtro", recetas);
            intent.putExtra("filtroImagenes" , imagenes);
            getActivity().setResult(getActivity().RESULT_OK, intent);
            try {
                myHelper.cerrarConexion();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
            for(int i = 0;i<recetas.size(); i++){
                Log.d("intentService", "Receta filtrado: " + recetas.get(i).getTituloReceta());
            }
            getActivity().finish();
        }
    }

    /**
     * The type Sacar tipos.
     */
    public static class SacarTipos extends AsyncTask<Void, Void, ArrayList<Tipo>>{
        private ArrayList<Tipo> tipos;//Para recoger los resultados de la consulta en el AsyncTask

        @Override
        protected ArrayList<Tipo> doInBackground(Void... voids) {
            try {
                tipos = new ArrayList<>();
                myHelper = new MySQLHelper();
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos todas las recetas
                tipos = myHelper.sacarTipoReceta();
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
            }
            for(int i = 0;i<recetas.size(); i++){
                Log.d("intentService", "Receta en intentService: " + recetas.get(i).getTituloReceta());
            }
            return tipos;
        }

        @Override
        protected void onPostExecute(ArrayList<Tipo> tipos) {
            super.onPostExecute(tipos);
            try {
                myHelper.cerrarConexion();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
            for(int i = 0;i<tipos.size(); i++){
                Log.d("intentService", "Receta tipo: " + tipos.get(i).getDescripcion());
            }
        }
    }

    /**
     * Iniciar async.
     *
     * @param position the position
     */
    public void iniciarAsync(int position){
        new FiltrarPorTipo().execute(position + 1);
    }
/*
    public static ArrayList<Receta> getRecetasTipo() {
        return recetasTipo;
    }

    public static ArrayList<Bitmap> getImagenesTipo() {
        return imagenesTipo;
    }*/
}