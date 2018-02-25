package net.ddns.smartfridge.smartfridgev2.modelo.basico;

/**
 * Clase que representa un tipo de menu_receta
 */

public class Tipo {
    private int id;//Representa el identificador del tipo
    private String descripcion;//Describe el tipo que es, le da nombre

    public Tipo(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
