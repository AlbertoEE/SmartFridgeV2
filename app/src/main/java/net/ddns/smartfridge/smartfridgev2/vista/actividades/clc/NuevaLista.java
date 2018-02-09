package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Fecha;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.DialogActivity;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainCa;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class NuevaLista extends AppCompatActivity {
    private Intent intent;//Para trabajar con los intents para lanzar nuevos activitys
    private String alimentoNuevo;//Para recoger el dato introducido por el usuario en el dialog
    private Context context;//Para indicar el contexto del activity
    private ArrayList<String> listaAlimentosCompra;//ArrayList que lleva el nombre de los alimentos que se van a mostrar en la lista
    private ListaCompra listaNueva;//Para trabajar con el objeto ListaCompra
    private Fecha fecha;//Para usar los métodos para obtener la fecha de hoy
    //private String[] listaDatos;//Para almacenar los datos leidos del SP
    private ArrayList<String> alimentosLeidosSP;//Para leer los aliemntos que hay en el SP almacenados
    private int elementos;//Para contar el número de elementos que hay en el SP
    private boolean hayElemento=false;//Variable para comprobar si hay elementos almacenados en el SP. Se inicializa a false
    private Map<String, ?> totalElementos = null;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_lista);
        //Creamos el nuevo objeto Lista
        listaNueva = new ListaCompra();
        fecha = new Fecha();
        //SharedPreferences mysp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        /*listaNueva.setFecha(fecha.fechaActual());
        Log.d("fecha", "" + fecha.fechaActual());*/
        //Fijamos el contexto del activity
        context = this;
        //Instanciamos el arraylist
        listaAlimentosCompra = new ArrayList<String>();
        alimentosLeidosSP = new ArrayList<String>();
        //Cogemos la referencia a los floating action buttons
        com.getbase.floatingactionbutton.FloatingActionButton botonManual = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.manual);
        com.getbase.floatingactionbutton.FloatingActionButton botonAlimentos = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.anadirAlimentos);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        //Le asignamos el listener a cada botón
        botonManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Cuando pulsemos el botón, nos da la opción de añadir alimentos de manera manual
                    //Mensaje del Alert
                    builder.setMessage("Introduzca el elemento que quiere añadir a la lista:");
                    //Título
                    builder.setTitle("Añadir manualmente");
                    //Añadimos el layout que hemos creado
                    //builder.setView(inflater.inflate(R.layout.dialognewfood, null));
                    final EditText input = new EditText(context);
                    builder.setView(input);
                    //Añadimos los botones
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Asignamos el valor introducido a la variable
                            alimentoNuevo =  input.getText().toString();
                            listaAlimentosCompra.add(alimentoNuevo);
                            Log.d("alimento", alimentoNuevo);
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //No hacemos nada
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        botonAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cuando pulsemos el botón, se va a abrir el activity con todos los alimentos
                /*intent = new Intent(this, ListadoAlimentos.class);
                startActivity(intent);
                 */
            }
        });
        //Comprobamos si hay algún alimento almacenado en el SP para notificárselo al usuario
        elementos = productosAlmacenados();
        if(hayElemento){
            //Mostramos la lista indicándo que hay elementos y cuáles quiere añadir a la lista
            alimentosLeidosSP = recogerValores(elementos);
            /*intent = new Intent(this, Lista.class);
            startActivity(intent);*/
        }
    }

    //Método para comprobar si hay algún elemento en el SP
    public  int productosAlmacenados(){
        elementos = 0;
        //Comprobamos si hay algún elemento leyendo el SP
        try{
            totalElementos = DialogActivity.getMySp().getAll();
            for (Map.Entry<String, ?> entry : totalElementos.entrySet()) {
                //alimentosLeidosSP.add((String)entry.getValue());
                Log.d("sp", entry.getKey() + ": " + entry.getValue().toString());
                elementos++;
            }
        } catch (NullPointerException e){
            hayElemento = false;
        }
        Log.d("sp", "numero de elementos en la lista de la compra: " + elementos);
        if (elementos>0){
            hayElemento=true;
        }
        return elementos;
    }
    //Método para recoger los valores del SP
    public ArrayList<String> recogerValores(int elem){
        String valor;//Para almacenar el valor recogido del SP
        for (int i=1; i<=elem;i++){
            valor = DialogActivity.getMySp().getString(String.valueOf(i),"oooooo");
            alimentosLeidosSP.add(valor);
            Log.d("sp", "alimento leido: " + valor);
        }

        return alimentosLeidosSP;
    }

    }

