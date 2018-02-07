package net.ddns.smartfridge.smartfridgev2.vista.actividades;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;

import java.util.Map;

public class DialogActivity extends Activity {
    private Intent i;//Para recoger el intent que nos llega de la notificación del service
    private Map<Integer, String> datosSP;//Para guardar los datos recibidos del intent
    private int posicion;//Para poner la posición en el SP


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //Recogemos el intent
        i = getIntent();
        Dialogos d = new Dialogos(this, this);
        d.dialogListaCompra();
    }

    //Método para guardar lo recibido en el intent en el SharedPreferences
    public void guardarSP(Map<Integer, String> datos){

    }

    //Método para obtener la posición dónde se almacenará el siguiente elemento del sP
    public int recogerPosicion(){
        int elementos = 0;//Iniciamos la variable con el total de elementos que hay en el SP a 0

        return elementos;
    }
}
