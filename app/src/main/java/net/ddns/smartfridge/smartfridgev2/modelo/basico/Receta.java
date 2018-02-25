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

    public Receta(int idReceta, String tituloReceta, String descripcion, int tipoReceta, int tiempoReceta, int dificultadReceta, Bitmap imagenReceta) {
        this.idReceta = idReceta;
        this.tituloReceta = tituloReceta;
        this.descripcion = descripcion;
        this.tipoReceta = tipoReceta;
        this.tiempoReceta = tiempoReceta;
        this.dificultadReceta = dificultadReceta;
        this.imagenReceta = imagenReceta;
    }

    public Receta(int idReceta, String tituloReceta, String descripcion, int tipoReceta, String tiempo, String dificultadReceta, Bitmap imagenReceta) {
        this.idReceta = idReceta;
        this.tituloReceta = tituloReceta;
        this.descripcion = descripcion;
        this.tipoReceta = tipoReceta;
        this.tiempo = tiempo;
        this.dificultad = dificultadReceta;
        this.imagenReceta = imagenReceta;
    }


    public int getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(int idReceta) {
        this.idReceta = idReceta;
    }

    public String getTituloReceta() {
        return tituloReceta;
    }

    public void setTituloReceta(String tituloReceta) {
        this.tituloReceta = tituloReceta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTipoReceta() {
        return tipoReceta;
    }

    public void setTipoReceta(int tipoReceta) {
        this.tipoReceta = tipoReceta;
    }

    public int getTiempoReceta() {
        return tiempoReceta;
    }

    public void setTiempoReceta(int tiempoReceta) {
        this.tiempoReceta = tiempoReceta;
    }

    public int getDificultadReceta() {
        return dificultadReceta;
    }

    public void setDificultadReceta(int dificultadReceta) {
        this.dificultadReceta = dificultadReceta;
    }

    public Bitmap getImagenReceta() {
        return imagenReceta;
    }

    public void setImagenReceta(Bitmap imagenReceta) {
        this.imagenReceta = imagenReceta;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
}
