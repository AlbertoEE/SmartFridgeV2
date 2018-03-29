package net.ddns.smartfridge.smartfridgev2.persistencia.MySQL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento_Codigo;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Precio;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Tipo;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase creada para manejar las conexiones a la BBDD externa escrita en MySQL, para el correcto
 * funcionamiento, esta clase hace uso de el driver JDBC.
 */
public class MySQLHelper {
    private final static String DRIVER = "com.mysql.jdbc.Driver";//Driver para la conexión con la bbdd
    private final static String NOMBRE_BBDD = "";//Nombre de la bbdd
    private final static String IP = "";//Dirección ip del servidor
    private final static String PUERTO = "";//Puerto de conexión
    private final static String USER = "";//Usuario de la bbdd
    private final static String PASS = "";//Contraseña
    private String servidor = "jdbc:mysql://" + IP + ":" + PUERTO + "/" + NOMBRE_BBDD;
    private Connection conexion;
    private static final String TABLA_COD_ALI = "CODIGO_ALIMENTO";//Nombre de la tabla con los códigos de los alimentos
    private static final String TABLA_INGREDIENTES = "INGREDIENTES";//Nombre de la tabla con los datos de todos los alimentos
    private ArrayList<Ingrediente> alimentosCategoria;//Para almacenar los alimentos recogidos de la bbdd según su categoría
    private String sentencia;//Para recoger las sentcias sql de acceso a la bbdd
    private Receta receta;//Para construir un objeto a partir de los datos de la bbdd
    private Blob blob;//Para almacenar la imagne de la bbdd
    private Bitmap imagen;//Para almacenar la imagen de la bbdd;
    private Ingrediente alimentoExterno;//Para recoger los datos de la bbdd
    private ArrayList<Precio> precios;//Para meter todos los precios de una lista de la compra
    private Precio precio;//Construimos el objeto a partir de los datos de la bbdd
    private ArrayList<Receta> recetas;//Para almacenar objetos de tipo menu_receta
    private ArrayList<Tipo> tipos;//Para almacenar objetos con los tipos de recetas que haya
    private Tipo tipo;//Para crear objetos a partir de los datos de la bbdd

