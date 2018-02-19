package net.ddns.smartfridge.smartfridgev2.modelo.basico;

/**
 * Clase que representa una receta de la bbdd
 */

public class Receta {
    private int idReceta;//El identificador de la receta
    private String tituloReceta;//El título de la receta
    private String descripcion;//Descripción de cómo se hace la receta
    private int tipoReceta;//Clasificación de la receta por categoría de alimento
    private int tiempoReceta;//Clasificación de la receta por el tiempo de preparación
    private int dificultadReceta;//Clasificación de la receta en función de la dificultad de elaboración

    public Receta(int idReceta, String tituloReceta, String descripcion, int tipoReceta, int tiempoReceta, int dificultadReceta) {
        this.idReceta = idReceta;
        this.tituloReceta = tituloReceta;
        this.descripcion = descripcion;
        this.tipoReceta = tipoReceta;
        this.tiempoReceta = tiempoReceta;
        this.dificultadReceta = dificultadReceta;
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
}
