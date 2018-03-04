package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import android.graphics.Bitmap;

/**
 * Clase que representa un ingrediente de la bbdd
 */
public class Ingrediente {
    private int idIngrediente;//El identificador del ingrediente
    private String nombreIngrediente;//El nombre del ingrediente
    private Bitmap imagenIngrediente;//La imagen del ingrediente
    private String clasificacion_compra;//Identificador de la clasificación cuando forma parte de la lista de la compra
    private int cantidad;//Para guardar la cantidad de un ingrediente dado en una menu_receta

    /**
     * Constructor
     *
     * @param idIngrediente        id del ingrediente en la bbdd
     * @param nombreIngrediente    nombre del ingrediente
     * @param imagenIngrediente    imagen del ingrediente
     * @param clasificacion_compra clasificación del ingrediente
     */
    public Ingrediente(int idIngrediente, String nombreIngrediente, Bitmap imagenIngrediente, String clasificacion_compra) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.imagenIngrediente = imagenIngrediente;
        this.clasificacion_compra = clasificacion_compra;
    }

    /**
     * Constructor
     *
     * @param idIngrediente     id del ingrediente en la bbdd
     * @param nombreIngrediente nombre del ingrediente
     * @param imagenIngrediente imagen del ingrediente
     */
    public Ingrediente(int idIngrediente, String nombreIngrediente, Bitmap imagenIngrediente) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.imagenIngrediente = imagenIngrediente;
    }

    /**
     * Constructor
     *
     * @param idIngrediente     id del ingrediente en la bbdd
     * @param nombreIngrediente nombre del ingrediente
     */
    public Ingrediente(int idIngrediente, String nombreIngrediente) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
    }

    /**
     * Constructor
     *
     * @param idIngrediente     id del ingrediente en la bbdd
     * @param nombreIngrediente nombre del ingrediente
     * @param cantidad          cantidad del ingrediente
     */
    public Ingrediente(int idIngrediente, String nombreIngrediente, int cantidad) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.cantidad = cantidad;
    }

    /**
     * Gets id ingrediente.
     *
     * @return the id ingrediente
     */
    public int getIdIngrediente() {
        return idIngrediente;
    }

    /**
     * Sets id ingrediente.
     *
     * @param idIngrediente the id ingrediente
     */
    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    /**
     * Gets nombre ingrediente.
     *
     * @return the nombre ingrediente
     */
    public String getNombreIngrediente() {
        return nombreIngrediente;
    }

    /**
     * Sets nombre ingrediente.
     *
     * @param nombreIngrediente the nombre ingrediente
     */
    public void setNombreIngrediente(String nombreIngrediente) {
        this.nombreIngrediente = nombreIngrediente;
    }

    /**
     * Gets imagen ingrediente.
     *
     * @return the imagen ingrediente
     */
    public Bitmap getImagenIngrediente() {
        return imagenIngrediente;
    }

    /**
     * Sets imagen ingrediente.
     *
     * @param imagenIngrediente the imagen ingrediente
     */
    public void setImagenIngrediente(Bitmap imagenIngrediente) {
        this.imagenIngrediente = imagenIngrediente;
    }

    /**
     * Gets clasificacion compra.
     *
     * @return the clasificacion compra
     */
    public String getClasificacion_compra() {
        return clasificacion_compra;
    }

    /**
     * Sets clasificacion compra.
     *
     * @param clasificacion_compra the clasificacion compra
     */
    public void setClasificacion_compra(String clasificacion_compra) {
        this.clasificacion_compra = clasificacion_compra;
    }
}
