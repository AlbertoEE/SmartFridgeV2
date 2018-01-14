package net.ddns.smartfridge.smartfridgev2.vista;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento_Codigo;
import net.ddns.smartfridge.smartfridgev2.modelo.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.modelo.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQLHelper;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

public class ConfirmarAlimentoActivity extends AppCompatActivity {
    private Intent escaner;//Para recoger el Intent del Activity Escaner
    private String cod_barrras = null;//Variable para almacenar el código de barras que recibimos el Escaner
    private String formato_codigo = null;//Para recoger el formato del código leído
    private MySQLHelper myHelper;
    private static ImageView imagen_alimento;//ImageView para mostrar la imagen de la bbdd
    private static TextView texto_alimento; //TextView para mostrar el nombre de la bbdd
    private Dialogos dialogos;//Para tener acceso a los dialogs que se mostrarán en la app
    private static Alimento_Codigo al=null;//Para el objeto de tipo Alimento
    private CustomDialogProgressBar customDialogProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        escaner = getIntent();
        cod_barrras = escaner.getStringExtra(EscanerActivity.TAG_CODIGO);
        formato_codigo = escaner.getStringExtra(EscanerActivity.TAG_TIPO_CODIGO);
        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Log.d("NOFUNCIONA", "onCreate: " + progressBar);
        customDialogProgressBar = new CustomDialogProgressBar(this);
        new Verificador().execute(cod_barrras);
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
                setContentView(R.layout.activity_producto_no_encontrado);
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
}
