package net.ddns.smartfridge.smartfridgev2.persistencia.gestores;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;
import net.ddns.smartfridge.smartfridgev2.persistencia.MiNeveraDB;

/**
 * Clase con los métodos para la gestión de persistencia en el módulo de listas de la compra.
 */

public class ListaCompraDB {
    private MiNeveraDB miNevera;//Para la instancia de MiNeveraDB
    private SQLiteDatabase sql;//Para crear la instancia de SQLiteDatabase para escritura
    private SQLiteDatabase sqe;//Para crear la instancia de SQLiteDatabase para lectura
    //Constructor
    public ListaCompraDB(Context contexto){
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

    //Método para crear una nueva lista de la compra
    public void insertarNuevaLista(ListaCompra l){
        ContentValues cv = new ContentValues();
        cv.put(MiNeveraDB.CAMPOS_LISTA[1], l.getFecha());
        sql.insert(MiNeveraDB.TABLA_ALIMENTOS_CREADOS, null, cv);
    }
}
