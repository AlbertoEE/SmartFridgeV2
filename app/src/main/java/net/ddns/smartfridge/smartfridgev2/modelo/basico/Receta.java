package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Clase que representa una menu_receta de la bbdd
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
     * Instantiates a new Receta.
     *
     * @param idReceta         the id receta
     * @param tituloReceta     the titulo receta
     * @param descripcion      the descripcion
     * @param tipoReceta       the tipo receta
     * @param tiempoReceta     the tiempo receta
     * @param dificultadReceta the dificultad receta
     * @param imagenReceta     the imagen receta
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
     * Instantiates a new Receta.
     *
     * @param idReceta         the id receta
     * @param tituloReceta     the titulo receta
     * @param descripcion      the descripcion
     * @param tipoReceta       the tipo receta
     * @param tiempo           the tiempo
     * @param dificultadReceta the dificultad receta
     * @param imagenReceta     the imagen receta
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
     * Sets id receta.
     *
     * @param idReceta the id receta
     */
    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
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
     * Sets titulo receta.
     *
     * @param tituloReceta the titulo receta
     */
    public void setTituloReceta(String tituloReceta) {
        this.tituloReceta = tituloReceta;
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
     * Sets descripcion.
     *
     * @param descripcion the descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
     * Sets tipo receta.
     *
     * @param tipoReceta the tipo receta
     */
    public void setTipoReceta(int tipoReceta) {
        this.tipoReceta = tipoReceta;
    }

    /**
     * Gets tiempo receta.
     *
     * @return the tiempo receta
     */
    public int getTiempoReceta() {
        return tiempoReceta;
    }

    /**
     * Sets tiempo receta.
     *
     * @param tiempoReceta the tiempo receta
     */
    public void setTiempoReceta(int tiempoReceta) {
        this.tiempoReceta = tiempoReceta;
    }

    /**
     * Gets dificultad receta.
     *
     * @return the dificultad receta
     */
    public int getDificultadReceta() {
        return dificultadReceta;
    }

    /**
     * Sets dificultad receta.
     *
     * @param dificultadReceta the dificultad receta
     */
    public void setDificultadReceta(int dificultadReceta) {
        this.dificultadReceta = dificultadReceta;
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
     * Sets tiempo.
     *
     * @param tiempo the tiempo
     */
    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    /**
     * Gets dificultad.
     *
     * @return the dificultad
     */
    public String getDificultad() {
        return dificultad;
    }

    /**
     * Sets dificultad.
     *
     * @param dificultad the dificultad
     */
    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
}
