package net.ddns.smartfridge.smartfridgev2.vista;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.Alimento;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQLHelper;

import java.sql.SQLException;

public class ConfirmarAlimentoActivity extends AppCompatActivity {
    private Intent escaner;//Para recoger el Intent del Activity Escaner
    private String cod_barrras = null;//Variable para almacenar el código de barras que recibimos el Escaner
    private String formato_codigo = null;//Para recoger el formato del código leído
    private MySQLHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        escaner = getIntent();
        cod_barrras = escaner.getStringExtra(EscanerActivity.TAG_CODIGO);
        formato_codigo = escaner.getStringExtra(EscanerActivity.TAG_TIPO_CODIGO);
        myHelper = new MySQLHelper();
        try {
            myHelper.abrirConexion();
            Log.d("Suerte", "conexion abierta");
            myHelper.consultaCodBarras("9632147856984");
            myHelper.cerrarConexion();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_confirmar_alimento);
      /*  Log.d("prueba", cod_barrras);
        Log.d("prueba", formato_codigo);*/
    }


    //Creamos el AsyncTask para hacer la consulta a la bbdd
    public class Verificador extends AsyncTask<String,Void, Alimento> {

        private Alimento ca = null;//Crearemos un objeto de este tipo para almacenar los datos
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myHelper = new MySQLHelper();
            try {
                myHelper.abrirConexion();
            } catch (ClassNotFoundException e) {
                Toast.makeText(ConfirmarAlimentoActivity.this, "Ha ocurrido un error al acceder a la bbdd.", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(ConfirmarAlimentoActivity.this, "Ha ocurrido un error al acceder a la bbdd. El error es: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Alimento doInBackground(String... cod_barras) {

            return null;
        }

    }
}
