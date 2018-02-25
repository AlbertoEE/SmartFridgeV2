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
    /**
     * The constant TABLA_ALIMENTOS.
     */
    public static final String TABLA_ALIMENTOS = "alimentos";//Para crear la tabla Alimentos
    /**
     * The constant TABLA_ALIMENTOS_CREADOS.
     */
    public static final String TABLA_ALIMENTOS_CREADOS = "alimentos_creados";//Para crear la tabla Alimentos_Creados
    /**
     * The constant TABLA_LISTA.
     */
    public static final String TABLA_LISTA= "lista";//Para crear la tabla Lista de la compra
    /**
     * The constant TABLA_ALIMENTO_EXTERNO.
     */
    public static final String TABLA_ALIMENTO_EXTERNO = "alimento_externo";//Para crear la tabla alimentos_externo
    /**
     * The constant TABLA_ALIMENTO_EXTERNO_LISTA.
     */
    public static final String TABLA_ALIMENTO_EXTERNO_LISTA = "alimento_externo_lista";//Para crear la tabla alimentos_externo_lista
    /**
     * The constant TABLA_ALIMENTO_INTERNO_LISTA.
     */
    public static final String TABLA_ALIMENTO_INTERNO_LISTA = "alimento_interno_lista";//Para crear la tabla alimentos_interno_lista
    /**
     * The constant TABLA_ALIMENTO_MANUAL.
     */
    public static final String TABLA_ALIMENTO_MANUAL = "alimento_manual";//Para crear la tabla de los alimentos de la lista insertados manualmente
    /**
     * The constant TABLA_ALIMENTO_MANUAL_LISTA.
     */
    public static final String TABLA_ALIMENTO_MANUAL_LISTA = "alimento_manual_lista";//Para crear la tabla alimentos_manual_lista
    /**
     * The constant CAMPOS_ALIMENTOS.
     */
    public static final String [] CAMPOS_ALIMENTOS = {"_id", "nombre", "cantidad", "dias_caducidad", "fecha_registro", "fecha_caducidad", "imagen_alimento"};//Campos de la tabla alimentos
    private static final String CREATE_TABLA_ALIMENTOS  = "CREATE TABLE " + TABLA_ALIMENTOS + " (" + CAMPOS_ALIMENTOS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CAMPOS_ALIMENTOS[1] +
            " TEXT NOT NULL UNIQUE, " + CAMPOS_ALIMENTOS[2] + " INTEGER NOT NULL, " + CAMPOS_ALIMENTOS[3] + " INTEGER NOT NULL, " + CAMPOS_ALIMENTOS[4] + " TEXT, " + CAMPOS_ALIMENTOS[5] +
            " TEXT, " + CAMPOS_ALIMENTOS[6] + " BLOB)";//Tabla alimentos
    /**
     * The constant CAMPOS_ALI_CREADOS.
     */
    public static final String [] CAMPOS_ALI_CREADOS = {"_id", "nombre_ali_nuevo", "fecha_creado", "id_alimento"};//Nombre de los
    //campos de la tabla "alimentos_creados"
    private static final String CREATE_TABLA_ALIMENTOS_CREADOS = "CREATE TABLE " + TABLA_ALIMENTOS_CREADOS + " (" + CAMPOS_ALI_CREADOS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CAMPOS_ALI_CREADOS[1] + " TEXT NOT NULL, " + CAMPOS_ALI_CREADOS[2] + " TEXT NOT NULL, " + CAMPOS_ALI_CREADOS[3] + " INTEGER," +
            " FOREIGN KEY (" + CAMPOS_ALI_CREADOS[3] + ") REFERENCES " + TABLA_ALIMENTOS + "(" + CAMPOS_ALIMENTOS[0] + ") ON DELETE SET NULL);";//Tabla alimentos_creados
    /**
     * The constant CAMPOS_LISTA.
     */
    public static final String [] CAMPOS_LISTA = {"id_lista", "fecha"};//Campos de la tabla lista
    /**
     * The constant CAMPOS_ALIMENTO_EXTERNO_LISTA.
     */
    public static final String [] CAMPOS_ALIMENTO_EXTERNO_LISTA = {"id_lista", "id_externo"};//Campos de la tabla alimento_externo_lista
    /**
     * The constant CAMPOS_ALIMENTO_EXTERNO.
     */
    public static final String [] CAMPOS_ALIMENTO_EXTERNO = {"id_externo", "nombreExterno"};//Campos de la talba alimento_externo
    /**
     * The constant CAMPOS_ALIMENTO_INTERNO_LISTA.
     */
    public static final String [] CAMPOS_ALIMENTO_INTERNO_LISTA = {"id_lista", "id_alimento"};//Campos de la tabla alimento_interno_lista
    /**
     * The constant CAMPOS_ALIMENTO_MANUAL.
     */
    public static final String [] CAMPOS_ALIMENTO_MANUAL = {"id_manual", "nombre_alimento"};//Campos de la tabla alimento_manual
    /**
     * The constant CAMPOS_ALIMENTO_MANUAL_LISTA.
     */
    public static final String [] CAMPOS_ALIMENTO_MANUAL_LISTA = {"id_lista", "id_manual"};//Campos de la tabla alimento_manual_lista
    private static final String CREATE_TABLA_LISTA = "CREATE TABLE " + TABLA_LISTA + " (" + CAMPOS_LISTA[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CAMPOS_LISTA[1] + " TEXT)";//Tabla lista
    private static final String CREATE_TABLA_ALIMENTO_EXTERNO = "CREATE TABLE " + TABLA_ALIMENTO_EXTERNO + " (" + CAMPOS_ALIMENTO_EXTERNO[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPOS_ALIMENTO_EXTERNO[1] + " TEXT);";//Tabla alimento_externo_lista
    private static final String CREATE_TABLA_ALIMENTO_EXTERNO_LISTA = "CREATE TABLE " + TABLA_ALIMENTO_EXTERNO_LISTA + " (" + CAMPOS_ALIMENTO_EXTERNO_LISTA[0] + " INTEGER, " +
            CAMPOS_ALIMENTO_EXTERNO_LISTA[1] + " INTEGER, PRIMARY KEY (" + CAMPOS_ALIMENTO_EXTERNO_LISTA[0] + ", " + CAMPOS_ALIMENTO_EXTERNO_LISTA[1] + ")" +
            ", FOREIGN KEY (" + CAMPOS_ALIMENTO_EXTERNO_LISTA[0] + ") REFERENCES " + TABLA_LISTA + " (" + CAMPOS_LISTA[0] + ") ON DELETE SET NULL, FOREIGN KEY (" +
            CAMPOS_ALIMENTO_EXTERNO_LISTA[1] + ") REFERENCES " + TABLA_ALIMENTO_EXTERNO + " (" + CAMPOS_ALIMENTO_EXTERNO[0] + ")ON DELETE SET NULL);";//Tabla alimento_interno_lista
    private static final String CREATE_TABLA_ALIMENTO_INTERNO_LISTA = "CREATE TABLE " + TABLA_ALIMENTO_INTERNO_LISTA + " (" + CAMPOS_ALIMENTO_INTERNO_LISTA[0] + " INTEGER, " + CAMPOS_ALIMENTO_INTERNO_LISTA[1] +
            " INTEGER, PRIMARY KEY (" + CAMPOS_ALIMENTO_INTERNO_LISTA[0] + ", " + CAMPOS_ALIMENTO_INTERNO_LISTA[1] + ")" +
            ", FOREIGN KEY (" + CAMPOS_ALIMENTO_INTERNO_LISTA[0] + ") REFERENCES " + TABLA_LISTA +
            " (" + CAMPOS_LISTA[0] + ") ON DELETE SET NULL, FOREIGN KEY (" + CAMPOS_ALIMENTO_INTERNO_LISTA[1] + ") REFERENCES " + TABLA_ALIMENTOS + " (" + CAMPOS_ALIMENTOS[0] +
            ")ON DELETE SET NULL);";//Tabla alimento_interno_lista
    private static final String CREATE_TABLA_ALIMENTO_MANUAL = "CREATE TABLE " + TABLA_ALIMENTO_MANUAL + " (" + CAMPOS_ALIMENTO_MANUAL[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPOS_ALIMENTO_MANUAL[1] + " TEXT NOT NULL)"; //Tabla alimento_manual
    private static final String CREATE_TABLA_ALIMENTO_MANUAL_LISTA = "CREATE TABLE " + TABLA_ALIMENTO_MANUAL_LISTA + " (" + CAMPOS_ALIMENTO_MANUAL_LISTA[0] + " INTEGER, " +
            CAMPOS_ALIMENTO_MANUAL_LISTA[1] + " INTEGER, PRIMARY KEY (" + CAMPOS_ALIMENTO_MANUAL_LISTA[0] + ", " + CAMPOS_ALIMENTO_MANUAL_LISTA[1] + ")" +
            ", FOREIGN KEY (" + CAMPOS_ALIMENTO_MANUAL_LISTA[0] + ") REFERENCES " + TABLA_LISTA + " (" + CAMPOS_LISTA[0] + ") ON DELETE SET NULL, FOREIGN KEY (" +
            CAMPOS_ALIMENTO_MANUAL_LISTA[1] + ") REFERENCES " + TABLA_ALIMENTO_MANUAL + " (" + CAMPOS_ALIMENTO_MANUAL[0] + ")ON DELETE SET NULL);";//Tabla alimento_manual_lista
    /**
     * The constant INSERT_ALIMENTO.
     */
    public static final String INSERT_ALIMENTO = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'manzana\', 3, 6, \'13-01-2018\', \'19-01-2018\')";
    /**
     * The constant INSERT_ALIMENTO2.
     */
    public static final String INSERT_ALIMENTO2 = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'tomate\', 5, 10, \'05-02-2018\', \'16-03-2019\')";
    /**
     * The constant INSERT_ALIMENTO3.
     */
    public static final String INSERT_ALIMENTO3 = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'coliflor\', 1, 0, \'13-01-2018\', \'19-01-2018\')";
    /**
     * The constant INSERT_ALIMENTO4.
     */
    public static final String INSERT_ALIMENTO4 = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'limón\', 2, 0, \'13-01-2018\', \'06-02-2018\')";
    /**
     * The constant INSERT_ALIMENTO5.
     */
    public static final String INSERT_ALIMENTO5 = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'mantequilla\', 1, 0, \'13-01-2018\', \'19-01-2018\')";
    /**
     * The constant INSERT_ALIMENTO6.
     */
    public static final String INSERT_ALIMENTO6 = "INSERT INTO alimentos (nombre, cantidad, dias_caducidad, fecha_registro, fecha_caducidad) VALUES (\'nata\', 1, 0, \'13-01-2018\', \'19-01-2018\')";
    /**
     * The constant INSERT_ALI_CREADO.
     */
    public static final String INSERT_ALI_CREADO = "INSERT INTO alimentos_creados (nombre_ali_nuevo, fecha_creado, id_alimento) VALUES (\'pomelo\', \'13-01-2018\', 1)";
    /**
     * The constant INSERT_EXTERNA.
     */
    public static final String INSERT_EXTERNA = "INSERT INTO alimento_externo (nombreExterno) VALUES (\'pavo\');";
    /**
     * The constant INSERT_EXTERNA2.
     */
    public static final String INSERT_EXTERNA2 = "INSERT INTO alimento_externo (nombreExterno) VALUES (\'queso\');";
    /**
     * The constant INSERT_EXTERNA3.
     */
    public static final String INSERT_EXTERNA3 = "INSERT INTO alimento_externo (nombreExterno) VALUES (\'chocolate\');";
    /**
     * The constant INSERT_MANUAL.
     */
    public static final String INSERT_MANUAL = "INSERT INTO alimento_manual (nombre_alimento) VALUES (\'chorizo\');";
    /**
     * The constant INSERT_MANUAL2.
     */
    public static final String INSERT_MANUAL2 = "INSERT INTO alimento_manual (nombre_alimento) VALUES (\'salchichón\');";
    /**
     * The constant INSERT_MANUAL3.
     */
    public static final String INSERT_MANUAL3 = "INSERT INTO alimento_manual (nombre_alimento) VALUES (\'jamón serrano\');";

    /**
     * Instantiates a new Mi nevera db.
     *
     * @param context the context
     */
//Constructor de la clase
    public MiNeveraDB (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLA_ALIMENTOS);
        db.execSQL(CREATE_TABLA_ALIMENTOS_CREADOS);
        db.execSQL(CREATE_TABLA_LISTA);
        db.execSQL(CREATE_TABLA_ALIMENTO_EXTERNO);
        db.execSQL(CREATE_TABLA_ALIMENTO_EXTERNO_LISTA);
        db.execSQL(CREATE_TABLA_ALIMENTO_INTERNO_LISTA);
        db.execSQL(CREATE_TABLA_ALIMENTO_MANUAL);
        db.execSQL(CREATE_TABLA_ALIMENTO_MANUAL_LISTA);
        db.execSQL(INSERT_ALIMENTO);
        db.execSQL(INSERT_ALIMENTO2);
        db.execSQL(INSERT_ALIMENTO3);
        db.execSQL(INSERT_ALIMENTO4);
        db.execSQL(INSERT_ALIMENTO5);
        db.execSQL(INSERT_ALIMENTO6);
        db.execSQL(INSERT_ALI_CREADO);
        db.execSQL(INSERT_EXTERNA);
        db.execSQL(INSERT_EXTERNA2);
        db.execSQL(INSERT_EXTERNA3);
        db.execSQL(INSERT_MANUAL);
        db.execSQL(INSERT_MANUAL2);
        db.execSQL(INSERT_MANUAL3);
        Log.d("insert", "sql: " + INSERT_ALI_CREADO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //Programar cuando haya actualizaciones
    }
}
