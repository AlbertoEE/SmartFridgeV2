package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * Clase que representa un alimento de la bbdd de sqlite
 */
public class Alimento implements Serializable {
    private int id;//Id del alimento
    private String nombreAlimento;//Nombre del alimento
    private int cantidad;//Número de unidades del alimento almacenadas
    private String fecha_registro;//Fecha en la que se registró el alimento
    private String fecha_caducidad;//Fecha de caducidad del aliemnto
    private int dias_caducidad;//Dias que faltan para que el alimento caduque
    private Bitmap imagen;//Imagen del alimento

    /**
     * Instantiates a new Alimento.
     *
     * @param id              the id
     * @param nombreAlimento  the nombre alimento
     * @param cantidad        the cantidad
     * @param dias_caducidad  the dias caducidad
     * @param fecha_registro  the fecha registro
     * @param fecha_caducidad the fecha caducidad
     * @param imagen          the imagen
     */
//Constructor 1
    public Alimento(int id, String nombreAlimento, int cantidad, int dias_caducidad,
                    String fecha_registro, String fecha_caducidad, Bitmap imagen) {
        this.id = id;
        this.nombreAlimento = nombreAlimento;
        this.cantidad = cantidad;
        this.dias_caducidad = dias_caducidad;
        this.fecha_registro = fecha_registro;
        this.fecha_caducidad = fecha_caducidad;
        this.imagen = imagen;
    }

    /**
     * Instantiates a new Alimento.
     *
     * @param nombreAlimento  the nombre alimento
     * @param cantidad        the cantidad
     * @param dias_caducidad  the dias caducidad
     * @param fecha_registro  the fecha registro
     * @param fecha_caducidad the fecha caducidad
     * @param imagen          the imagen
     */
//Constructor 2
    public Alimento(String nombreAlimento, int cantidad, int dias_caducidad, String fecha_registro,
            String fecha_caducidad, Bitmap imagen) {
        this.nombreAlimento = nombreAlimento;
        this.cantidad = cantidad;
        this.fecha_registro = fecha_registro;
        this.fecha_caducidad = fecha_caducidad;
        this.dias_caducidad = dias_caducidad;
        this.imagen = imagen;
    }

    /**
     * Instantiates a new Alimento.
     *
     * @param nombreA the nombre a
     * @param bm      the bm
     */
//Constructor que recibe como parámetros el nombre y la imagen
    public Alimento (String nombreA, Bitmap bm){
        this.nombreAlimento = nombreA;
        this.imagen = bm;
    }

    /**
     * Instantiates a new Alimento.
     *
     * @param _id     the id
     * @param nombreA the nombre a
     * @param bm      the bm
     */
//Constructor que recibe como parámetros el id, el nombre y la imagen
    public Alimento (int _id, String nombreA, Bitmap bm){
        this.id = _id;
        this.nombreAlimento = nombreA;
        this.imagen = bm;
    }


    /**
     * Gets cantidad.
     *
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets nombre alimento.
     *
     * @return the nombre alimento
     */
    public String getNombreAlimento() {
        return nombreAlimento;
    }

    /**
     * Sets nombre alimento.
     *
     * @param nombreAlimento the nombre alimento
     */
    public void setNombreAlimento(String nombreAlimento) {
        this.nombreAlimento = nombreAlimento;
    }

    /**
     * Sets cantidad.
     *
     * @param cantidad the cantidad
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Gets fecha registro.
     *
     * @return the fecha registro
     */
    public String getFecha_registro() {
        return fecha_registro;
    }

    /**
     * Sets fecha registro.
     *
     * @param fecha_registro the fecha registro
     */
    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    /**
     * Gets fecha caducidad.
     *
     * @return the fecha caducidad
     */
    public String getFecha_caducidad() {
        return fecha_caducidad;
    }

    /**
     * Sets fecha caducidad.
     *
     * @param fecha_caducidad the fecha caducidad
     */
    public void setFecha_caducidad(String fecha_caducidad) {
        this.fecha_caducidad = fecha_caducidad;
    }

    /**
     * Gets dias caducidad.
     *
     * @return the dias caducidad
     */
    public int getDias_caducidad() {
        return dias_caducidad;
    }

    /**
     * Sets dias caducidad.
     *
     * @param dias_caducidad the dias caducidad
     */
    public void setDias_caducidad(int dias_caducidad) {
        this.dias_caducidad = dias_caducidad;
    }

    /**
     * Gets imagen.
     *
     * @return the imagen
     */
    public Bitmap getImagen() {
        return imagen;
    }

    /**
     * Sets imagen.
     *
     * @param imagen the imagen
     */
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
