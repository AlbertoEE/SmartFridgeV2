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
    private ArrayList<Lista>todosLosProductos2 = new ArrayList<>();

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
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_MANUAL + " (" + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[1] + ") VALUES (\'" +
                nombreAlimento + "\');";
        sql.execSQL(sentencia);
    }

    //Metodo para recoger el id de un objeto de la tabla alimento_manual
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

    //Método para crear una nueva lista de la compra
    public void insertarListaCompra(ListaCompra l){
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_LISTA + " (" + MiNeveraDB.CAMPOS_LISTA[1] + ") VALUES (\'" + l.getFecha() + "\');";
        sql.execSQL(sentencia);
    }

    //Método para coger el id de la nueva lista creada
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

    //Método para insertar un componente de la lista en la tabla interna
    public synchronized void insertComponenteInterno(ComponenteListaCompra c, int idLista){
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_INTERNO_LISTA + " VALUES (" + idLista + ", " + c.getId() + ");";
        Log.d("sentencia", "sentencia: " + sentencia);
        sql.execSQL(sentencia);
    }

    //Método para insertar un componente de la lista en la tabla externa
    public void insertComponenteExterno(ComponenteListaCompra c, int idLista){
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_EXTERNO_LISTA + " VALUES (" + idLista + ", " + c.getId() + ");";
        sql.execSQL(sentencia);
    }

    //Método para insertar un componente de la lista en la tabla de alimentos manuales
    public void insertComponenteManual(ComponenteListaCompra c, int idLista){
        sentencia = "INSERT INTO " + MiNeveraDB.TABLA_ALIMENTO_MANUAL_LISTA + " VALUES (" + idLista + ", " + c.getId() + ");";
        sql.execSQL(sentencia);
    }

    //Método para recuperar todos los ids de todas las listas que hay
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


    /*Método para recuperar los productos de la lista interna, a partir de un id
    public ArrayList<Lista> recuperarComponentesListaInterna(){
        String fecha;
        Lista listaCompra;
        ArrayList<Lista>todasLasListas = new ArrayList<Lista>();
        ids.clear();
        ids = recuperarIdListas();
        for (int n : ids) {
            Log.d("fecha2", "id= " + n);
            sentencia = "SELECT * FROM " + MiNeveraDB.TABLA_ALIMENTO_INTERNO_LISTA + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_INTERNO_LISTA[0] + " = " + n + ";";
            //El resultado se almacena en un cursor
            cursor = sqe.rawQuery(sentencia, new String[]{});
            //Comprobamos si se ha recogido algún registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //Añadimos cada id al ArrayList
                    idsTabla.add(cursor.getInt(1));
                    Log.d("ref", "id tabla interna: " + cursor.getInt(1));
                } while (cursor.moveToNext());
            }
            //Una vez que tenemos los ids, necesitamos recuperar el nombre de esos alimentos de la tabla lista_interna
            for (int numero : idsTabla) {
                fecha = obtenerFechaLista(numero);
                sentencia = "SELECT " + MiNeveraDB.CAMPOS_ALIMENTOS[1] + " FROM  " + MiNeveraDB.TABLA_ALIMENTOS + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTOS[0] + " = " + numero + ";";
                cursor = sqe.rawQuery(sentencia, new String[]{});
                if (cursor.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        componenteListaCompra = new ComponenteListaCompra(numero, cursor.getString(0), 1);
                        productos.add(componenteListaCompra);
                        Log.d("ref", "nombre alimento interna: " + cursor.getString(0));
                        Log.d("ref", "unidades en array: " + productos.size());
                    } while (cursor.moveToNext());
                }
                listaCompra = new Lista(numero, fecha, productos);
                todasLasListas.add(listaCompra);
            }
        }
        cursor.close();
        return todasLasListas;
    }

    //Método para recuperar los productos de la lista interna, a partir de un id
    public ArrayList<Lista> recuperarComponentesListaExterna(ArrayList<Lista> todasLasListas){
        //Recogemos ahora los datos de la lista externa
        CopyOnWriteArrayList<ComponenteListaCompra> auxiliar = new CopyOnWriteArrayList();
        for (Lista l : todasLasListas){
            int id = l.getId();
            auxiliar = l.getProductos();
            sentencia = "SELECT * FROM " + MiNeveraDB.TABLA_ALIMENTO_EXTERNO_LISTA + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_EXTERNO_LISTA[0] + " = " + id + ";";
            //El resultado se almacena en un cursor
            cursor = sqe.rawQuery(sentencia, new String[]{});
            //Comprobamos si se ha recogido algún registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //Añadimos cada id al ArrayList
                    idsTabla.add(cursor.getInt(1));
                    Log.d("ref", "id tabla externa: " + cursor.getInt(1));
                } while (cursor.moveToNext());
            }
            //Una vez que tenemos los ids, necesitamos recuperar el nombre de esos alimentos de la tabla lista_externa
            for (int numero : idsTabla) {
                sentencia = "SELECT " + MiNeveraDB.CAMPOS_ALIMENTO_EXTERNO[1] + " FROM  " + MiNeveraDB.TABLA_ALIMENTO_EXTERNO + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_EXTERNO[0] + " = " + numero + ";";
                cursor = sqe.rawQuery(sentencia, new String[]{});
                if (cursor.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        componenteListaCompra = new ComponenteListaCompra(numero, cursor.getString(0), 2);
                        auxiliar.add(componenteListaCompra);
                        Log.d("ref", "nombre alimento externa: " + cursor.getString(0));
                        Log.d("ref", "unidades en array: " + auxiliar.size());
                    } while (cursor.moveToNext());
                }
            }
            sentencia = "SELECT * FROM " + MiNeveraDB.TABLA_ALIMENTO_MANUAL_LISTA + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL_LISTA[0] + " = " + id + ";";
            //El resultado se almacena en un cursor
            cursor3 = sqe.rawQuery(sentencia, new String[]{});
            //Comprobamos si se ha recogido algún registro
            if (cursor3.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //Añadimos cada id al ArrayList
                    idsTabla.add(cursor3.getInt(1));
                    Log.d("ref", "id tabla manual: " + cursor3.getInt(1));
                } while(cursor3.moveToNext());
            }
            //Una vez que tenemos los ids, necesitamos recuperar el nombre de esos alimentos de la tabla lista_externa
            for (int numero : idsTabla){
                sentencia = "SELECT "+ MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[1] + " FROM  " + MiNeveraDB.TABLA_ALIMENTO_MANUAL + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[0] + " = " + numero + ";";
                cursor3 = sqe.rawQuery(sentencia, new String[]{});
                if (cursor3.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        componenteListaCompra = new ComponenteListaCompra(numero, cursor3.getString(0), 3);
                        auxiliar.add(componenteListaCompra);
                        Log.d("ref", "nombre alimento manual: " + cursor3.getString(0));
                    } while(cursor3.moveToNext());
                }
            }
            l.setProductos(auxiliar);
            todosLosProductos2.add(l);
        }
        cursor.close();
        cursor3.close();
        /*
        ids.clear();
        ids = recuperarIdListas();
        productos.clear();
        for (int n : ids) {
            sentencia = "SELECT * FROM " + MiNeveraDB.TABLA_ALIMENTO_EXTERNO_LISTA + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_EXTERNO_LISTA[0] + " = " + n + ";";
            //El resultado se almacena en un cursor
            cursor = sqe.rawQuery(sentencia, new String[]{});
            //Comprobamos si se ha recogido algún registro
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //Añadimos cada id al ArrayList
                    idsTabla.add(cursor.getInt(1));
                    Log.d("ref", "id tabla externa: " + cursor.getInt(1));
                } while (cursor.moveToNext());
            }
            //cursor.close();
            //Una vez que tenemos los ids, necesitamos recuperar el nombre de esos alimentos de la tabla lista_externa
            for (int numero : idsTabla) {
                sentencia = "SELECT " + MiNeveraDB.CAMPOS_ALIMENTO_EXTERNO[1] + " FROM  " + MiNeveraDB.TABLA_ALIMENTO_EXTERNO + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_EXTERNO[0] + " = " + numero + ";";
                cursor = sqe.rawQuery(sentencia, new String[]{});
                if (cursor.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        componenteListaCompra = new ComponenteListaCompra(numero, cursor.getString(0), 2);
                        productos.add(componenteListaCompra);
                        Log.d("ref", "nombre alimento externa: " + cursor.getString(0));
                        Log.d("ref", "unidades en array: " + productos.size());
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        return todosLosProductos2;
    }
*/
    //Método para recuperar los productos de la lista interna, a partir de un id/*
  /*  public synchronized CopyOnWriteArrayList<ComponenteListaCompra> recuperarComponentesListaManual(){
        //Recogemos ahora los datos de la lista externa
        ids.clear();
        idsTabla.clear();
        ids = recuperarIdListas();
        productosM.clear();
        for (int n : ids) {
            sentencia = "SELECT * FROM " + MiNeveraDB.TABLA_ALIMENTO_MANUAL_LISTA + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL_LISTA[0] + " = " + n + ";";
            //El resultado se almacena en un cursor
            cursor3 = sqe.rawQuery(sentencia, new String[]{});
            //Comprobamos si se ha recogido algún registro
            if (cursor3.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    //Añadimos cada id al ArrayList
                    idsTabla.add(cursor3.getInt(1));
                    Log.d("ref", "id tabla manual: " + cursor3.getInt(1));
                } while(cursor3.moveToNext());
            }
            //Una vez que tenemos los ids, necesitamos recuperar el nombre de esos alimentos de la tabla lista_externa
            for (int numero : idsTabla){
                sentencia = "SELECT "+ MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[1] + " FROM  " + MiNeveraDB.TABLA_ALIMENTO_MANUAL + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[0] + " = " + numero + ";";
                cursor3 = sqe.rawQuery(sentencia, new String[]{});
                if (cursor3.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros
                    do {
                        componenteListaCompra = new ComponenteListaCompra(numero, cursor3.getString(0), 3);
                        productosM.add(componenteListaCompra);
                        Log.d("ref", "nombre alimento manual: " + cursor3.getString(0));
                    } while(cursor3.moveToNext());
                }
            }
        }
        cursor3.close();
        return productosM;
    }
*/
    /*Método para recuperar todos los productos de una lista a partir del id de esa lista
    public synchronized ArrayList<ComponenteListaCompra> recuperarComponentesLista(Object id){
        ids.clear();
        sentencia = "SELECT * FROM " + MiNeveraDB.TABLA_ALIMENTO_INTERNO_LISTA + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_INTERNO_LISTA[0] + " = " + id + ";";
        //El resultado se almacena en un cursor
        cursor = sqe.rawQuery(sentencia, new String[]{});
        //Comprobamos si se ha recogido algún registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //Añadimos cada id al ArrayList
                ids.add(cursor.getInt(1));
                Log.d("ref", "id tabla interna: " + cursor.getInt(1));
            } while(cursor.moveToNext());
        }
        //cursor.close();
        //Una vez que tenemos los ids, necesitamos recuperar el nombre de esos alimentos de la tabla lista_interna
        for (int numero : ids){
            sentencia = "SELECT "+ MiNeveraDB.CAMPOS_ALIMENTOS[1] + " FROM  " + MiNeveraDB.TABLA_ALIMENTOS + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTOS[0] + " = " + numero + ";";
            cursor = sqe.rawQuery(sentencia, new String[]{});
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    componenteListaCompra = new ComponenteListaCompra(numero, cursor.getString(0), 1);
                    productos.add(componenteListaCompra);
                    Log.d("ref", "nombre alimento interna: " + cursor.getString(0));
                    Log.d("ref", "unidades en array: "+ productos.size());
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
        //Recogemos ahora los datos de la lista externa
        ids.clear();
        sentencia = "SELECT * FROM " + MiNeveraDB.TABLA_ALIMENTO_EXTERNO_LISTA + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_EXTERNO_LISTA[0] + " = " + id + ";";
        //El resultado se almacena en un cursor
        cursor = sqe.rawQuery(sentencia, new String[]{});
        //Comprobamos si se ha recogido algún registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //Añadimos cada id al ArrayList
                ids.add(cursor.getInt(1));
                Log.d("ref", "id tabla externa: " + cursor.getInt(1));
            } while(cursor.moveToNext());
        }
        //cursor.close();
        //Una vez que tenemos los ids, necesitamos recuperar el nombre de esos alimentos de la tabla lista_externa
        for (int numero : ids){
            sentencia = "SELECT "+ MiNeveraDB.CAMPOS_ALIMENTO_EXTERNO[1] + " FROM  " + MiNeveraDB.TABLA_ALIMENTO_EXTERNO + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_EXTERNO[0] + " = " + numero + ";";
            cursor = sqe.rawQuery(sentencia, new String[]{});
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    componenteListaCompra = new ComponenteListaCompra(numero, cursor.getString(0), 2);
                    productos.add(componenteListaCompra);
                    Log.d("ref", "nombre alimento externa: " + cursor.getString(0));
                    Log.d("ref", "unidades en array: "+ productos.size());
                } while(cursor.moveToNext());
            }
        }
        //cursor.close();
        //Recogemos ahora los datos de la tabla manual
        ids.clear();
        sentencia = "SELECT * FROM " + MiNeveraDB.TABLA_ALIMENTO_MANUAL_LISTA + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL_LISTA[0] + " = " + id + ";";
        //El resultado se almacena en un cursor
        cursor = sqe.rawQuery(sentencia, new String[]{});
        //Comprobamos si se ha recogido algún registro
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //Añadimos cada id al ArrayList
                ids.add(cursor.getInt(1));
                Log.d("ref", "id tabla manual: " + cursor.getInt(1));
            } while(cursor.moveToNext());
        }
        cursor.close();
        //Una vez que tenemos los ids, necesitamos recuperar el nombre de esos alimentos de la tabla lista_externa
        for (int numero : ids){
            sentencia = "SELECT "+ MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[1] + " FROM  " + MiNeveraDB.TABLA_ALIMENTO_MANUAL + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTO_MANUAL[0] + " = " + numero + ";";
            cursor = sqe.rawQuery(sentencia, new String[]{});
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    componenteListaCompra = new ComponenteListaCompra(numero, cursor.getString(0), 3);
                    productos.add(componenteListaCompra);
                    Log.d("ref", "nombre alimento manual: " + cursor.getString(0));
                } while(cursor.moveToNext());
            }
        }
        cursor.close();
        return productos;
    }*/

    //Método para obtener la fecha de una lista almacenada en la bbdd
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
