package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.persistencia.MySQL.MySQLHelper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrecioCompraActivity extends AppCompatActivity {
    private Intent intent;//Para recoger los valores del Activity que le llama
    private ArrayList<ComponenteListaCompra> nombres;//Para almacenar lo recogido del intent
    private CustomDialogProgressBar customDialogProgressBar;//Para mostrar un progressBar cuando se ejecuta la consulta a la bbdd
    private MySQLHelper myHelper;//Para acceder a la bbdd

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precio_compra);
        customDialogProgressBar = new CustomDialogProgressBar(this);
        //Recogemos los valores del intent
        intent = getIntent();
        nombres = (ArrayList<ComponenteListaCompra>) intent.getSerializableExtra("productos");
        Log.d("precio", "nombres: " + nombres.size());
    }

    //Creamos la consulta a la bbdd
    public class consultarTodosPrecios extends AsyncTask<ArrayList<ComponenteListaCompra>, Void, ArrayList<Double>>{
        private final String [] SUPERMERCADOS = {"carrefour", "alcampo", "hipercor", "mercadona"};
        private ArrayList<BigDecimal>  precioPorSuper = new ArrayList<>();//Para almacenar los resultados de la bbdd
        private ArrayList<Double> precioTotalSuper = new ArrayList<>();//Para almacenar el total de precios de cada supermercado
        private double totalPorSupermercado;//Para calcular el total por cada supermercdo
        //Mostramos el progressBar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogProgressBar.showDialogOndas();
        }
        //Ejecutamos la consulta a la bbdd
        @Override
        protected ArrayList<Double> doInBackground(ArrayList<ComponenteListaCompra>[] arrayLists) {
            myHelper = new MySQLHelper();
            //Abrimos la conexión a la bbdd
            try {
                myHelper.abrirConexion();
                //Recogemos los precios de todos los elementos en un array
                precioPorSuper = myHelper.recogerPrecio(arrayLists[0], SUPERMERCADOS[0]);
                //Calculamos el total por cada supermercado
            } catch (ClassNotFoundException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getMessage());
            } catch (SQLException e) {
                Log.d("SQL", "Error al conectarse a la bbdd: " + e.getErrorCode());
            }
            return null;
        }

        public double calcularTotal(ArrayList<BigDecimal> aBig){
            double p;//Para convertir cada elemento de BigDecimal a double
            totalPorSupermercado=0;//Para calcular el total
            //Recorremos el array y vamos calculando los valores
            for (int i=0; i<aBig.size();i++){
                p = aBig.get(i).doubleValue();
            }



            return totalPorSupermercado;
        }
    }
}
