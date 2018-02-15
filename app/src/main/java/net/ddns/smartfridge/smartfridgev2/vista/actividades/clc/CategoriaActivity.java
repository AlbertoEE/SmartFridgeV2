package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;

public class CategoriaActivity extends AppCompatActivity {
    private Intent i;//Para pasar datos entre Activitys
    private String categoria;//Para hacer la búsqueda en la bbdd
    private MySQLHelper myHelper;//Para acceder a la bbdd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        i = getIntent();
        categoria = i.getStringExtra("Categoria");
        myHelper = new MySQLHelper();
        try {
            myHelper.abrirConexion();
        } catch (ClassNotFoundException e) {
            Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
        } catch (SQLException e) {
            Log.d("SQL", "Error al establecer la conexión: " + e.getErrorCode());
        }
    }
    //Programamos el onclick de los botones
    public void agregar(View view){
        //Hacemos el select a la bbdd con el parámetro de la categoría que hemos recibido de la
        //selección que ha hecho el usuario
    }

    //Cerramos la conexión a la bbdd en el onDestroy

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            myHelper.cerrarConexion();
        } catch (SQLException e) {
            Log.d("SQL", "Error al cerrar la conexión: " + e.getErrorCode());
        }
    }
}
