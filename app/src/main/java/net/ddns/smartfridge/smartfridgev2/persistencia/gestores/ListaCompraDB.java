package net.ddns.smartfridge.smartfridgev2.persistencia.gestores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
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

    //Método para insertar un alimento introducido manualmente por el usuario en la bbdd de alimento_manual
    public void insertarAlimentoManual(String nombreAlimento){
        String sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_MANUAL + " (" + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[1] + ") VALUES (\'" +
                nombreAlimento + "\');";
        sql.execSQL(sentencia);
    }

    //Metodo para recoger el id de un objeto de la tabla alimento_manual
    public int getIdAlimento(String nombre_alimento){
        int id=0;//Para almacenar el resultado de la bbdd
        String QUERYIDALIMENTO = "SELECT " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[0] + " FROM " + MiNeveraDB.TABLA_ALIMENTO_MANUAL + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[1] + " = \'" +
                nombre_alimento + "\';";
        //El resultado se almacena en un cursor
        Cursor cursor = sqe.rawQuery(QUERYIDALIMENTO, new String[]{});
        //Comprobamos si se ha recogido algún registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                id = cursor.getInt(0);
                Log.d("ref", "Cursor id: " + id);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return id;
    }

    //Método para crear una nueva lista de la compra
    public void insertarListaCompra(ListaCompra l){
        String sentencia = "INSERT INTO " + MiNeveraDB.TABLA_LISTA + " (" + MiNeveraDB.CAMPOS_LISTA[1] + ") VALUES (\'" + l.getFecha() + "\');";
        sql.execSQL(sentencia);
    }

    //Método para coger el id de la nueva lista creada
    public int getIdLista(String fechaLista){
        int id=0;//Para almacenar el resultado de la bbdd
        String QUERYIDALIMENTO = "SELECT " + MiNeveraDB.CAMPOS_LISTA[0] + " FROM " + MiNeveraDB.TABLA_LISTA + " WHERE " + MiNeveraDB.CAMPOS_LISTA[1] + " = \'" +
                fechaLista + "\';";
        //El resultado se almacena en un cursor
        Cursor cursor = sqe.rawQuery(QUERYIDALIMENTO, new String[]{});
        Log.d("ref", "Antes del if");
        //Comprobamos si se ha recogido algún registro
        if (cursor.moveToFirst()) {
            Log.d("ref", "Entramos en el bucle");
            //Recorremos el cursor hasta que no haya más registros
            do {
                id = cursor.getInt(0);
                Log.d("ref", "Cursor id: " + id);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return id;
    }

    //Método para insertar un componente de la lista en la tabla interna
    public void insertComponenteInterno(ComponenteListaCompra c, int idLista){
        String sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_INTERNO_LISTA + " VALUES (" + idLista + ", " + c.getId() + ");";
        sql.execSQL(sentencia);
    }

    //Método para insertar un componente de la lista en la tabla externa
    public void insertComponenteExterno(ComponenteListaCompra c, int idLista){
        String sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_EXTERNO_LISTA + " VALUES (" + c.getId() + ", \'" + c.getNombreElemento() + "\', " + idLista + ");";
        sql.execSQL(sentencia);
    }

    //Método para insertar un componente de la lista en la tabla de alimentos manuales
    public void insertComponenteManual(ComponenteListaCompra c, int idLista){
        String sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_MANUAL_LISTA + " VALUES (" + idLista + ", " + c.getId() + ");";
        sql.execSQL(sentencia);
    }
}
