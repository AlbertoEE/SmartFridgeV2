package net.ddns.smartfridge.smartfridgev2.persistencia;

import android.util.Log;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public String consultaCodBarras(String cod_barras) throws SQLException {
        String madre=null;
        String query_cod_barras = "SELECT * from " + TABLA_COD_ALI + " where cod_barras = \'" + cod_barras + "\'";
        //Log.d("prueba", query_cod_barras);
        Statement st = (Statement) conexion.createStatement();
        ResultSet rs = st.executeQuery(query_cod_barras);
        //Log.d("Suerte", "cursor antes del while");
        while (rs.next()) {
            madre = rs.getString(2);
            //Log.d("Suerte", madre);
        }
        return madre;
    }
    /**
     * Consulta de prueba
     * @throws SQLException

    public void consultar() throws SQLException {
        String sqlQuery = "SELECT * FROM CLASIFICACION_TIEMPO";
        Statement st = (Statement) conexion.createStatement();
        ResultSet rs = st.executeQuery(sqlQuery);

        while (rs.next()) {
            String madre = rs.getString(1);
            Log.d("Suerte", madre);
        }
    } */
}

