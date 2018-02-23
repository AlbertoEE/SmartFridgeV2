package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabOtros extends Fragment {
    private EditText buscar;//TextView donde el usuario va a introducir los criterios de búsqueda
    private String texto;//La búsqueda del usuario
    private CustomDialogProgressBar customDialogProgressBar;
    private MySQLHelper myHelper;//Para trabajar con la bbdd
    private ArrayList<Receta> recetas;

    public TabOtros() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customDialogProgressBar = new CustomDialogProgressBar(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_otros, container, false);
        buscar = (EditText)v.findViewById(R.id.actvRecetas);
        texto = buscar.getText().toString();
        Log.d("filtro", "texto a buscar: " + texto);
        return v;
    }

    public class mostrarRecetasFiltro extends AsyncTask<String, Void, ArrayList<Receta>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogProgressBar.showDialogCuadrado();
        }

        @Override
        protected ArrayList<Receta> doInBackground(String... voids) {
            recetas = new ArrayList<>();
            myHelper = new MySQLHelper();
            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos las recetas una a una
                recetas = myHelper.recogerRecetaTitulo(voids[0]);
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
                customDialogProgressBar.endDialog();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
        }


    }

}
