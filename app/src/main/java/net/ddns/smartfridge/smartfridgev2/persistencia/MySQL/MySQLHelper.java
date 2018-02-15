package net.ddns.smartfridge.smartfridgev2.persistencia.MySQL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento_Codigo;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.DriverManager;
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
    private ArrayList<Alimento> alimentosCategoria;//Para almacenar los alimentos recogidos de la bbdd según su categoría
    private String sentencia;//Para recoger las sentcias sql de acceso a la bbdd
    private Alimento alimento;//Para construir un objeto a partir de los datos de la bbdd
    private Blob blob;//Para almacenar la imagne de la bbdd
    private Bitmap imagen;//Para almacenar la imagen de la bbdd;
    private Ingrediente alimentoExterno;//Para recoger los datos de la bbdd
    private ArrayList<Ingrediente>alimentosExternos;//Para meter todos los alimentos leidos de la bbdd en un array

    /**
     * Abre la conexión con la BBDD
     *
     * @throws ClassNotFoundException cuando no se encuentra el driver especificado
     * @throws SQLException cuando no se ha podido establecer la conexión
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
    public ArrayList<Alimento> recogerAlimentoPorCategoria(String categoria) throws SQLException {
        alimentosCategoria = new ArrayList<Alimento>();
        sentencia = "SELECT id_ingrediente, nombre, imagen FROM INGREDIENTES WHERE codificacion_compra = \'" + categoria + "\';";
        Statement st = (Statement) conexion.createStatement();
        ResultSet rs = st.executeQuery(sentencia);
        while (rs.next()) {
            blob = rs.getBlob(2);
            byte[] data = blob.getBytes(1, (int)blob.length());
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            imagen = BitmapFactory.decodeStream(bais);
            alimento = new Alimento(rs.getInt(0), rs.getString(1), imagen);
            alimentosCategoria.add(alimento);
        }
        return alimentosCategoria;
    }

    //Método para mostrar los alimentos en función de la categoría, para hacer la lista de la compra
    public ArrayList<Ingrediente> mostrarAlimentos(String categoria){
        alimentosExternos = new ArrayList<Ingrediente>();
        //Sacamos todos los datos de la bbdd
        sentencia = "SELECT * FROM INGREDIENTES WHERE clasificacion_compra = \'" + categoria + "\';";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = (Statement) conexion.createStatement();
            rs = st.executeQuery(sentencia);
            while (rs.next()) {
                //Construimos el objeto Ingrediente
                alimentoExterno = new Ingrediente(rs.getInt(0), rs.getString(1));
                alimentosExternos.add(alimentoExterno);
            }
        } catch (SQLException e) {
            Log.d("SQL", "Error de SQL: " + e.getErrorCode());
        }
        return alimentosExternos;
    }
}

