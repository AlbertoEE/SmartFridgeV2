package net.ddns.smartfridge.smartfridgev2.vista.actividades;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Activity que muestra el dialog cuando haya que incluir un alimento en el Shared Preferences para sugerírselo al usuario cuando vaya a hacer una nueva
 * lista de la compra
 */
public class DialogActivity extends Activity {
    private Intent i;//Para recoger el intent que nos llega de la notificación del service
    //private Map<Integer, String> datosSP;//Para guardar los datos recibidos del intent
    private int posicion;//Para poner la posición en el SP
    private static SharedPreferences mySp;//Referencia al Sharedpreferences de la app
    private ComponenteListaCompra componente;//Para crear un componente nuevo de la lista para añadirlo a esta


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        //Recogemos el intent
        i = getIntent();
        componente = (ComponenteListaCompra)i.getSerializableExtra("Alimento");
        Log.d("sp", componente.getNombreElemento());
        mySp = InitialActivity.getSp();
        guardarSP(componente.getId(), componente.getNombreElemento());
        Dialogos d = new Dialogos(this, this);
        d.dialogListaCompra();
    }

    /**
     * Método para guardar lo recibido en el intent en el SharedPreferences
     *
     * @param posicion       posicion para guardar en el Shared Preferences
     * @param nombreAlimento Nombre del alimento
     */
    public void guardarSP(int posicion, String nombreAlimento){
        //Escribimos los datos. Generamos el editor
        SharedPreferences.Editor editor = mySp.edit();
        //Metemos cada par clave/valor
        editor.putString(String.valueOf(posicion), nombreAlimento);
        editor.commit();
    }
}
