package net.ddns.smartfridge.smartfridgev2.vista.actividades.sr;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class SeleccionarRecetaActivity extends AppCompatActivity {
/*    private ArrayList<Receta> recetas;//Para almacenar todas las recetas de la bbdd
    private MySQLHelper myHelper;//Para acceder a la bbdd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_receta);
        //new SacarRecetas().execute();
    }

    //Creamos el AsyncTask para hacer las consultas a la bbdd
    public class SacarRecetas extends AsyncTask<Void, Void, ArrayList<Receta>> {

        @Override
        protected ArrayList<Receta> doInBackground(Void... voids) {
            recetas = new ArrayList<>();
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
            return recetas;
        }

        @Override
        protected void onPostExecute(ArrayList<Receta> recetas) {
            super.onPostExecute(recetas);
            //Línea donde se añaden las recetas al adapter para mostrarlas
            try {
                myHelper.cerrarConexion();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
        }
    }*/
}
