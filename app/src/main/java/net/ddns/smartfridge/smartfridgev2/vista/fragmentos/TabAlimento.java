package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.content.Intent;
import android.graphics.Bitmap;
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

import android.widget.RadioGroup;
import android.widget.Toast;

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
    private String sentenciaSeleccion;//Para ejecutar la sentencia de búsqueda en la bbdd
    private ArrayList<Ingrediente> ingredientesSeleccionados;//Para almacenar los ingredientes seleccionados por el usuario
    private boolean contenga;//Para ver qué radiobutton se ha seleccionado
    private Intent intent;//Intent para pasar los datos al adapter y refrescarlos
    private ArrayList<Bitmap> arrayFotoReceta;//Array para guardar las imágenes de las recetas

    private int contador = 0;
    private ChipsInput linearLayout;

    private ArrayList<String> ingredientesComprobacion;

    public TabAlimento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myHelper = new MySQLHelper();
        ingredientesSeleccionados = new ArrayList<>();
        ingredientesComprobacion = new ArrayList<>();
        new GetAllIngredientes().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_alimento, container, false);
        act = (AutoCompleteTextView)v.findViewById(R.id.acAlimentosReceta);
        RadioGroup radioGroup = (RadioGroup) v .findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.rbTenga:
                        Log.d("check", "boton pulsado Tenga");
                        //Si queremos alimentos que contengan, hacemos la select correspondiente para pasársela al AsyncTask
                        contenga = true;
                        Log.d("check", "contenga: " + contenga);
                        //sentencia = myHelper.montarSentenciaSi(ingredientesSeleccionados);
                        break;
                    case R.id.rbNoTenga:
                        Log.d("check", "boton pulsado No Tenga");
                        contenga = false;
                        Log.d("check", "contenga: " + contenga);
                        //Si queremos que no tenga alimentos, montamos la select correspondiente
                        //sentencia = myHelper.montarSentenciaNo(ingredientesSeleccionados);
                        break;
                }
            }
        });

        v.findViewById(R.id.ibBuscar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Le damos el arrayList con los datos indicados por el usuario
                //ingredientesSeleccionados = fake();
                //Log.d("check", "sentencia: " + sentenciaSeleccion);
                //Log.d("check", "boton pulsado Búsqueda");
                if (contenga){
                    //Log.d("check", "contenga onClick: " + contenga);
                    //Si queremos alimentos que contengan, hacemos la select correspondiente para pasársela al AsyncTask
                    sentenciaSeleccion = myHelper.montarSentenciaSi(ingredientesSeleccionados);
                    Log.d("check", "sentencia onClick: " + sentenciaSeleccion);
                    //Llamamos al asyncTask pasándole la select correspondiente
                    new CogerRecetasFiltro().execute(sentenciaSeleccion);
                } else {
                    Log.d("check", "contenga onClick: " + contenga);
                    //Si queremos que no tenga alimentos, montamos la select correspondiente
                    sentenciaSeleccion = myHelper.montarSentenciaNo(ingredientesSeleccionados);
                    Log.d("check", "sentencia onClick: " + sentenciaSeleccion);
                    //Llamamos al asyncTask pasándole la select correspondiente
                    new CogerRecetasFiltro().execute(sentenciaSeleccion);
                }

                String alimento = act.getText().toString();
                Log.d("autocomplete", alimento);
            }
        });

        v.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngrediente();
            }
        });

        return v;
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
            ingredientesComprobacion.add(ing.get(i).getNombreIngrediente());
            Log.d("llllll", "generarSugerencias: " + ingredientesComprobacion.get(i));
        }
        return alimentos;
    }

    public void addIngrediente(){
        String ingrediente = act.getText().toString();
        Log.d("ingrediente", "texto: " + ingrediente);
        if(ingredientesComprobacion.contains(ingrediente)){
            Log.d("ingrediente", "entra por el if");
            ingredientesSeleccionados.add(ingredientes.get(ingredientesComprobacion.indexOf(ingrediente)));
        } else {
            Log.d("ingrediente", "entra por el else, va vacío");
            Toast.makeText(getContext(), "No existe dicho ingrediente", Toast.LENGTH_SHORT).show();
        }
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
            String [] nombres = generarSugerencias(ingredientes);
            act.setAdapter(new ArrayAdapter<String>(
                    getContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    nombres
            ));
        }
    }

    public class CogerRecetasFiltro extends AsyncTask<String, Void, ArrayList<Receta>> {

        @Override
        protected ArrayList<Receta> doInBackground(String... strings) {

            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                Log.d("check", "Sentencia en AsyncTask: " + strings[0]);
                //Recogemos todas las recetas
                recetas = myHelper.filtrarReceta(strings[0]);
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
}