package net.ddns.smartfridge.smartfridgev2.persistencia.MySQL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento_Codigo;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Precio;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Tipo;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
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
    private final static String NOMBRE_BBDD = "BBDD_SMART_FRIDGE";//Nombre de la bbdd
    private final static String IP = "smartfridge.ddns.net";//Dirección ip del servidor
    private final static String PUERTO = "3306";//Puerto de conexión
    private final static String USER = "RealRoot";//Usuario de la bbdd
    private final static String PASS = "OTTsteTl";//Contraseña
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
    private ArrayList<Receta> recetas;//Para almacenar objetos de tipo receta
    private ArrayList<Tipo> tipos;//Para almacenar objetos con los tipos de recetas que haya
    private Tipo tipo;//Para crear objetos a partir de los datos de la bbdd

    /**
     * Abre la conexión con la BBDD
     *
     * @throws ClassNotFoundException cuando no se encuentra el driver especificado
     * @throws SQLException cuando no se ha podido establecer la conexión
     */
    public void abrirConexion() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        conexion = (Connection) DriverManager.getConnection(servidor, USER, PASS);
        Log.d("BUGAZO", "abrirConexion: "+ conexion);
    }

    /**
     * Cierra la conexión con la BBDD
     *
     * @throws SQLException cuando no se ha podido cerrar la conexión
     */
    public void cerrarConexion() throws SQLException {
        conexion.close();
    }

    //Consulta para comprobar si el código de barras escaneado está en la bbdd
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
    //Método para seleccionar todos los ingredientes de una categoría determinada
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

    //Método para recoger el precio de una lista de la compra, según el supermercado
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

    //Método para recoger todas las recetas de la bbdd
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
                //Log.d("receta", "receta: " + receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }

    //Método para seleccionar todos los ingredientes
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
                Log.d("receta", "ingrediente: " + alimentoExterno.getNombreIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return alimentosCategoria;
    }

    //Método para recoger las recetas sin la foto en función de si tienen o no algún ingrediente
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
                Log.d("check", "receta: " + receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }
    //Método para recoger las recetas sin la foto en función de si tienen o no algún ingrediente
    public ArrayList<Receta> filtrarFotoReceta(String consulta){
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
                        rs.getInt(5), rs.getInt(6), imagen);
                recetas.add(receta);
                Log.d("check", "receta: " + receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }
    //Método para montar la sentencia SQL para la búsqueda en la bbdd
    public String montarSentenciaSi(ArrayList<Ingrediente> aIngrediente){
        //Log.d("check", "metodo en mysql");
        int numero = aIngrediente.size(); //Para ver el número de ingredientes que ha seleccionado el usuario
        Log.d("check", "elementos en array: " + numero);
       // for (int i=0; i<numero; i++){
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
             /*   sentencia = "SELECT * FROM RECETAS WHERE id_receta IN (SELECT id_receta FROM INGREDIENTES_RECETAS " +
                        "WHERE id_ingrediente IN (" + ing + aIngrediente.get(numero-1).getIdIngrediente() + ")" +
                        " GROUP BY id_receta HAVING COUNT(*) = " + numero + ");";*/
                sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                        "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta" +
                        " AND id_receta IN (SELECT id_receta FROM INGREDIENTES_RECETAS " +
                        "WHERE id_ingrediente IN (" + ing + aIngrediente.get(numero-1).getIdIngrediente() + ")" +
                        " GROUP BY id_receta HAVING COUNT(*) = " + numero + ");";
                Log.d("sentencia", "sentencia con varios ingredientes: " + sentencia);
            }
      //  }
        return sentencia;
    }
    //Método para montar la sentencia SQL para la búsqueda en la bbdd
    public String montarSentenciaNo(ArrayList<Ingrediente> aIngrediente){
        int numero = aIngrediente.size(); //Para ver el número de ingredientes que ha seleccionado el usuario
    //    for (Ingrediente i : aIngrediente){
        if (numero==1){
            Log.d("check", "solo hay un elemento en el array");
          /*  sentencia = "SELECT * FROM RECETAS WHERE id_receta NOT IN (SELECT id_receta FROM INGREDIENTES_RECETAS " +
                    "WHERE id_ingrediente = " + aIngrediente.get(0).getIdIngrediente() + ");";*/
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
        /*    sentencia = "SELECT * FROM RECETAS WHERE id_receta NOT IN (SELECT id_receta FROM INGREDIENTES_RECETAS " +
                    "WHERE id_ingrediente IN (" + ing + aIngrediente.get(numero-1).getIdIngrediente() + ")" +
                    " GROUP BY id_receta HAVING COUNT(*) = " + numero + ");";*/
            sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                    "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta" +
                    " AND id_receta NOT IN (SELECT id_receta FROM INGREDIENTES_RECETAS " +
                    "WHERE id_ingrediente IN (" + ing + aIngrediente.get(numero-1).getIdIngrediente() + ")" +
                    " GROUP BY id_receta HAVING COUNT(*) = " + numero + ");";
            Log.d("sentencia", "sentencia con varios ingredientes: " + sentencia);
        }
      //  }
        return sentencia;
    }

    //Método para filtrar las recetas por tipo
    public ArrayList<Receta> filtrarRecetaPorTipo(int tipo){
        recetas = new ArrayList<>();
        //Statement st = null;
        PreparedStatement pst =null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        //sentencia = "SELECT * FROM RECETAS WHERE id_tipo_receta = ?;";
        sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta" +
                " AND id_tipo_receta = ?;";
        Log.d("dialogo", "sentencia tipo: " + sentencia);
        try {
            //st = (Statement) conexion.createStatement();
            //rs = st.executeQuery(sentencia);
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
                Log.d("check", "receta: " + receta.getTituloReceta());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }

    //Metodo para sacar todos los tipos de alimentos que hay en la bbdd
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
                //Log.d("receta", "ingrediente: " + alimentoExterno.getNombreIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return tipos;
    }

    //Método para seleccionar todos los ingredientes de una receta dada
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
                Log.d("receta", "ingrediente: " + alimentoExterno.getIdIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return alimentosCategoria;
    }

    //Método para buscar una receta a partir de un texto
    public ArrayList<Receta> recogerRecetaTitulo(String  texto){
        recetas = new ArrayList<>();
        PreparedStatement pst =null;
        ResultSet rs = null;
        //Sacamos todos los datos de la bbdd
        if (texto==null) {
            sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                    "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta;";
        } else {
            sentencia = "SELECT R.id_receta, R.nombre_receta, R.descripcion_receta, R.id_tipo_receta, T.duracion, D.nombre_dificultad, R.imagen_receta " +
                    "FROM RECETAS R, CLASIFICACION_TIEMPO T, DIFICULTAD D WHERE R.id_tiempo_receta = T.id_tiempo_receta AND R.id_dificultad_receta = D.id_dificultad_receta " +
                    "AND R.nombre_receta LIKE % ? %;";
        }
        Log.d("sentencia2", "sentencia: " + sentencia);
        try {
            pst = conexion.prepareStatement(sentencia);
            pst.setString(1, texto);
            rs = pst.executeQuery();
            while (rs.next()) {
                //Vamos creando los objetos que almacenaremos luego en un arraylist
                alimentoExterno = new Ingrediente(rs.getInt(1), rs.getString(2), rs.getInt(3));
                alimentosCategoria.add(alimentoExterno);
                Log.d("receta", "ingrediente: " + alimentoExterno.getIdIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return recetas;
    }

    //Método para recoger todos los elementos de duración
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
                Log.d("receta", "ingrediente: " + alimentoExterno.getIdIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return duracion;
    }
    //Método para recoger todos los elementos de duración
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
                Log.d("receta", "ingrediente: " + alimentoExterno.getIdIngrediente());
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return dificultad;
    }
}

