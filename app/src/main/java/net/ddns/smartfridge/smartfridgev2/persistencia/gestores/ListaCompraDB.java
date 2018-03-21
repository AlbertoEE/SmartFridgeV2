package net.ddns.smartfridge.smartfridgev2.persistencia.gestores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;
import net.ddns.smartfridge.smartfridgev2.persistencia.MiNeveraDB;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase con los métodos para la gestión de persistencia en el módulo de listas de la compra.
 */
public class ListaCompraDB {
    private MiNeveraDB miNevera;//Para la instancia de MiNeveraDB
    private SQLiteDatabase sql;//Para crear la instancia de SQLiteDatabase para escritura
    private SQLiteDatabase sqe;//Para crear la instancia de SQLiteDatabase para lectura
    private ArrayList<Integer> ids = new ArrayList<Integer>();//Para almacenar los ids de todas las listas que hay creadas
    private String sentencia;//Para crear todas las sentencias de la bbdd
    private Cursor cursor;//Cursor para recorrer los datos de la bbdd
    private Cursor cursor3;//Cursor para recorrer los datos de la bbdd
    private CopyOnWriteArrayList<ComponenteListaCompra> productos = new CopyOnWriteArrayList<ComponenteListaCompra>();//ArrayList para guardar los ítems de la lista de la compra
    private ListaCompra lc;//Para generar objetos ListaCompra y mostrarlos en la lista
    private ComponenteListaCompra componenteListaCompra;//Para crear objetos a partir de los datos de la bbdd
    private ArrayList<Integer> idsTabla = new ArrayList<Integer>();//Para recoger los ids de la tabla
    private CopyOnWriteArrayList<ComponenteListaCompra>productosM = new CopyOnWriteArrayList<ComponenteListaCompra>();
    private ArrayList<ComponenteListaCompra>todosLosProductos = new ArrayList<ComponenteListaCompra>();//Para crear la lista con todos los productos
    private String fecha;//Para almacenar la fecha obtenida a partir del id de la lista
    //private ArrayList<Lista>todosLosProductos2 = new ArrayList<>();

    /**
     * Constructor
     *
     * @param contexto Contexto de la Activity
     */
    public ListaCompraDB(Context contexto){
        miNevera = new MiNeveraDB(contexto);
        sqe= miNevera.getReadableDatabase();
        sql = miNevera.getWritableDatabase();
    }

    /**
     * Método para cerrar la conexión con la bbdd
     */
    public void cerrarConexion(){
        //Cerramos el acceso a la bbdd
        miNevera.close();
        sqe.close();
        sql.close();
    }

    /**
     * Método para crear una nueva lista de la compra
     *
     * @param l Objeto ListaCompra que representa una lista de la compra que caramos insertar
     */
    public void insertarNuevaLista(ListaCompra l){
        ContentValues cv = new ContentValues();
        cv.put(MiNeveraDB.CAMPOS_LISTA[1], l.getFecha());
        sql.insert(MiNeveraDB.TABLA_ALIMENTOS_CREADOS, null, cv);
    }

    /**
     * Método para insertar un alimento introducido manualmente por el usuario en la bbdd de alimento_manual
     *
     * @param nombreAlimento Nombre del alimento seleccionado para añadir a la lista
     */
    public void insertarAlimentoManual(String nombreAlimento){
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_MANUAL + " (" + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[1] + ") VALUES (\'" +
                nombreAlimento + "\');";
        sql.execSQL(sentencia);
    }

