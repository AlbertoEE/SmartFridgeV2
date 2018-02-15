package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapterRevistaCategorias;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;
import java.util.ArrayList;

public class CategoriaActivity extends AppCompatActivity {
    private Intent i;//Para pasar datos entre Activitys
    private String categoria;//Para hacer la búsqueda en la bbdd
    private MySQLHelper myHelper;//Para acceder a la bbdd
    private ArrayList<Ingrediente> ingredientesCategoria;//Para recoger todos los ingredientes de una categoria
    private CustomDialogProgressBar customDialogProgressBar;//Para mostrar un progressBar cuando se ejecuta la consulta a la bbdd
    private static Ingrediente ingrediente=null;//Para recoger los ingredientes de la bbdd

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CustomRecyclerViewAdapterRevistaCategorias adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        i = getIntent();
        categoria = i.getStringExtra("Categoria");
        customDialogProgressBar = new CustomDialogProgressBar(this);
        ingredientesCategoria = new ArrayList<Ingrediente>();
        //Iniciamos la consulta a la bbdd
        new ListadoExterno().execute(categoria);
    }
    //Programamos el onclick de los botones
    public void agregar(View view){
        //Hacemos el select a la bbdd con el parámetro de la categoría que hemos recibido de la
        //selección que ha hecho el usuario
    }

    //Creamos el AsyncTask para hacer la consulta a la bbdd
    public class ListadoExterno extends AsyncTask<String,Void, ArrayList<Ingrediente>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogProgressBar.showDialogOndas();
        }

        @Override
        protected ArrayList<Ingrediente> doInBackground(String... categoria) {
            myHelper = new MySQLHelper();
            try {
                //Abrimos la conexión a la bbdd
                myHelper.abrirConexion();
                //Recogemos los ingredientes uno a uno
                ingredientesCategoria = myHelper.recogerAlimentoPorCategoria(categoria[0]);
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al establecer la conexión: " + e.getMessage());
            }
            return ingredientesCategoria;
        }

        @Override
        protected void onPostExecute(ArrayList<Ingrediente> ingredientesCategoria) {
            super.onPostExecute(ingredientesCategoria);
            try {
                //Asignamos aqui el ArrayList al recyclerview
                myHelper.cerrarConexion();
                customDialogProgressBar.endDialog();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
            cargarAdapter();
        }
    }

    public ArrayList<Ingrediente> getIngredientesCategoria() {
        return ingredientesCategoria;
    }

    public void setIngredientesCategoria(ArrayList<Ingrediente> ingredientesCategoria) {
        this.ingredientesCategoria = ingredientesCategoria;
    }

    private void cargarAdapter(){
        this.adapter = new CustomRecyclerViewAdapterRevistaCategorias(ingredientesCategoria, this);
        this.layoutManager = new GridLayoutManager(this, 2);
        this.recyclerView = findViewById(R.id.rvCategoria);

        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter);
        this.adapter.notifyDataSetChanged();
    }
}
