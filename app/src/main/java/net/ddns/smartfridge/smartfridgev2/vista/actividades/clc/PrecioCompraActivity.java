package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Precio;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Activity donde se muestra el precio de los ingredientes de la lista según los distintos comercios
 */
public class PrecioCompraActivity extends AppCompatActivity {
    private Intent intent;//Para recoger los valores del Activity que le llama
    private ArrayList<ComponenteListaCompra> nombres;//Para almacenar lo recogido del intent
    private CustomDialogProgressBar customDialogProgressBar;//Para mostrar un progressBar cuando se ejecuta la consulta a la bbdd
    private MySQLHelper myHelper;//Para acceder a la bbdd
    private ArrayList<Precio> precios;
    private ArrayList<Double> totales;//Para almacenar los totales de todos los supermercados
    //ArrayList con el nombre de los 4 comercios consultados
    private final String [] SUPERMERCADOS = {"carrefour", "alcampo", "hipercor", "mercadona"};
    private ImageButton imageButtonAlcampo;
    private ImageButton imageButtonHipercor;
    private ImageButton imageButtonCarrefour;
    private ImageButton imageButtonMercadona;
    private TextView tvCarrefour;
    private TextView tvAlcampo;
    private TextView tvHipercor;
    private TextView tvMercadona;
    private Intent intentNuevo;
    private static final String FORMATO = "##.00";//Para el formato a la hora de visualizar los precios

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precio_compra);
        customDialogProgressBar = new CustomDialogProgressBar(this);
        totales = new ArrayList<>();
        //Recogemos los valores del intent
        intent = getIntent();
        nombres = (ArrayList<ComponenteListaCompra>) intent.getSerializableExtra(getString(R.string.productos));
        Log.d("precio", "nombres: " + nombres.size());
        intentNuevo = new Intent(this, PrecioCompraProductosActivity.class);
        new consultarTodosPrecios(this).execute(nombres);
        Toast.makeText(this, getString(R.string.mostrar_precio), Toast.LENGTH_SHORT).show();
    }

    /**
     * Método para calcular el total del precio de todos los ingredientes
     *
     * @param precios Lista con todos los precios
     * @return Lista con todos los precios totales
     */
    public ArrayList<Double> calcularTotal(ArrayList<Precio> precios){
        double carrefour=0f;//Para calcular el total en Carrefour
        double alcampo=0f;//Para calcular el total en Alcampo
        double hipercor=0f;//Para calcular el total en Hipercor
        double mercadona=0f;//Para calcular el total en Mercadona
        //Recorremos el array y vamos calculando los valores
        for (int i=0; i<precios.size();i++){
            if(precios.get(i).getSupermercado().equals(SUPERMERCADOS[0])){
                carrefour += precios.get(i).getPvp();
            } else if (precios.get(i).getSupermercado().equals(SUPERMERCADOS[1])){
                alcampo += precios.get(i).getPvp();
            } else if (precios.get(i).getSupermercado().equals(SUPERMERCADOS[2])){
                hipercor += precios.get(i).getPvp();
            } else if (precios.get(i).getSupermercado().equals(SUPERMERCADOS[3])){
                mercadona += precios.get(i).getPvp();
            }
            //Asignamos los valore a un ArrayList de double
            totales.clear();
            totales.add(carrefour);
            totales.add(alcampo);
            totales.add(hipercor);
            totales.add(mercadona);
            for (int j=0; j<totales.size(); j++){
                Log.d("TotalArray", "TotalArray: " + totales.get(j).doubleValue());
            }
        }
        return totales;
    }

    /**
     * Método para asignar a cada botón los precios correspondientes a ese supermercado
     *
     * @param preciosS Lista con todos los precios de los alimentos
     */
    public void setArray(ArrayList<Precio> preciosS){
        this.precios = preciosS;
        imageButtonAlcampo = findViewById(R.id.ibAlcampo);
        imageButtonHipercor = findViewById(R.id.ibHipercor);
        imageButtonCarrefour = findViewById(R.id.ibCarrefour);
        imageButtonMercadona = findViewById(R.id.ibMercadona);

        totales = calcularTotal(precios);

        imageButtonAlcampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentNuevo.putExtra(getString(R.string.precios), precios);
                intentNuevo.putExtra(getString(R.string.superm), SUPERMERCADOS[1]);
                intentNuevo.putExtra(getString(R.string.total), totales.get(1));
                startActivity(intentNuevo);
            }
        });

        imageButtonHipercor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentNuevo.putExtra(getString(R.string.precios), precios);
                intentNuevo.putExtra(getString(R.string.superm), SUPERMERCADOS[2]);
                intentNuevo.putExtra(getString(R.string.total), totales.get(2));
                startActivity(intentNuevo);
            }
        });

        imageButtonCarrefour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentNuevo.putExtra(getString(R.string.precios), precios);
                intentNuevo.putExtra(getString(R.string.superm), SUPERMERCADOS[0]);
                intentNuevo.putExtra(getString(R.string.total), totales.get(0));
                startActivity(intentNuevo);
            }
        });

        imageButtonMercadona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentNuevo.putExtra(getString(R.string.precios), precios);
                intentNuevo.putExtra(getString(R.string.superm), SUPERMERCADOS[3]);
                intentNuevo.putExtra(getString(R.string.total), totales.get(3));
                startActivity(intentNuevo);
            }
        });

        DecimalFormat f = new DecimalFormat(FORMATO);

        tvCarrefour = findViewById(R.id.tvCar);
        tvAlcampo = findViewById(R.id.tvAlc);
        tvHipercor = findViewById(R.id.tvHip);
        tvMercadona = findViewById(R.id.tvMer);
        if (totales!=null) {
            tvCarrefour.setText(f.format(totales.get(0)));
            tvAlcampo.setText(f.format(totales.get(1)));
            tvHipercor.setText(f.format(totales.get(2)));
            tvMercadona.setText(f.format(totales.get(3)));
        } else {
            Toast.makeText(this, getString(R.string.no_precio), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clase con el AsyncTask para consultar los precios a partir de los ingredientes de la bbdd
     */
    public class consultarTodosPrecios extends AsyncTask<ArrayList<ComponenteListaCompra>, Void, ArrayList<Precio>>{
        private final String [] SUPERMERCADOS = {"carrefour", "alcampo", "hipercor", "mercadona"};
        private ArrayList<Precio>  precioPorSuper = new ArrayList<>();//Para almacenar los resultados de la bbdd
        private ArrayList<Double> precioTotalSuper = new ArrayList<>();//Para almacenar el total de precios de cada supermercado
        private ArrayList<Precio> totalPorSupermercado = new ArrayList<>();//Para calcular el total por cada supermercdo
        private Precio precio;//Para generar objetos a partir de los datos de la bbdd
        private PrecioCompraActivity clase;

        /**
         * Constructor
         *
         * @param clase Activity sobre el que se van a mostrar los datos
         */
        public consultarTodosPrecios(PrecioCompraActivity clase){
            this.clase = clase;
        }

        //Mostramos el progressBar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogProgressBar.showDialogOndas();
        }
        //Ejecutamos la consulta a la bbdd
        @Override
        protected ArrayList<Precio> doInBackground(ArrayList<ComponenteListaCompra>[] arrayLists) {
            myHelper = new MySQLHelper();
            //Abrimos la conexión a la bbdd
            try {
                myHelper.abrirConexion();
                for(int i=0; i<SUPERMERCADOS.length; i++) {
                    //Recogemos los precios de todos los elementos en un array
                    precioPorSuper = myHelper.recogerPrecio(arrayLists[0], SUPERMERCADOS[i]);
                    for(int j=0; j<precioPorSuper.size(); j++){
                        totalPorSupermercado.add(precioPorSuper.get(j));
                    }
                    Log.d("precio", "total de todos los supermercados: " + totalPorSupermercado.size());
                }
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getMessage());
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            }
            return totalPorSupermercado;
        }

        @Override
        protected void onPostExecute(ArrayList<Precio> precios) {
            super.onPostExecute(precios);
            totalPorSupermercado = precios;
            if (precios.size()>0) {
                //Asignamos aqui el ArrayList al recyclerview
                clase.setArray(precios);
            } else {
                Toast.makeText(clase, getString(R.string.no_precio_r), Toast.LENGTH_SHORT).show();
                try {
                    myHelper.cerrarConexion();
                    customDialogProgressBar.endDialog();
                    finish();
                } catch (SQLException e) {
                    Log.d("SQL", "Error al cerrar la bbdd");
                }
            }
            try {
                myHelper.cerrarConexion();
                customDialogProgressBar.endDialog();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
        }
    }
}