    /**
     * Abre la conexión con la BBDD
     *
     * @throws ClassNotFoundException cuando no se encuentra el driver especificado
     * @throws SQLException           cuando no se ha podido establecer la conexión
     */
    public void abrirConexion() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        conexion = (Connection) DriverManager.getConnection(servidor, USER, PASS);
    }

    /**
     * Cierra la conexión con la BBDD
     *
     * @throws SQLException cuando no se ha podido cerrar la conexión
     */
    public void cerrarConexion() throws SQLException {
        conexion.close();
    }

    /**
     * Consulta para comprobar si el código de barras escaneado está en la bbdd
     *
     * @param cod_barras el código de barras que queremos consultar
     * @return Alimento perteneciente a ese código de barras
     * @throws SQLException cuando no se puede acceder correctamente a la bbdd
     */
    public Alimento_Codigo consultaCodBarras(String cod_barras) throws SQLException {
        Alimento_Codigo ac=null;//Para almacenar los datos de la bbdd
        String nombre;//Para almacenar le nombre de la bbdd
        int id_ingrediente = 0;//Para almacenar el id del ingrediente

        String query_cod_barras = "SELECT * from " + TABLA_COD_ALI + " where cod_barras = \'" + cod_barras + "\'";
        Statement st = (Statement) conexion.createStatement();
        ResultSet rs = st.executeQuery(query_cod_barras);
        while (rs.next()) {
            id_ingrediente = rs.getInt(2);
        }
        String query_alimento = "SELECT * from " + TABLA_INGREDIENTES + " where id_ingrediente = " + id_ingrediente;
        rs = st.executeQuery(query_alimento);
        while (rs.next()) {
            nombre = rs.getString(2);
            //Recogemos el blob de la bbdd
            blob = rs.getBlob(3);
            //Lo pasamos a array de bytes
            byte[] data = blob.getBytes(1, (int)blob.length());
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            imagen = BitmapFactory.decodeStream(bais);
            ac = new Alimento_Codigo(id_ingrediente, nombre, cod_barras, imagen);
        }

        return ac;
    }

    /**
     * Método para seleccionar todos los ingredientes de una categoría determinada
     *
     * @param categoria categoria de los ingredientes que queremos recoger
     * @return ArrayList con todos los alimentos pertenecientes a esa categoria
     * @throws SQLException cuando no se puede acceder correctamente a la bbdd
     */
    public ArrayList<Ingrediente> recogerAlimentoPorCategoria(String categoria) throws SQLException {
        alimentosCategoria = new ArrayList<Ingrediente>();
        sentencia = "SELECT id_ingrediente, nombre, imagen FROM INGREDIENTES WHERE codificacion_compra = \'" + categoria + "\';";
        Statement st = (Statement) conexion.createStatement();
        ResultSet rs = st.executeQuery(sentencia);
        while (rs.next()) {
            blob = rs.getBlob(3);
            byte[] data = blob.getBytes(1, (int)blob.length());
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            imagen = BitmapFactory.decodeStream(bais);
            alimentoExterno = new Ingrediente(rs.getInt(1), rs.getString(2), imagen);
            alimentosCategoria.add(alimentoExterno);
        }
        return alimentosCategoria;
    }

    /**
     * Método para recoger el precio de una lista de la compra, según el supermercado
     *
     * @param nombres lista con los componentes de una lista de la compra
     * @param superm  nombre del supermercado a consultar
     * @return Lista con todos los precios de cada producto en el supermercado indicado
     */
    public ArrayList<Precio> recogerPrecio(ArrayList<ComponenteListaCompra> nombres, String superm){
        precios = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        for (int i=0; i<nombres.size(); i++) {
            String nombre = nombres.get(i).getNombreElemento();
            //Sacamos todos los datos de la bbdd
            sentencia = "SELECT " + superm + " FROM INGREDIENTES WHERE UPPER(nombre) = \'" + nombre + "\';";
            //Log.d("precio", "sentencia: " + sentencia);
            try {
                st = (Statement) conexion.createStatement();
                rs = st.executeQuery(sentencia);
                while (rs.next()) {
                    //Vamos creando los objetos que almacenaremos luego en un arraylist
                    precio = new Precio(nombres.get(i).getNombreElemento(), rs.getBigDecimal(1).doubleValue(), superm);
                    precios.add(precio);
                    //Log.d("precio", "precio: " + sentencia);
                }
            } catch (SQLException e) {
                Log.d("SQL", "Error de SQL: " + e.getErrorCode());
            }
        }
        Log.d("precio", "longitud tras hacer la consulta: " + precios.size());
        return precios;
    }

    /**
     * Método para recoger todas las recetas de la bbdd
     *
     * @return Lista con todas las recetas de la bbdd
     */
    public ArrayList<Receta> recogerRecetas(){
        recetas = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta;";
        Log.d("sentencia2", "sentencia: " + sentencia);
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            while (rs.next()) {
                blob = rs.getBlob(7);
                byte[] data = blob.getBytes(1, (int)blob.length());
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                imagen = BitmapFactory.decodeStream(bais);
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                receta = new Receta(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getString(5), rs.getString(6), imagen);
                recetas.add(receta);
                //Log.d("menu_receta", "menu_receta: " + menu_receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }

    /**
     * Método para seleccionar todos los ingredientes
     *
     * @return Lista con todos los ingredientes de la bbdd
     */
    public ArrayList<Ingrediente> recogerIngredientes(){
        alimentosCategoria = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        sentencia = "SELECT id_ingrediente, nombre FROM INGREDIENTES;";
        Log.d("sentencia", "sentencia: " + sentencia);
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            while (rs.next()) {
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                alimentoExterno = new Ingrediente(rs.getInt(1), rs.getString(2));
                alimentosCategoria.add(alimentoExterno);
                Log.d("menu_receta", "ingrediente: " + alimentoExterno.getNombreIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return alimentosCategoria;
    }

    /**
     * Método para recoger las recetas sin la foto en función de si tienen o no algún ingrediente
     *
     * @param consulta sentencia con la consulta a ejecutar
     * @return Lista de las recetas encontradas en función de la consulta aplicada
     */
    public ArrayList<Receta> filtrarReceta(String consulta){
        Log.d("check", "filtrarReceta");
        Log.d("check", "consulta: " + consulta);
        recetas = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(consulta);
            while (rs.next()) {
                Log.d("check", "hay registros");
                blob = rs.getBlob(7);
                byte[] data = blob.getBytes(1, (int)blob.length());
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                imagen = BitmapFactory.decodeStream(bais);
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                receta = new Receta(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getString(5), rs.getString(6), imagen);
                recetas.add(receta);
                Log.d("check", "menu_receta: " + receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }
    /**
     * Método para montar la sentencia SQL para la búsqueda en la bbdd
     *
     * @param aIngrediente listados de ingredientes que tiene que tener una receta
     * @return Sentencia para la búsqueda de esos ingredientes en la bbdd
     */
    public String montarSentenciaSi(ArrayList<Ingrediente> aIngrediente){
        int numero = aIngrediente.size(); //Para ver el número de ingredientes que ha seleccionado el usuario
        Log.d("check", "elementos en array: " + numero);
            if (numero==1){
                Log.d("check", "solo hay un elemento en el array");
                //sentencia = "SELECT * FROM RECETAS WHERE id_receta IN (SELECT id_receta FROM INGREDIENTES_RECETAS " +
                //        "WHERE id_ingrediente = " + aIngrediente.get(0).getIdIngrediente() + ");";
                sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                        "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta" +
                        " AND id_receta IN (SELECT id_receta FROM INGREDIENTES_RECETAS " +
                        "WHERE id_ingrediente = " + aIngrediente.get(0).getIdIngrediente() + ");";
                Log.d("sentencia", "sentencia con 1 ingrediente: " + sentencia);
            } else if (numero>1){
                Log.d("check", "entra por el else por que hay: " + numero);
                String ing="";
                for(int j =0; j<numero-1; j++){
                    ing += String.valueOf(aIngrediente.get(j).getIdIngrediente()) + ", ";
                    Log.d("check", "valor de la sentencia: " + ing);
                }
                sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                        "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta" +
                        " AND id_receta IN (SELECT id_receta FROM INGREDIENTES_RECETAS " +
                        "WHERE id_ingrediente IN (" + ing + aIngrediente.get(numero-1).getIdIngrediente() + ")" +
                        " GROUP BY id_receta HAVING COUNT(*) = " + numero + ");";
                Log.d("sentencia", "sentencia con varios ingredientes: " + sentencia);
            }
        return sentencia;
    }

    /**
     * Método para montar la sentencia SQL para la búsqueda en la bbdd
     *
     * @param aIngrediente listados de ingredientes que no tiene que tener una receta
     * @return Sentencia para la búsqueda de esos ingredientes en la bbdd
     */
    public String montarSentenciaNo(ArrayList<Ingrediente> aIngrediente){
        int numero = aIngrediente.size(); //Para ver el número de ingredientes que ha seleccionado el usuario
        if (numero==1){
            Log.d("check", "solo hay un elemento en el array");
            sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                    "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta" +
                    " AND id_receta NOT IN (SELECT id_receta FROM INGREDIENTES_RECETAS WHERE id_ingrediente = " + aIngrediente.get(0).getIdIngrediente() + ");";
            Log.d("sentencia", "sentencia con 1 ingrediente: " + sentencia);
        } else if (numero>1){
            Log.d("check", "entra por el else por que hay: " + numero);
            String ing="";
            for(int j =0; j<numero-1; j++){
                ing += String.valueOf(aIngrediente.get(j).getIdIngrediente()) + ", ";
                Log.d("check", "valor de la sentencia: " + ing);
            }
            sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                    "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta" +
                    " AND id_receta NOT IN (SELECT id_receta FROM INGREDIENTES_RECETAS " +
                    "WHERE id_ingrediente IN (" + ing + aIngrediente.get(numero-1).getIdIngrediente() + ")" +
                    " GROUP BY id_receta HAVING COUNT(*) = " + numero + ");";
            Log.d("sentencia", "sentencia con varios ingredientes: " + sentencia);
        }
        return sentencia;
    }

    /**
     * Método para filtrar las recetas por tipo
     *
     * @param tipo identificador del tipo de la receta a buscar
     * @return listado con las recetas filtradas en función del tipo indicado
     */
    public ArrayList<Receta> filtrarRecetaPorTipo(int tipo){
        recetas = new ArrayList<>();
        PreparedStatement pst =null;
        ResultSet rs = null;
        sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta" +
                " AND id_tipo_receta = ?;";
        Log.d("dialogo", "sentencia tipo: " + sentencia);
        try {
            pst = conexion.prepareStatement(sentencia);
            pst.setInt(1, tipo);
            rs = pst.executeQuery();
            while (rs.next()) {
                blob = rs.getBlob(7);
                byte[] data = blob.getBytes(1, (int)blob.length());
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                imagen = BitmapFactory.decodeStream(bais);
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                receta = new Receta(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getString(5), rs.getString(6), imagen);
                recetas.add(receta);
                Log.d("check", "menu_receta: " + receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }

    /**
     * Metodo para sacar todos los tipos de alimentos que hay en la bbdd
     *
     * @return Listado de todos los tipos de recetas que hay
     */
    public ArrayList<Tipo> sacarTipoReceta(){
        tipos  = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        sentencia = "SELECT * FROM TIPO_DE_RECETAS;";
        Log.d("sentencia", "sentencia: " + sentencia);
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            while (rs.next()) {
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                tipo = new Tipo(rs.getInt(1), rs.getString(2));
                tipos.add(tipo);
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return tipos;
    }

    /**
     * Método para seleccionar todos los ingredientes de una menu_receta dada
     *
     * @param id identificador de la receta
     * @return Listado con todos los ingredientes en función del id de receta indicado
     */
    public ArrayList<Ingrediente> recogerIngredientesReceta(int id){
        alimentosCategoria = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        sentencia = "SELECT I.id_ingrediente, I.nombre, IR.cantidad FROM INGREDIENTES I, INGREDIENTES_RECETAS IR " +
                "WHERE I.id_ingrediente = IR.id_ingrediente AND IR.id_receta=" + id + ";";
        Log.d("sentencia2", "sentencia: " + sentencia);
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            while (rs.next()) {
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                alimentoExterno = new Ingrediente(rs.getInt(1), rs.getString(2), rs.getInt(3));
                alimentosCategoria.add(alimentoExterno);
                Log.d("menu_receta", "ingrediente: " + alimentoExterno.getIdIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return alimentosCategoria;
    }

    /**
     * Método para buscar una menu_receta a partir de un texto
     *
     * @param texto String con el texto para buscar por nombre de receta
     * @return Listado de las recetas
     */
    public ArrayList<Receta> recogerRecetaTitulo(String  texto){
        recetas = new ArrayList<>();
        //PreparedStatement pst =null;
        Statement st=null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        if (texto==null || texto.isEmpty()) {
            sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                    "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta;";
        } else {
            sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                    "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta " +
                    "AND R.nombre_receta LIKE '%" + texto + "%';";
        }
        Log.d("sentencia3", "sentencia: " + sentencia);
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            Log.d("sentencia3", "TEXTO: " + texto);
            //rs = pst.executeQuery();
            Log.d("sentencia3", "rs: " + rs.getFetchSize());
            while (rs.next()) {
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                blob = rs.getBlob(7);
                byte[] data = blob.getBytes(1, (int)blob.length());
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                imagen = BitmapFactory.decodeStream(bais);
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                receta = new Receta(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getString(5), rs.getString(6), imagen);
                recetas.add(receta);
                Log.d("sentencia3", "receta: " + receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }

    /**
     * Método para recoger todos los elementos de duración
     *
     * @return Listado de todas las duraciones disponibles de recetas en la tabla correspondiente
     */
    public ArrayList<String> recogerDuracion(){
        ArrayList<String> duracion = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        sentencia = "SELECT duracion FROM CLASIFICACION_TIEMPO;";
        Log.d("sentencia2", "sentencia: " + sentencia);
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            while (rs.next()) {
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                duracion.add(rs.getString(1));
                Log.d("menu_receta", "ingrediente: " + alimentoExterno.getIdIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return duracion;
    }

    /**
     * Método para recoger todos los elementos de duración
     *
     * @return Listado con todas las dificultades almacenadas en la bbdd
     */
    public ArrayList<String> recogerDificultad(){
        ArrayList<String> dificultad = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        sentencia = "SELECT nombre_dificultad FROM DIFICULTAD;";
        Log.d("sentencia2", "sentencia: " + sentencia);
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            while (rs.next()) {
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                dificultad.add(rs.getString(1));
                Log.d("menu_receta", "ingrediente: " + alimentoExterno.getIdIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return dificultad;
    }

    /**
     * Método para recoger las recetas en función del tiempo y la dificultad
     *
     * @param tiempo     tiempo de duración de la receta
     * @param dificultad dificultad de la receta a buscar
     * @return Listado de recetas con la duración y el tiempo indicados
     */
    public ArrayList<Receta> recetaPorTiempoDificultad(String tiempo, String dificultad){
        recetas = new ArrayList<>();
        //PreparedStatement pst =null;
        Statement st=null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                    "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta " +
                    "AND T.duracion = \'" + tiempo + "\' AND D.nombre_dificultad = \'" + dificultad + "\';";
        Log.d("sentencia4", "sentencia: " + sentencia);
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            while (rs.next()) {
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                blob = rs.getBlob(7);
                byte[] data = blob.getBytes(1, (int)blob.length());
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                imagen = BitmapFactory.decodeStream(bais);
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                receta = new Receta(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getString(5), rs.getString(6), imagen);
                recetas.add(receta);
                Log.d("sentencia4", "receta: " + receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }

    /**
     * Método para filtrar recetas a partir de los ingredientes que hay en MiNevera
     *
     * @param alimentos Lista con los alimentos por los que queremos filtrar
     * @return Listado con las recetas correspondientes a esos alimentos
     */
    public ArrayList<Receta> filtrarRecetaMiNevera(ArrayList<String> alimentos){
        String nombreAlimento ="";
        recetas = new ArrayList<>();
        Statement st = null;
        ResultSet rs = null;
        int numero = alimentos.size(); //Para ver el número de ingredientes que ha seleccionado el usuario
        if (numero==1){
            Log.d("check", "solo hay un elemento en el array");
            sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                        "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND " +
                        "R.id_dificultad_receta = D.id_dificultad_receta AND R.id_receta IN (SELECT id_receta FROM INGREDIENTES_RECETAS WHERE id_ingrediente = (" +
                        "SELECT id_ingrediente FROM INGREDIENTES WHERE nombre = \'" + alimentos.get(0) + "\'));";
            Log.d("sentencia", "sentencia con 1 ingrediente: " + sentencia);
        } else if (numero>1){
            Log.d("check", "entra por el else por que hay: " + numero);
            String ing="";
            for(int j =0; j<numero-1; j++){
                nombreAlimento += alimentos.get(j) + " or nombre = ";
                Log.d("check", "valor de la sentencia: " + ing);
            }
            sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                    "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta " +
                    "AND R.id_receta IN (SELECT id_receta FROM INGREDIENTES_RECETAS WHERE id_ingrediente IN (SELECT id_ingrediente FROM INGREDIENTES " +
                    "WHERE nombre = \'" + ing + alimentos.get(numero-1)+"\') GROUP BY id_receta HAVING COUNT(*)= " + numero + ");";
            Log.d("sentencia", "sentencia con varios ingredientes: " + sentencia);
        }
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            while (rs.next()) {
                Log.d("check", "hay registros");
                blob = rs.getBlob(7);
                byte[] data = blob.getBytes(1, (int)blob.length());
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                imagen = BitmapFactory.decodeStream(bais);
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                receta = new Receta(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getString(5), rs.getString(6), imagen);
                recetas.add(receta);
                Log.d("check", "menu_receta: " + receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }
}

