package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.content.Context;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabOtros extends Fragment {
    private EditText buscar;//TextView donde el usuario va a introducir los criterios de búsqueda
    private String texto;//La búsqueda del usuario
    private CustomDialogProgressBar customDialogProgressBar;
    private MySQLHelper myHelper;//Para trabajar con la bbdd
    private ArrayList<Receta> recetas;
    private ArrayList<String> tiempo;//Array para guardar los ids de la tabla del tiempo de las recetas
    private ArrayList<String> dificultad;//Array para guardar los ids de la tabla de dificultad de las recetas
    private Spinner spinnerT;//Para coger la referencia del spinner del tiempo
    private Spinner spinnerD;//Para coger la referencia del spinner de la duración
    private Context contexto;//Para el contexto del activity
    private int procedencia;//Para ver por dónde se va a realizar la búsqueda

    public TabOtros() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customDialogProgressBar = new CustomDialogProgressBar(getActivity());
        //Cogemos la referencia al activity donde se sacan los datos
        TabAlimento ta = (TabAlimento)getFragmentManager().findFragmentByTag("tab1");
        //Recogemos los valores que necesitamos para los comboBox
        tiempo=ta.getTiempo();
        dificultad = ta.getDif();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_otros, container, false);
        buscar = (EditText)v.findViewById(R.id.tvRecetasD);

        spinnerT = (Spinner) v.findViewById(R.id.spnTiempo);
        spinnerD = (Spinner) v.findViewById(R.id.spnDificultad);
        //Le asignamos los valores a los spinner

        List<String> spinnerArrayTiempo =  new ArrayList<String>();
        List<String> spinnerArrayDificultad =  new ArrayList<String>();
        for (String item : tiempo) {
            spinnerArrayTiempo.add(item);
        }
        for (String item : dificultad) {
            spinnerArrayDificultad.add(item);
        }

        ArrayAdapter<String> adapterT = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArrayTiempo);
        ArrayAdapter<String> adapterD = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArrayDificultad);

        adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerT.setAdapter(adapterT);

        adapterD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerD.setAdapter(adapterD);

        v.findViewById(R.id.ibFiltrarTabOtros).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                procedencia = 2;
                //Vemos si el usuario quiere filtrar por el título de la receta o por el tiempo y duración
                switch (procedencia){
                    case 1:
                        //En caso de que filtre por el título, pasamos estos parámetros al asyncTask
                        new mostrarRecetasFiltro().execute("titulo", buscar.getText().toString().toUpperCase());
                        break;
                    case 2:
                        Log.d("sentencia4", "case 2");
                        //En caso de que filtre por la duración y el tiempo, pasamos estos parámetros al asyncTask
                        new mostrarRecetasFiltro().execute("spinner", spinnerT.getSelectedItem().toString(), spinnerD.getSelectedItem().toString());
                        break;
                }

            }
        });
        return v;
    }

    public class mostrarRecetasFiltro extends AsyncTask<String, Void, ArrayList<Receta>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ustomDialogProgressBar.showDialogCuadrado();
        }

        @Override
        protected ArrayList<Receta> doInBackground(String... voids) {
            Log.d("sentencia3", "sentencia: " + voids[1]);
            recetas = new ArrayList<>();
            myHelper = new MySQLHelper();
            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Vemos si tenemos que hacer la consulta en función del título o de los spinner
                if (voids[0].equals("titulo")) {
                    recetas = myHelper.recogerRecetaTitulo(voids[1]);
                } else if (voids[0].equals("spinner")){
                    Log.d("sentencia4", "else if");
                    recetas = myHelper.recetaPorTiempoDificultad(voids[1], voids[2]);
                }
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
                getActivity().setResult(getActivity().RESULT_OK, intent);
            } else {
                //Si no hay coincidencias, se mostrará un toast al usuario
                Toast.makeText(getContext(), "No hay resultados", Toast.LENGTH_SHORT).show();
            }
            try {
                myHelper.cerrarConexion();

                getActivity().finish();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
        }
    }
}