    /**
     * Metodo para recoger el id de un objeto de la tabla alimento_manual
     *
     * @param nombre_alimento nombre del alimento del que queremos saber su id
     * @return identificador del alimento pasado por parámetro
     */
    public int getIdAlimento(String nombre_alimento){
        int id=0;//Para almacenar el resultado de la bbdd
        sentencia = "SELECT " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[0] + " FROM " + MiNeveraDB.TABLA_ALIMENTO_MANUAL + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[1] + " = \'" +
                nombre_alimento + "\';";
        //El resultado se almacena en un cursor
        cursor = sqe.rawQuery(sentencia, new String[]{});
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

    /**
     * Método para crear una nueva lista de la compra
     *
     * @param l objeto ListaCompra que representa a la lista que queremos almacenar en la bbdd
     */
    public void insertarListaCompra(ListaCompra l){
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_LISTA + " (" + MiNeveraDB.CAMPOS_LISTA[1] + ") VALUES (\'" + l.getFecha() + "\');";
        sql.execSQL(sentencia);
    }

    /**
     * Método para coger el id de la nueva lista creada
     *
     * @param fechaLista fecha en la que se ha creado la lista
     * @return identificador de la lista cuya fecha hemos pasado por parámetro
     */
    public int getIdLista(String fechaLista){
        int id=0;//Para almacenar el resultado de la bbdd
        sentencia = "SELECT " + MiNeveraDB.CAMPOS_LISTA[0] + " FROM " + MiNeveraDB.TABLA_LISTA + " WHERE " + MiNeveraDB.CAMPOS_LISTA[1] + " = \'" +
                fechaLista + "\';";
        //El resultado se almacena en un cursor
        cursor = sqe.rawQuery(sentencia, new String[]{});
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

    /**
     * Método para insertar un componente de la lista en la tabla interna
     *
     * @param c       componente que representa un alimento en la lista, que proviene de los alimentos almacenados internamente
     * @param idLista identificador de la lista donde vamos a incluir ese elemento
     */
    public synchronized void insertComponenteInterno(ComponenteListaCompra c, int idLista){
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_INTERNO_LISTA + " VALUES (" + idLista + ", " + c.getId() + ");";
        Log.d("sentencia", "sentencia: " + sentencia);
        sql.execSQL(sentencia);
    }

    /**
     * Método para insertar un componente de la lista en la tabla externa
     *
     * @param c       componente que representa un alimento en la lista, que proviene de los alimentos almacenados externamente
     * @param idLista identificador de la lista donde vamos a incluir ese elemento
     */
    public void insertComponenteExterno(ComponenteListaCompra c, int idLista){
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_EXTERNO_LISTA + " VALUES (" + idLista + ", " + c.getId() + ");";
        sql.execSQL(sentencia);
    }

    /**
     * Insert componente manual.
     *
     * @param c       componente que representa un alimento en la lista, que proviene de los alimentos generados de manera manual
     * @param idLista identificador de la lista donde vamos a incluir ese elemento
     */
//Método para insertar un componente de la lista en la tabla de alimentos manuales
    public void insertComponenteManual(ComponenteListaCompra c, int idLista){
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_MANUAL_LISTA + " VALUES (" + idLista + ", " + c.getId() + ");";
        sql.execSQL(sentencia);
    }

    /**
     * Método para recuperar todos los ids de todas las listas que hay
     *
     * @return ArrayList con todos los ids de todas las listas almacenadas en la bbdd interna
     */
    public synchronized ArrayList<Integer> recuperarIdListas(){
        sentencia = "SELECT * FROM " + MiNeveraDB.TABLA_LISTA + ";";
        //El resultado se almacena en un cursor
        cursor = sqe.rawQuery(sentencia, new String[]{});
        //Comprobamos si se ha recogido algún registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //Añadimos cada id al ArrayList
                ids.add(cursor.getInt(0));
                Log.d("ref", "id compra: " + cursor.getInt(0));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return ids;
    }

    /**
     * Método para obtener la fecha de una lista almacenada en la bbdd
     *
     * @param id identificador de la lista
     * @return fecha de la lista indicada por parámetro
     */
    public String obtenerFechaLista(int id){
        sentencia = "SELECT " + MiNeveraDB.CAMPOS_LISTA[1] + " FROM " + MiNeveraDB.TABLA_LISTA + " WHERE " + MiNeveraDB.CAMPOS_LISTA[0] + " = " + id;
        //El resultado se almacena en un cursor
        cursor = sqe.rawQuery(sentencia, new String[]{});
        //Comprobamos si se ha recogido algún registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //Almacenamos la fecha en la variable
                fecha = cursor.getString(0);
                Log.d("fecha2", "fecha lista compra: " + cursor.getString(0));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return fecha;
    }

}
