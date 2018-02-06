package net.ddns.smartfridge.smartfridgev2.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Representa la creación y actualización de la bbdd interna
 */

public class MiNeveraDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;//Cte para la versión de la BBDD
    private static final String DATABASE_NAME = "MiNevera";//Cte para el nombre de la BBDD
    public static final String TABLA_ALIMENTOS = "alimentos";//Para crear la tabla Alimentos
    public static final String TABLA_ALIMENTOS_CREADOS = "alimentos_creados";//Para crear la tabla Alimentos_Creados
    public static final String TABLA_LISTA= "lista";//Para crear la tabla Lista de la compra
    public static final String TABLA_ALIMENTO_EXTERNO_LISTA = "alimento_externo_lista";//Para crear la tabla alimentos_externo_lista
    public static final String TABLA_ALIMENTO_INTERNO_LISTA = "alimento_interno_lista";//Para crear la tabla alimentos_interno_lista
    public static final String [] CAMPOS_ALIMENTOS = {"_id", "nombre", "cantidad", "dias_caducidad", "fecha_registro", "fecha_caducidad", "imagen_alimento"};//Campos de la tabla alimentos
    private static final String CREATE_TABLA_ALIMENTOS  = "CREATE TABLE " + TABLA_ALIMENTOS + " (" + CAMPOS_ALIMENTOS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CAMPOS_ALIMENTOS[1] +
            " TEXT NOT NULL UNIQUE, " + CAMPOS_ALIMENTOS[2] + " INTEGER NOT NULL, " + CAMPOS_ALIMENTOS[3] + " INTEGER NOT NULL, " + CAMPOS_ALIMENTOS[4] + " TEXT, " + CAMPOS_ALIMENTOS[5] +
            " TEXT, " + CAMPOS_ALIMENTOS[6] + " BLOB)";//Tabla alimentos
    public static final String [] CAMPOS_ALI_CREADOS = {"_id", "nombre_ali_nuevo", "fecha_creado", "id_alimento"};//Nombre de los
    //campos de la tabla "alimentos_creados"
    private static final String CREATE_TABLA_ALIMENTOS_CREADOS = "CREATE TABLE " + TABLA_ALIMENTOS_CREADOS + " (" + CAMPOS_ALI_CREADOS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CAMPOS_ALI_CREADOS[1] + " TEXT NOT NULL, " + CAMPOS_ALI_CREADOS[2] + " TEXT NOT NULL, " + CAMPOS_ALI_CREADOS[3] + " INTEGER," +
            " FOREIGN KEY (" + CAMPOS_ALI_CREADOS[3] + ") REFERENCES " + TABLA_ALIMENTOS + "(" + CAMPOS_ALIMENTOS[0] + "));";//Tabla alimentos_creados
    public static final String [] CAMPOS_LISTA = {"id_lista", "fecha"};//Campos de la tabla lista
    public static final String [] CAMPOS_ALIMENTO_EXTERNO_LISTA = {"id_externo", "nombreAlimento", "id_lista"};//Campos de la talba alimento_externo_lista
    public static final String [] CAMPOS_ALIMENTO_INTERNO_LISTA = {"id_lista", "id_alimento"};//Campos de la tabla alimento_interno_lista
    private static final String CREATE_TABLA_LISTA = "CREATE TABLE " + TABLA_LISTA + " (" + CAMPOS_LISTA[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CAMPOS_LISTA[1] + " TEXT)";//Tabla lista
    private static final String CREATE_TABLA_ALIMENTO_EXTERNO_LISTA = "CREATE TABLE " + TABLA_ALIMENTO_EXTERNO_LISTA + " (" + CAMPOS_ALIMENTO_EXTERNO_LISTA[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPOS_ALIMENTO_EXTERNO_LISTA[1] + " TEXT UNIQUE NOT NULL, " + CAMPOS_ALIMENTO_EXTERNO_LISTA[2] + " INTEGER, FOREIGN KEY (" +
            CAMPOS_ALIMENTO_EXTERNO_LISTA[2] + ") REFERENCES " + TABLA_LISTA + "(" + CAMPOS_LISTA[0] + "));";//Tabla alimento_externo_lista
    private static final String CREATE_TABLA_ALIMENTO_INTERNO_LISTA = "CREATE TABLE " + TABLA_ALIMENTO_INTERNO_LISTA + " (" + CAMPOS_ALIMENTO_INTERNO_LISTA[0] + " INTEGER, " + CAMPOS_ALIMENTO_INTERNO_LISTA[1] +
            " INTEGER, PRIMARY KEY (" + CAMPOS_ALIMENTO_INTERNO_LISTA[0] + ", " + CAMPOS_ALIMENTO_INTERNO_LISTA[1] + ", FOREIGN KEY (" + CAMPOS_ALIMENTO_INTERNO_LISTA[0] + ") REFERENCES " + TABLA_LISTA +
            " (" + CAMPOS_LISTA[0] + "), FOREIGN KEY (" + CAMPOS_ALIMENTO_INTERNO_LISTA[1] + ") REFERENCES " + TABLA_ALIMENTOS + " (" + CAMPOS_ALIMENTOS[0] + "));";//Tabla alimento_interno_lista
    public static final String INSERT_ALIMENTO = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'manzana\', 3, 6, \'13-01-2018\', \'19-01-2018\')";
    public static final String INSERT_ALIMENTO2 = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'tomate\', 5, 10, \'05-02-2018\', \'16-03-2019\')";
    public static final String INSERT_ALIMENTO3 = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'coliflor\', 1, 0, \'13-01-2018\', \'19-01-2018\')";
    public static final String INSERT_ALIMENTO4 = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'limón\', 2, 0, \'13-01-2018\', \'06-02-2018\')";
    public static final String INSERT_ALI_CREADO = "INSERT INTO alimentos_creados (nombre_ali_nuevo, fecha_creado, id_alimento) VALUES (\'pomelo\', \'13-01-2018\', 1)";

    //Constructor de la clase
    public MiNeveraDB (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLA_ALIMENTOS);
        db.execSQL(CREATE_TABLA_ALIMENTOS_CREADOS);
        db.execSQL(CREATE_TABLA_LISTA);
        db.execSQL(CREATE_TABLA_ALIMENTO_EXTERNO_LISTA);
        db.execSQL(CREATE_TABLA_ALIMENTO_INTERNO_LISTA);
        db.execSQL(INSERT_ALIMENTO);
        db.execSQL(INSERT_ALIMENTO2);
        db.execSQL(INSERT_ALIMENTO3);
        db.execSQL(INSERT_ALIMENTO4);
        db.execSQL(INSERT_ALI_CREADO);
        Log.d("insert", "sql: " + INSERT_ALI_CREADO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //Programar cuando haya actualizaciones
    }
}
