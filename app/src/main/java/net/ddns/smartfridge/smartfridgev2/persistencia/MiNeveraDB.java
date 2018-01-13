package net.ddns.smartfridge.smartfridgev2.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Representa la creación y actualización de la bbdd interna
 */

public class MiNeveraDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;//Cte para la versión de la BBDD
    private static final String DATABASE_NAME = "MiNevera";//Cte para el nombre de la BBDD
    public static final String TABLA_ALIMENTOS = "alimentos";//Para crear la tabla Alimentos
    public static final String [] CAMPOS_ALIMENTOS = {"_id", "nombre", "cantidad", "dias_caducidad", "fecha_registro", "fecha_caducidad", "imagen_alimento"};//Campos de la tabla alimentos
    private static final String CREATE_TABLA_ALIMENTOS  = "CREATE TABLE " + TABLA_ALIMENTOS + " (" + CAMPOS_ALIMENTOS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CAMPOS_ALIMENTOS[1] +
            " TEXT NOT NULL, " + CAMPOS_ALIMENTOS[2] + " INTEGER NOT NULL, " + CAMPOS_ALIMENTOS[3] + " INTEGER NOT NULL, " + CAMPOS_ALIMENTOS[4] + " TEXT, " + CAMPOS_ALIMENTOS[5] +
            " TEXT, " + CAMPOS_ALIMENTOS[6] + " BLOB)";//Tabla users
    private static final String INSERT_ALIMENTO = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'manzana\', 3, 6, \'13012018\', \'19012018\')";

    //Constructor de la clase
    public MiNeveraDB (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLA_ALIMENTOS);
        db.execSQL(INSERT_ALIMENTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //Programar cuando haya actualizaciones
    }
}
