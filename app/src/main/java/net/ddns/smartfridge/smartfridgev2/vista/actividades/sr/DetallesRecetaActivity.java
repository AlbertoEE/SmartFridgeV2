package net.ddns.smartfridge.smartfridgev2.vista.actividades.sr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapzen.speakerbox.Speakerbox;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.clc.PrecioCompraActivity;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class DetallesRecetaActivity extends AppCompatActivity {
    private Intent intent;//Para recoger el intent del otro Activity
    private MySQLHelper myHelper;//Para hacer la búsqueda en la bbdd
    private ArrayList<Ingrediente> ingredientesReceta;//Para almacenar los ingredientes de cada menu_receta
    private String textoIngredientes="";//Para poner en el textview la relación de los ingredientes
    private TextView tvIng;//El textview donde van a ir los ingredientes
    private Intent compartir;//Creamos el intent para compartir la lista por mail
    private Speakerbox speakerbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_receta);
        intent = getIntent();
        TextView tvTitulo = (TextView)findViewById(R.id.tvTituloReceta);
        //titulo =
        tvTitulo.setText(intent.getStringExtra("nombre"));
        TextView tvDificultad = (TextView)findViewById(R.id.tvNumeroProductos);
        tvDificultad.setText(intent.getStringExtra("dificultad"));
        ImageView iv = (ImageView)findViewById(R.id.ivDetalleReceta);
        iv.setImageBitmap((Bitmap)intent.getParcelableExtra("imagen"));
        TextView tvTiempo = (TextView) findViewById(R.id.tvDescTiempo);
        tvTiempo.setText(" - " + intent.getStringExtra("duracion"));
        TextView tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);
        tvDescripcion.setText(intent.getStringExtra("descripcion"));
        ingredientesReceta = new ArrayList<>();
        tvIng = (TextView)findViewById(R.id.tvIngredientes);
        new RecogerIngredientes().execute(intent.getIntExtra("id",0));
        speakerbox = new Speakerbox(getApplication());
    }

    public class RecogerIngredientes extends AsyncTask<Integer, Void, ArrayList<Ingrediente>>{

        @Override
        protected ArrayList<Ingrediente> doInBackground(Integer... integers) {
            myHelper = new MySQLHelper();
            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos los ingredientes de cada menu_receta en función de su id
                ingredientesReceta = myHelper.recogerIngredientesReceta(integers[0]);
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
            }
            for(int i = 0;i<ingredientesReceta.size(); i++){
                Log.d("ingRecetas", "Ingrediente por menu_receta: " + ingredientesReceta.get(i).getNombreIngrediente());
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
    /**
     * @see 'https://developer.android.com/reference/android/app/Activity.html?hl=es-419#onCreateOptionsMenu(android.view.Menu)'
     */
    //Para crear el ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receta, menu);
        return true;
    }

    /**
     * @see 'https://developer.android.com/reference/android/app/Activity.html?hl=es-419#onOptionsItemSelected(android.view.MenuItem)'
     */
    //Programamos el botón del ActionBAr
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Programamos el botón de compartir
            case R.id.miShare:
                //Creamos el intent
                compartir = new Intent();
                compartir.setAction(Intent.ACTION_SEND);
                compartir.putExtra(Intent.EXTRA_EMAIL, new String[]{"raquel.menciac@gmail.com", "smartfridgespain@gmail.com"});
                compartir.putExtra(Intent.EXTRA_SUBJECT, "Receta: " + intent.getStringExtra("nombre"));
                compartir.putExtra(Intent.EXTRA_TEXT, "Hola,\nTe envío la receta de " + intent.getStringExtra("nombre") +
                        "\n - Duración: " + intent.getStringExtra("duracion") + "\n - Dificultad: " + intent.getStringExtra("dificultad") +
                        "\n - Descripción: " + intent.getStringExtra("descripcion") + "\n Espero que la disfrutes.");
                compartir.setType("*/*");
                startActivity(Intent.createChooser(compartir, "Enviar menu_receta por Email"));
                Toast.makeText(this, "Preparando el envío.", Toast.LENGTH_SHORT).show();
                Log.d("compartir", "Enviada lista");
                if (compartir.resolveActivity(getPackageManager()) != null) {
                    //Para que elijamos cómo compartir, con qué app
                    startActivity(Intent.createChooser(compartir, "Seleccione aplicación"));
                } else {
                    Toast.makeText(this, "No hay ninguna aplicación disponible para enviar la lista.", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.miTextSpeech:
                //Programamos el botón para que lea en voz alta el texto
                speakerbox = new Speakerbox(getApplication());
                speakerbox.play(intent.getStringExtra("descripcion"));
                return true;
            case R.id.miPause:
                //Programamos el botón para que pause la lectura
                speakerbox.stop();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
