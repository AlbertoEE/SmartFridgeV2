package net.ddns.smartfridge.smartfridgev2.modelo.basico;

/**
 * Clase que representa un tipo de menu_receta
 */
public class Tipo {
    private int id;//Representa el identificador del tipo
    private String descripcion;//Describe el tipo que es, le da nombre

    /**
     * Instantiates a new Tipo.
     *
     * @param id          the id
     * @param descripcion the descripcion
     */
    public Tipo(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
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
     *
     *
     *
     * @param descripcion the descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
