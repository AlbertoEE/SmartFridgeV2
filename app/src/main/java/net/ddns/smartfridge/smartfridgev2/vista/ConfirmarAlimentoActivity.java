package net.ddns.smartfridge.smartfridgev2.vista;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento_Codigo;
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
    public static boolean encontrado = false;//Variable para saber si se ha encontrado el elemento en la bbdd y así mostrar un layout u otro
    private Alimento_Codigo ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        escaner = getIntent();
        cod_barrras = escaner.getStringExtra(EscanerActivity.TAG_CODIGO);
        formato_codigo = escaner.getStringExtra(EscanerActivity.TAG_TIPO_CODIGO);
        //texto_alimento = (TextView)findViewById(R.id.tvNombreProducto_ConfirmarAlimento);
        new Verificador().execute(cod_barrras);
        Toast.makeText(this, "" + encontrado, Toast.LENGTH_SHORT).show();
        if (encontrado) {
            setContentView(R.layout.activity_confirmar_alimento);
            //imagen_alimento = (ImageView)findViewById(R.id.ivProducto_ConfirmarAlimento);
            //texto_alimento = (TextView)findViewById(R.id.tvNombreProducto_ConfirmarAlimento);
            //texto_alimento.setSelected(true);//Para las animaciones de los textos

            //texto_alimento.setText(ac.getNomAlimento());
        } else {

        }
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
            //ac = al;
            /*
            String nombre = al.getNomAlimento();
            //TextView texto_alimento = (TextView)findViewById(R.id.tvNombreProducto_ConfirmarAlimento);
            //Toast.makeText(ConfirmarAlimentoActivity.this, "nombre: " + nombre, Toast.LENGTH_SHORT).show();
            //imagen_alimento.setImageBitmap(al.getImagen());
            texto_alimento.setText(al.getNomAlimento());*/
            String nombre = al.getNomAlimento();
            Toast.makeText(ConfirmarAlimentoActivity.this, "nombre: " + nombre, Toast.LENGTH_SHORT).show();
        }
    }
}
