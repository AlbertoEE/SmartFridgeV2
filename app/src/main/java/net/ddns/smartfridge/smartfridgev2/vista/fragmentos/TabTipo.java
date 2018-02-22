package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Tipo;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;
import java.util.ArrayList;


public class TabTipo extends Fragment {
    private static MySQLHelper myHelper;//Para trabajar con la bbdd de MySQL
    private static ArrayList<Receta> recetas;//Para almacenar las recetas recogidas de la bbdd

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       recetas = new ArrayList<Receta>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_tipo, container, false);
        //Meter esto en la fila del adapter
     /*   Dialogos d = new Dialogos(getContext(),getActivity());
        Tipo t = new Tipo(1, "arroz");
        d.dialogoFiltroTipo(t);*/
        return v;
    }

    public static class FiltrarPorTipo extends AsyncTask<Integer, Void, ArrayList<Receta>>{

        @Override
        protected ArrayList<Receta> doInBackground(Integer... ints) {
            try {
                myHelper = new MySQLHelper();
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos todas las recetas
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
            try {
                myHelper.cerrarConexion();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
            for(int i = 0;i<recetas.size(); i++){
                Log.d("intentService", "Receta filtrado: " + recetas.get(i).getTituloReceta());
            }
        }
    }
}