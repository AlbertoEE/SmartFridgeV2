package net.ddns.smartfridge.smartfridgev2.persistencia.gestores;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.smartfridge.smartfridgev2.persistencia.MiNeveraDB;

/**
 * Clase para trabajar con los objetos de tipo Alimento_Nuevo insertados en la bbdd
 */

public class Alimento_NuevoDB {
    private MiNeveraDB miNevera;//Para la instancia de MiNeveraDB
    private SQLiteDatabase sql;//Para crear la instancia de SQLiteDatabase para escritura
    private SQLiteDatabase sqe;//Para crear la instancia de SQLiteDatabase para lectura
    private static final String QUERYBBDDCOMPLETA = "SELECT nombre_ali_nuevo FROM " + MiNeveraDB.TABLA_ALIMENTOS_CREADOS;

    //Constructor
    public Alimento_NuevoDB(Context contexto){
        miNevera = new MiNeveraDB(contexto);
        sqe= miNevera.getReadableDatabase();
        sql = miNevera.getWritableDatabase();
    }

    //Método para cerrar la conexión con la bbdd
    public void cerrarConexion(){
        //Cerramos el acceso a la bbdd
        miNevera.close();
        sqe.close();
        sql.close();
    }
    //Metodo para sacar el nombre de todos los alimentos de la tabla alimentos_creados
    public Cursor getAlimentosNuevos(){
        //El resultado se almacena en un cursor
        Cursor cursor = sqe.rawQuery(QUERYBBDDCOMPLETA, new String[]{});
        return cursor;
    }
}
