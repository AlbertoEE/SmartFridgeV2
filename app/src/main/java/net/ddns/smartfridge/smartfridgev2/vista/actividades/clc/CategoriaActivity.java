package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaActivity extends AppCompatActivity {
    private Intent i;//Para pasar datos entre Activitys
    private String categoria;//Para hacer la búsqueda en la bbdd
    private MySQLHelper myHelper;//Para acceder a la bbdd
    private ArrayList<Ingrediente> ingredientesCategoria;//Para recoger todos los ingredientes de una categoria

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        i = getIntent();
        categoria = i.getStringExtra("Categoria");
        ingredientesCategoria = new ArrayList<Ingrediente>();
        myHelper = new MySQLHelper();
        /*try {
            myHelper.abrirConexion();
        } catch (ClassNotFoundException e) {
            Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
        } catch (SQLException e) {
            Log.d("SQL", "Error al establecer la conexión: " + e.getErrorCode());
            Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
        }*/
    }
    //Programamos el onclick de los botones
    public void agregar(View view){
        //Hacemos el select a la bbdd con el parámetro de la categoría que hemos recibido de la
        //selección que ha hecho el usuario
        try {
            myHelper.abrirConexion();
            ingredientesCategoria = myHelper.recogerAlimentoPorCategoria(categoria);
            for(Ingrediente i : ingredientesCategoria){
                Log.d("externa", "ingredientes: " + i.getNombreIngrediente());
            }
            myHelper.cerrarConexion();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Cerramos la conexión a la bbdd en el onDestroy

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*try {
            myHelper.cerrarConexion();
        } catch (SQLException e) {
            Log.d("SQL", "Error al cerrar la conexión: " + e.getErrorCode());
        }*/
    }
}
