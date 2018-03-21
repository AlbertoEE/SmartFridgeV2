package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Clase que representa una receta de la bbdd
 */
public class Receta implements Serializable{
    private int idReceta;//El identificador de la menu_receta
    private String tituloReceta;//El título de la menu_receta
    private String descripcion;//Descripción de cómo se hace la menu_receta
    private int tipoReceta;//Clasificación de la menu_receta por categoría de alimento
    private int tiempoReceta;//Clasificación de la menu_receta por el tiempo de preparación
    private int dificultadReceta;//Clasificación de la menu_receta en función de la dificultad de elaboración
    private Bitmap imagenReceta;//Representa la imagen de la menu_receta
    private String tiempo;//Para tener el tiempo en lugar del id del tiempo
    private String dificultad;//Para escribir el texto con la dificultad

    /**
     * Constructor
     *
     * @param idReceta         id de la receta
     * @param tituloReceta     titulo de la receta
     * @param descripcion      descripción de la receta
     * @param tipoReceta       tipo de receta
     * @param tiempoReceta     tiempo que se tarda en preparar la receta
     * @param dificultadReceta dificultad de la receta
     * @param imagenReceta     imagen de la receta
     */
    public Receta(int idReceta, String tituloReceta, String descripcion, int tipoReceta, int tiempoReceta, int dificultadReceta, Bitmap imagenReceta) {
        this.idReceta = idReceta;
        this.tituloReceta = tituloReceta;
        this.descripcion = descripcion;
        this.tipoReceta = tipoReceta;
        this.tiempoReceta = tiempoReceta;
        this.dificultadReceta = dificultadReceta;
        this.imagenReceta = imagenReceta;
    }

    /**
     * Constructor
     *
     * @param idReceta         id de la receta
     * @param tituloReceta     titulo de la receta
     * @param descripcion      descripción de la receta
     * @param tipoReceta       tipo de receta
     * @param tiempo           tiempo que se tarda en preparar la receta
     * @param dificultadReceta dificultad de la receta
     * @param imagenReceta     imagen de la receta
     */
    public Receta(int idReceta, String tituloReceta, String descripcion, int tipoReceta, String tiempo, String dificultadReceta, Bitmap imagenReceta) {
        this.idReceta = idReceta;
        this.tituloReceta = tituloReceta;
        this.descripcion = descripcion;
        this.tipoReceta = tipoReceta;
        this.tiempo = tiempo;
        this.dificultad = dificultadReceta;
        this.imagenReceta = imagenReceta;
    }


    /**
     * Gets id receta.
     *
     * @return the id receta
     */
    public int getIdReceta() {
        return idReceta;
    }

    /**
     * Gets titulo receta.
     *
     * @return the titulo receta
     */
    public String getTituloReceta() {
        return tituloReceta;
    }

    /**
     * Gets descripcion.
     *
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Gets tipo receta.
     *
     * @return the tipo receta
     */
    public int getTipoReceta() {
        return tipoReceta;
    }

    /**
     * Gets imagen receta.
     *
     * @return the imagen receta
     */
    public Bitmap getImagenReceta() {
        return imagenReceta;
    }

    /**
     * Sets imagen receta.
     *
     * @param imagenReceta the imagen receta
     */
    public void setImagenReceta(Bitmap imagenReceta) {
        this.imagenReceta = imagenReceta;
    }

    /**
     * Gets tiempo.
     *
     * @return the tiempo
     */
    public String getTiempo() {
        return tiempo;
    }

    /**
     * Gets dificultad.
     *
     * @return the dificultad
     */
    public String getDificultad() {
        return dificultad;
    }

}
