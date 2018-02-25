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

    public Ingrediente(int idIngrediente, String nombreIngrediente, Bitmap imagenIngrediente, String clasificacion_compra) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.imagenIngrediente = imagenIngrediente;
        this.clasificacion_compra = clasificacion_compra;
    }

    public Ingrediente(int idIngrediente, String nombreIngrediente, Bitmap imagenIngrediente) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.imagenIngrediente = imagenIngrediente;
    }

    public Ingrediente(int idIngrediente, String nombreIngrediente) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
    }

    public Ingrediente(int idIngrediente, String nombreIngrediente, int cantidad) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.cantidad = cantidad;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(int idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNombreIngrediente() {
        return nombreIngrediente;
    }

    public void setNombreIngrediente(String nombreIngrediente) {
        this.nombreIngrediente = nombreIngrediente;
    }

    public Bitmap getImagenIngrediente() {
        return imagenIngrediente;
    }

    public void setImagenIngrediente(Bitmap imagenIngrediente) {
        this.imagenIngrediente = imagenIngrediente;
    }

    public String getClasificacion_compra() {
        return clasificacion_compra;
    }

    public void setClasificacion_compra(String clasificacion_compra) {
        this.clasificacion_compra = clasificacion_compra;
    }
}
