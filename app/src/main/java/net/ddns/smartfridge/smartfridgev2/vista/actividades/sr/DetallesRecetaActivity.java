package net.ddns.smartfridge.smartfridgev2.vista.actividades.sr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetallesRecetaActivity extends AppCompatActivity {
    private Intent intent;//Para recoger el intent del otro Activity
    private MySQLHelper myHelper;//Para hacer la búsqueda en la bbdd
    private ArrayList<Ingrediente> ingredientesReceta;//Para almacenar los ingredientes de cada receta
    private String textoIngredientes="";//Para poner en el textview la relación de los ingredientes
    private TextView tvIng;//El textview donde van a ir los ingredientes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_receta);
        intent = getIntent();
        ImageView iv = (ImageView)findViewById(R.id.ivDetalleReceta);
        iv.setImageBitmap((Bitmap)intent.getParcelableExtra("imagen"));
        TextView tvTiempo = (TextView) findViewById(R.id.tvNumeroProductos);
        tvTiempo.setText(intent.getStringExtra("duracion"));
        TextView tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);
        tvDescripcion.setText(intent.getStringExtra("descripcion"));
        ingredientesReceta = new ArrayList<>();
        tvIng = (TextView)findViewById(R.id.tvIngredientes);
        new RecogerIngredientes().execute(intent.getIntExtra("id",0));
    }

    public class RecogerIngredientes extends AsyncTask<Integer, Void, ArrayList<Ingrediente>>{

        @Override
        protected ArrayList<Ingrediente> doInBackground(Integer... integers) {
            myHelper = new MySQLHelper();
            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos los ingredientes de cada receta en función de su id
                ingredientesReceta = myHelper.recogerIngredientesReceta(integers[0]);
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
            }
            for(int i = 0;i<ingredientesReceta.size(); i++){
                Log.d("ingRecetas", "Ingrediente por receta: " + ingredientesReceta.get(i).getNombreIngrediente());
            }
            return ingredientesReceta;
        }

        @Override
        protected void onPostExecute(ArrayList<Ingrediente> ingredientes) {
            super.onPostExecute(ingredientes);
            try {
                myHelper.cerrarConexion();
                //Ponemos todos los ingredientes en el TextView
                for (int i=0; i<ingredientes.size(); i++){
                    textoIngredientes += " - " + ingredientes.get(i).getNombreIngrediente() + "\n";
                }
                tvIng.setText(textoIngredientes);
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
        }
    }
}
