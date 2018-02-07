package net.ddns.smartfridge.smartfridgev2.vista.actividades;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;

import java.io.File;
import java.util.Map;

public class DialogActivity extends Activity {
    private Intent i;//Para recoger el intent que nos llega de la notificación del service
    //private Map<Integer, String> datosSP;//Para guardar los datos recibidos del intent
    private int posicion;//Para poner la posición en el SP


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //Recogemos el intent
        i = getIntent();
        String nombreAlimento = i.getStringExtra("Alimento");
        Log.d("sp", "alimento: "+ nombreAlimento);
        //Comprobamos el número de elementos que hay en el SP
        posicion = recogerPosicion();
        Log.d("sp", "posicion: "+ posicion);
        //Añadimos el elemento en la posición siguiente
        posicion++;
        Log.d("sp", "posicion: "+ posicion);
        //Añadimos el elemento
        guardarSP(posicion, nombreAlimento);
        Dialogos d = new Dialogos(this, this);
        d.dialogListaCompra();
    }

    //Método para guardar lo recibido en el intent en el SharedPreferences
    public void guardarSP(int posicion, String nombreAlimento){
        //Cogemos el SP.
        SharedPreferences mySp = getPreferences(MODE_PRIVATE);
        //Escribimos los datos. Generamos el editor
        SharedPreferences.Editor editor = mySp.edit();
        //Metemos cada par clave/valor
        editor.putString(String.valueOf(posicion), nombreAlimento);
        editor.commit();
    }

    //Método para obtener la posición dónde se almacenará el siguiente elemento del sP
    public int recogerPosicion(){
        int elementos = 0;//Iniciamos la variable con el total de elementos que hay en el SP a 0
        File prefsdir = new File(getApplicationInfo().dataDir,"shared_prefs");
        //Comprobamos que haya un SP y que sea un directorio
        if(prefsdir.exists() && prefsdir.isDirectory()){
            //Guardamos en una lista los datos del sp
            String[] listaDatos = prefsdir.list();
            elementos = listaDatos.length;
            Log.d("sp", "elementos: " + elementos);
        }
        return elementos;
    }
}
