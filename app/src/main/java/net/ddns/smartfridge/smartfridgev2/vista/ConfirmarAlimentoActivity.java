package net.ddns.smartfridge.smartfridgev2.vista;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento_Codigo;
import net.ddns.smartfridge.smartfridgev2.modelo.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQLHelper;

import org.w3c.dom.Text;

import java.sql.SQLException;

public class ConfirmarAlimentoActivity extends AppCompatActivity {
    private Intent escaner;//Para recoger el Intent del Activity Escaner
    private String cod_barrras = null;//Variable para almacenar el código de barras que recibimos el Escaner
    private String formato_codigo = null;//Para recoger el formato del código leído
    private MySQLHelper myHelper;
    private ImageView imagen_alimento;//ImageView para mostrar la imagen de la bbdd
    private TextView texto_alimento; //TextView para mostrar el nombre de la bbdd
    private Alimento_Codigo ac;
    private Dialogos dialogos;//Para tener acceso a los dialogs que se mostrarán en la app

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        escaner = getIntent();
        cod_barrras = escaner.getStringExtra(EscanerActivity.TAG_CODIGO);
        formato_codigo = escaner.getStringExtra(EscanerActivity.TAG_TIPO_CODIGO);
        new Verificador().execute(cod_barrras);
        dialogos = new Dialogos(this);
    }

    //Creamos el AsyncTask para hacer la consulta a la bbdd
    public class Verificador extends AsyncTask<String,Void, Alimento_Codigo> {

        private Alimento ca = null;//Crearemos un objeto de este tipo para almacenar los datos

        @Override
        protected Alimento_Codigo doInBackground(String... cod_barras) {
            Alimento_Codigo al=null;
            myHelper = new MySQLHelper();
            try {
                myHelper.abrirConexion();
                al = myHelper.consultaCodBarras(cod_barras[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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
        }
    }

    //Programamos el botón de NO
    public void volverIdentificarAlimento (View v){
        dialogos.dialogAlimentoNoEncontrado();
    }

    //Programamos el botón de SI
    public void alimentoIdentificado (View v){
        dialogos.dialogAlimentoEncontrado();
    }
}
