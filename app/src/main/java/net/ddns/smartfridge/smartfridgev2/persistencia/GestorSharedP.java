package net.ddns.smartfridge.smartfridgev2.persistencia;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.DialogActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.InitialActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase que gestiona el almacenamiento del fichero Shared Preferences
 */

public class GestorSharedP {
    private ArrayList<ComponenteListaCompra> alimentosLeidosSP;//Para leer los aliemntos que hay en el SP almacenados
    private int elementos;//Para contar el número de elementos que hay en el SP
    private boolean hayElemento;//Variable para comprobar si hay elementos almacenados en el SP. Se inicializa a false
    private Map<String, ?> totalElementos = null;//Map con los datos contenidos en el SP
    private SharedPreferences sp= InitialActivity.getSp();//Cogemos el SP que hemos usado antes
    private ComponenteListaCompra componente;//Para crear los componentes a partir del SP

    //Método para comprobar si hay algún elemento en el SP
    public  int productosAlmacenados(){
        elementos = 0;
        //Comprobamos si hay algún elemento leyendo el SP
        try{
            totalElementos = sp.getAll();
            for (Map.Entry<String, ?> entry : totalElementos.entrySet()) {
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
    /*Método para recoger los valores del SP
    public ArrayList<String> recogerValores(int elem){
        String valor;//Para almacenar el valor recogido del SP
        alimentosLeidosSP = new ArrayList<String>();
        for (int i=1; i<=elem;i++){
            valor = sp.getString(String.valueOf(i),null);
            alimentosLeidosSP.add(valor);
            Log.d("sp", "alimento leido: " + valor);
        }
        return alimentosLeidosSP;
    }*/
    //Método para eliminar los datos almacenados en el SP
    public void borrarSP(){
        SharedPreferences.Editor editor = sp.edit();
        //Limpiamos lo que hay en el Shared Preferences
        editor.clear();
        editor.commit();
        Log.d("sp", "limpiamos el sp");
    }

    public boolean isHayElemento() {
        return hayElemento;
    }

    public void setHayElemento(boolean hayElemento) {
        this.hayElemento = hayElemento;
    }

    //Método para recoger los valores del SP
    public ArrayList<ComponenteListaCompra> recogerValores(){
        //Recogemos todos los datos del SP
        totalElementos = sp.getAll();
        alimentosLeidosSP = new ArrayList<ComponenteListaCompra>();
        for (Map.Entry<String, ?> entry : totalElementos.entrySet()) {
            componente = new ComponenteListaCompra(Integer.parseInt(entry.getKey()), (String)entry.getValue(), 1);
            alimentosLeidosSP.add(componente);
            Log.d("sp", entry.getKey() + ": " + entry.getValue().toString());
        }
        return alimentosLeidosSP;
    }


}
