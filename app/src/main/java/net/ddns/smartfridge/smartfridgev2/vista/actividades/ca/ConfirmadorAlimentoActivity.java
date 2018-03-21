package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento_Codigo;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;

/**
 * Activity donde se van a confirmar que los datos obtenidos de las consultas del código de barras o de Google Cloud son los esperados
 */
public class ConfirmadorAlimentoActivity extends AppCompatActivity {
    private Intent escaner;//Para recoger el Intent
    private String cod_barrras = null;//Variable para almacenar el código de barras que recibimos el Escaner
    private String formato_codigo = null;//Para recoger el formato del código leído
    private MySQLHelper myHelper;
    private static ImageView imagen_alimento;//ImageView para mostrar la imagen de la bbdd
    private static TextView texto_alimento; //TextView para mostrar el nombre de la bbdd
    //Para tener acceso a los dialogs que se mostrarán en la app
    private static Alimento_Codigo al=null;//Para el objeto de tipo Alimento
    private CustomDialogProgressBar customDialogProgressBar;
    private static Bitmap imagenCloud;//Para recuperar la imagen del intent cuando viene del Cloud Vision
    private static String nombreCloud;//Para poner el nombre del alimento identificado
    private Dialogos dialogos;
    private static final int SLEEP =1000;//Tiempo en miliseguntos para el sleep del AyncTask


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        escaner = getIntent();
        ProgressBar progressBar = findViewById(R.id.spin_kit);
        customDialogProgressBar = new CustomDialogProgressBar(this);
        dialogos = new Dialogos(this, this);
        //Comprobamos el activity desde el que viene
        if(escaner.getStringExtra(getString(R.string.clasePadre)).equals(getString(R.string.identificador))){
            setContentView(R.layout.activity_confirmar_alimento);
            imagen_alimento = (ImageView)findViewById(R.id.ivProducto_ConfirmarAlimento);
            texto_alimento = (TextView)findViewById(R.id.tvCloud);
            imagenCloud = (Bitmap) escaner.getExtras().get(getString(R.string.img_Cl));
            nombreCloud = escaner.getStringExtra(getString(R.string.nom_Cl));
            texto_alimento.setText(nombreCloud);
            imagen_alimento.setImageBitmap(imagenCloud);
        } else if(escaner.getStringExtra(getString(R.string.clasePadre)).equals(getString(R.string.escaner))){
            String s = escaner.getStringExtra(getString(R.string.string));
            cod_barrras = escaner.getStringExtra(EscanerActivity.TAG_CODIGO);
            formato_codigo = escaner.getStringExtra(EscanerActivity.TAG_TIPO_CODIGO);
            new Verificador().execute(cod_barrras);
        }
        dialogos = new Dialogos(this, this);
    }

    /**
     * Clase para hacer la consulta a la bbdd y ver si los datos obtenidos son los esperados
     */
    public class Verificador extends AsyncTask<String,Void, Alimento_Codigo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogProgressBar.showDialogOndas();
        }

        @Override
        protected Alimento_Codigo doInBackground(String... cod_barras) {
            myHelper = new MySQLHelper();
            try {
                myHelper.abrirConexion();
                al = myHelper.consultaCodBarras(cod_barras[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return al;
        }

        @Override
        protected void onPostExecute(Alimento_Codigo al) {
            super.onPostExecute(al);
            if (al!=null) {
                setContentView(R.layout.activity_confirmar_alimento);
                imagen_alimento = (ImageView)findViewById(R.id.ivProducto_ConfirmarAlimento);
                texto_alimento = (TextView)findViewById(R.id.tvNombreProducto_ConfirmarAlimento);
                imagen_alimento.setImageBitmap(al.getImagen());
                texto_alimento.setText(al.getNomAlimento());
                texto_alimento.setSelected(true);//Para las animaciones de los textos
            } else {
                Intent intent = new Intent(getApplicationContext(), IdentificadorAlimentoActivity.class);
                intent.putExtra(getString(R.string.cod_barras), cod_barrras);
                intent.putExtra(getString(R.string.clasePadre), getString(R.string.confirmador));
                startActivity(intent);
                Log.d("cod", "codigo 1: " + cod_barrras);
                finishAffinity();
            }
            try {
                myHelper.cerrarConexion();
                customDialogProgressBar.endDialog();
            } catch (SQLException e) {
                Toast.makeText(ConfirmadorAlimentoActivity.this, "Error con la bbdd", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Método que muestra un dialog si el usuario selecciona que el alimento mostrado no es el esperado
     *
     * @param v Vista para programar el onClick del boton de NO
     */
    public void volverIdentificarAlimento (View v){
        dialogos.dialogAlimentoNoEncontrado();
    }

    /**
     * Método que muestra un dialog si el usuario selecciona que el alimento mostrado es el esperado
     *
     * @param v Vista para programar el onClick del boton de SI
     */
    public void alimentoIdentificado (View v){
        dialogos.dialogAlimentoEncontrado();
    }


    /**
     * Gets alimento.
     *
     * @return the alimento
     */
    public static Alimento_Codigo getAlimento() {
        return al;
    }
    public static void setAlimento(Alimento_Codigo all){
        al = all;
    }

    /**
     * Gets imagen cloud.
     *
     * @return the imagen cloud
     */
    public static Bitmap getImagenCloud() {
        return imagenCloud;
    }

    /**
     * Gets nombre cloud.
     *
     * @return the nombre cloud
     */
    public static String getNombreCloud() {
        return nombreCloud;
    }
}
