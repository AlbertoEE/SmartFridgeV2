package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento_Codigo;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQLHelper;

import java.sql.SQLException;

public class ConfirmarAlimentoActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        escaner = getIntent();
        //TextView t = (TextView)findViewById(R.id.tvCloud) ;
        ProgressBar progressBar = findViewById(R.id.spin_kit);
        //Log.d("NOFUNCIONA", "onCreate: " + progressBar);
        customDialogProgressBar = new CustomDialogProgressBar(this);
        dialogos = new Dialogos(this, this);
        //Comprobamos el activity desde el que viene
        if(escaner.getStringExtra("ClasePadre").equals("IdentificarAlimentoActivity")){
            setContentView(R.layout.activity_confirmar_alimento);
            imagen_alimento = (ImageView)findViewById(R.id.ivProducto_ConfirmarAlimento);
            texto_alimento = (TextView)findViewById(R.id.tvCloud);
            imagenCloud = (Bitmap) escaner.getExtras().get("imagenCloud");
            nombreCloud = escaner.getStringExtra("nombreCloud");
            texto_alimento.setText(nombreCloud);
            imagen_alimento.setImageBitmap(imagenCloud);
        } else if(escaner.getStringExtra("ClasePadre").equals("EscanerActivity")){
            String s = escaner.getStringExtra("string");
            cod_barrras = escaner.getStringExtra(EscanerActivity.TAG_CODIGO);
            formato_codigo = escaner.getStringExtra(EscanerActivity.TAG_TIPO_CODIGO);
            new Verificador().execute(cod_barrras);
        }
        dialogos = new Dialogos(this, this);
    }

    //Creamos el AsyncTask para hacer la consulta a la bbdd
    public class Verificador extends AsyncTask<String,Void, Alimento_Codigo> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogProgressBar.showDialog();
        }

        @Override
        protected Alimento_Codigo doInBackground(String... cod_barras) {
            //Alimento_Codigo al;
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
                Thread.sleep(3000);
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
                Intent intent = new Intent(getApplicationContext(), IdentificarAlimentoActivity.class);
                intent.putExtra("CODIGO_BARRAS", cod_barrras);
                startActivity(intent);
                dialogos.dialogNoCodBarras();
                finishAffinity();
            }
            //Toast.makeText(getApplicationContext(), "nombre" + al.getNomAlimento(), Toast.LENGTH_LONG).show();
            try {
                myHelper.cerrarConexion();
                customDialogProgressBar.endDialog();
            } catch (SQLException e) {
                Toast.makeText(ConfirmarAlimentoActivity.this, "Error con la bbdd", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Programamos el botón de NO
    public void volverIdentificarAlimento (View v){
        //Toast.makeText(this, "nombre: " + al.getNomAlimento(), Toast.LENGTH_SHORT).show();
        dialogos.dialogAlimentoNoEncontrado();
    }

    //Programamos el botón de SI
    public void alimentoIdentificado (View v){
        dialogos.dialogAlimentoEncontrado();
    }


    public static Alimento_Codigo getAlimento() {
        return al;
    }

    public static Bitmap getImagenCloud() {
        return imagenCloud;
    }

    public static String getNombreCloud() {
        return nombreCloud;
    }
}
