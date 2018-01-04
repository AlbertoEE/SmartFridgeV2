package net.ddns.smartfridge.smartfridgev2.modelo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by raquel on 29/12/2017.
 */

public class Permiso {
    public static final int PERM_FOTO=1;//Lo usaremos para indicar la posición del array para el permiso de la cámara

    //Metodo para comprobar si tiene concedido el permiso para acceder a la cámara
    public boolean permisoCamara(Context contexto, Activity actividad){
        boolean permisoC = false;//Declaramos la variable donde vamos a almacenar el permiso, la inicializamos como false
        //Declaramos una variable de tipo int donde se almacenará si se han dado o no permisos
        int permiso = ContextCompat.checkSelfPermission(contexto,
                Manifest.permission.CAMERA);
        if (permiso == PackageManager.PERMISSION_GRANTED){//El usuario ha aceptado el permiso
            permisoC=true;
        } else {
            permisoC=false;
        }
        return permisoC;
    }

}
