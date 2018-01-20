package net.ddns.smartfridge.smartfridgev2.persistencia.gestores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.persistencia.MiNeveraDB;

import java.io.ByteArrayOutputStream;

/**
 * Clase con los métodos para la capa de persistencia del objeto Alimento
 */

public class AlimentoDB {
    private MiNeveraDB miNevera;//Para la instancia de MiNeveraDB
    private SQLiteDatabase sql;//Para crear la instancia de SQLiteDatabase para escritura
    private SQLiteDatabase sqe;//Para crear la instancia de SQLiteDatabase para lectura
    private static final String QUERYBBDDCOMPLETA = "SELECT * FROM " + MiNeveraDB.TABLA_ALIMENTOS;

    //Constructor
    public AlimentoDB(Context contexto){
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

    //Metodo para insertar un nuevo objeto de tipo Alimento en la bbdd
    public void guardarAlimento(Alimento alimento){
        ContentValues cv = new ContentValues();
        Bitmap bitmap = alimento.getImagen();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 , baos);
        byte[] blob = baos.toByteArray();
        cv.put(MiNeveraDB.CAMPOS_ALIMENTOS[1], alimento.getNombreAlimento());
        cv.put(MiNeveraDB.CAMPOS_ALIMENTOS[2], alimento.getCantidad());
        cv.put(MiNeveraDB.CAMPOS_ALIMENTOS[3], alimento.getDias_caducidad());
        cv.put(MiNeveraDB.CAMPOS_ALIMENTOS[4], alimento.getFecha_registro());
        cv.put(MiNeveraDB.CAMPOS_ALIMENTOS[5], alimento.getFecha_caducidad());
        cv.put(MiNeveraDB.CAMPOS_ALIMENTOS[6], blob);
        sql.insert(MiNeveraDB.TABLA_ALIMENTOS, null, cv);
    }
    //Recoge todos los alimentos de la bbdd
    public Cursor getAlimentos(){
        //El resultado se almacena en un cursor
        Cursor cursor = sqe.rawQuery(QUERYBBDDCOMPLETA, new String[]{});
        return cursor;
    }

    //Metodo para borrar un alimento de la bbdd a partir de su id
    public void borrarAlimento(int id){
        sql.execSQL("DELETE FROM " + MiNeveraDB.TABLA_ALIMENTOS + " WHERE " + MiNeveraDB.CAMPOS_ALIMENTOS[0] + "=" + id + ";");
    }
}