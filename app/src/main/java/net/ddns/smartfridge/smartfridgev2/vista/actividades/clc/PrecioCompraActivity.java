package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Precio;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrecioCompraActivity extends AppCompatActivity {
    private Intent intent;//Para recoger los valores del Activity que le llama
    private ArrayList<ComponenteListaCompra> nombres;//Para almacenar lo recogido del intent
    private CustomDialogProgressBar customDialogProgressBar;//Para mostrar un progressBar cuando se ejecuta la consulta a la bbdd
    private MySQLHelper myHelper;//Para acceder a la bbdd
    private ArrayList<Precio> precios;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precio_compra);
        customDialogProgressBar = new CustomDialogProgressBar(this);
        //Recogemos los valores del intent
        intent = getIntent();
        nombres = (ArrayList<ComponenteListaCompra>) intent.getSerializableExtra("productos");
        Log.d("precio", "nombres: " + nombres.size());

        intentNuevo = new Intent(this, PrecioCompraProductosActivity.class);

        new consultarTodosPrecios(this).execute(nombres);
    }

    public ArrayList<Double> calcularTotal(ArrayList<Precio> precios){
        ArrayList<Double> totales = new ArrayList<>();//Variable para almacenar el total de los productos por supermercado
        double carrefour=0;//Para calcular el total en Carrefour
        double alcampo=0;//Para calcular el total en Alcampo
        double hipercor=0;//Para calcular el total en Hipercor
        double mercadona=0;//Para calcular el total en Mercadona
        //Recorremos el array y vamos calculando los valores
        for (int i=0; i<precios.size();i++){
            if(precios.get(i).getSupermercado().equals(SUPERMERCADOS[0])){
                carrefour += precios.get(i).getPvp();
                Log.d("CeroMisHuevos", "calcularTotal: 0" + carrefour);
            } else if (precios.get(i).getSupermercado().equals(SUPERMERCADOS[1])){
                alcampo += precios.get(i).getPvp();
                Log.d("CeroMisHuevos", "calcularTotal: 1" + alcampo);
            } else if (precios.get(i).getSupermercado().equals(SUPERMERCADOS[2])){
                hipercor += precios.get(i).getPvp();
                Log.d("CeroMisHuevos", "calcularTotal: 2" + hipercor);
            } else if (precios.get(i).getSupermercado().equals(SUPERMERCADOS[3])){
                mercadona += precios.get(i).getPvp();
                Log.d("CeroMisHuevos", "calcularTotal: 3" + mercadona);
            }
            //Asignamos los valore a un ArrayList de double
            totales.add(carrefour);
            totales.add(alcampo);
            totales.add(hipercor);
            totales.add(mercadona);

            Log.d("CeroMisHuevos", "calcularTotal: adfasf" + totales.get(2));
        }
        return totales;
    }

    public void setArray(ArrayList<Precio> preciosS){
        this.precios = preciosS;
        imageButtonAlcampo = findViewById(R.id.ibAlcampo);
        imageButtonHipercor = findViewById(R.id.ibHipercor);
        imageButtonCarrefour = findViewById(R.id.ibCarrefour);
        imageButtonMercadona = findViewById(R.id.ibMercadona);

        final ArrayList<Double> totales = calcularTotal(precios);

        imageButtonAlcampo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentNuevo.putExtra("Precios", precios);
                intentNuevo.putExtra("Super", "alcampo");
                intentNuevo.putExtra("Total", totales.get(1));
                startActivity(intentNuevo);
            }
        });

        imageButtonHipercor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentNuevo.putExtra("Precios", precios);
                intentNuevo.putExtra("Super", "hipercor");
                intentNuevo.putExtra("Total", totales.get(2));
                startActivity(intentNuevo);
            }
        });

        imageButtonCarrefour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentNuevo.putExtra("Precios", precios);
                intentNuevo.putExtra("Super", "carrefour");
                intentNuevo.putExtra("Total", totales.get(0));
                startActivity(intentNuevo);
            }
        });

        imageButtonMercadona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentNuevo.putExtra("Precios", precios);
                intentNuevo.putExtra("Super", "mercadona");
                intentNuevo.putExtra("Total", totales.get(3));
                startActivity(intentNuevo);
            }
        });

        tvCarrefour = findViewById(R.id.tvCar);
        tvAlcampo = findViewById(R.id.tvAlc);
        tvHipercor = findViewById(R.id.tvHip);
        tvMercadona = findViewById(R.id.tvMer);

        tvCarrefour.setText(String.valueOf(totales.get(0)));
        tvAlcampo.setText(String.valueOf(totales.get(1)));
        tvHipercor.setText(String.valueOf(totales.get(2)));
        tvMercadona.setText(String.valueOf(totales.get(3)));
    }

    //Creamos la consulta a la bbdd
    public class consultarTodosPrecios extends AsyncTask<ArrayList<ComponenteListaCompra>, Void, ArrayList<Precio>>{
        private final String [] SUPERMERCADOS = {"carrefour", "alcampo", "hipercor", "mercadona"};
        private ArrayList<Precio>  precioPorSuper = new ArrayList<>();//Para almacenar los resultados de la bbdd
        private ArrayList<Double> precioTotalSuper = new ArrayList<>();//Para almacenar el total de precios de cada supermercado
        private ArrayList<Precio> totalPorSupermercado = new ArrayList<>();//Para calcular el total por cada supermercdo
        private Precio precio;//Para generar objetos a partir de los datos de la bbdd
        private PrecioCompraActivity clase;

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
                    /*Calculamos el total por cada supermercado
                    totalPorSupermercado = calcularTotal(precioPorSuper);
                    precioTotalSuper.add(totalPorSupermercado);*/
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
            clase.setArray(precios);
            try {
                //Asignamos aqui el ArrayList al recyclerview
                myHelper.cerrarConexion();
                customDialogProgressBar.endDialog();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
        }

        /*  @Override
        protected void onPostExecute(ArrayList<Double> doubles) {
            super.onPostExecute(doubles);
            precioTotalSuper = doubles;
            try {
                //Asignamos aqui el ArrayList al recyclerview
                myHelper.cerrarConexion();
                customDialogProgressBar.endDialog();
            } catch (SQLException e) {
                Log.d("SQL", "Error al cerrar la bbdd");
            }
        }*/

        //Método para calcular el total de precio en función de cada supermercado
        public ArrayList<Double> calcularTotal(ArrayList<Precio> precios){
            ArrayList<Double> totales = new ArrayList<>();//Variable para almacenar el total de los productos por supermercado
            double carrefour=0;//Para calcular el total en Carrefour
            double alcampo=0;//Para calcular el total en Alcampo
            double hipercor=0;//Para calcular el total en Hipercor
            double mercadona=0;//Para calcular el total en Mercadona
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
                totales.add(carrefour);
                totales.add(alcampo);
                totales.add(hipercor);
                totales.add(mercadona);
            }
            return totales;
        }
    }
}
