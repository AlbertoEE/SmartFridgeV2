package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabAlimento extends Fragment {
    private MySQLHelper myHelper;//Para trabajar con la bbdd de mysql
    private ArrayList<Ingrediente> ingredientes;//Para almacenar los ingredientes recogidos de la bbdd
    private AutoCompleteTextView act;//Para coger la referencia al elemento del layout
    private ArrayList<Receta> recetas;//Para almacenar las recetas recogidas de la bbdd
    private String sentencia;//Para ejecutar la sentencia de búsqueda en la bbdd
    private ArrayList<Ingrediente> ingredientesSeleccionados;//Para almacenar los ingredientes seleccionados por el usuario

    public TabAlimento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myHelper = new MySQLHelper();
        ingredientesSeleccionados = new ArrayList<>();
        new GetAllIngredientes().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_alimento, container, false);
        act = (AutoCompleteTextView)v.findViewById(R.id.acAlimentosReceta);
        return v;
    }
    //Método para realizar la consulta a la bbdd a partir de los datos recogidos
    public void comprobarRB(View view) {
        //Miramos si está seleccionado el radiobutton
        boolean checked = ((me.omidh.liquidradiobutton.LiquidRadioButton) view).isChecked();
        String alimento = act.getText().toString();
        Log.d("autocomplete", alimento);
        //Hacemos un case con las opciones de cada radiobutton
        switch(view.getId()) {
            case R.id.rbTenga:
                if (checked)
                    //Si queremos alimentos que contengan, hacemos la select correspondiente para pasársela al AsyncTask
                    sentencia = myHelper.montarSentenciaSi(ingredientesSeleccionados);
                    //Llamamos al asyncTask pasándole la select correspondiente

                    break;
            case R.id.rbNoTenga:
                if (checked)
                    //Si queremos que no tenga alimentos, montamos la select correspondiente
                    sentencia = myHelper.montarSentenciaNo(ingredientesSeleccionados);
                //Llamamos al asyncTask pasándole la select correspondiente
                    break;

        }
    }

    private String[] generarSugerencias(ArrayList<Ingrediente> ing){
        int count = ing.size();
        int contador = 0;
        Log.d("Count", "generarSugerencias: " + count);
        String[] alimentos = new String[count];
        //Log.d("String", "NOMBRE: " + cursor.getString(0));
        //Log.d("String", "NOMBRE: " + cursor.getString(1));
        for (int i=0; i<count; i++){
            alimentos[contador] = ing.get(i).getNombreIngrediente();
            contador++;
        }
        return alimentos;
    }

    public class GetAllIngredientes extends AsyncTask<Void, Void, ArrayList<Ingrediente>> {

        @Override
        protected ArrayList<Ingrediente> doInBackground(Void... voids) {
            myHelper = new MySQLHelper();
            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos todos los ingredientes
                ingredientes = myHelper.recogerIngredientes();
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
            }
            for(int i = 0;i<ingredientes.size(); i++){
                Log.d("intentService", "Receta en intentService: " + ingredientes.get(i).getNombreIngrediente());
            }
            return ingredientes;
        }

        @Override
        protected void onPostExecute(ArrayList<Ingrediente> ingredientes) {
            super.onPostExecute(ingredientes);
            try {
                myHelper.cerrarConexion();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
            generarSugerencias(ingredientes);
        }
    }

    public class CogerRecetasFiltro extends AsyncTask<String, Void, ArrayList<Receta>> {

        @Override
        protected ArrayList<Receta> doInBackground(String... strings) {

            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos todas las recetas
                if(strings[0].equals("SI")){
                    //sentencia = montarSentenciaSi();
                } else if (strings[0].equals("NO")){
                    //sentencia = montarSentenciaNo();
                }
                recetas = myHelper.filtrarReceta(sentencia);
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
        }
    }



}
