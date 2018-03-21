package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase que representa una lista de la compra
 */
public class ListaCompra implements Serializable{
    private int id;//Representa el identificador único de la lista
    private String fecha;//Para almacenar la fecha en la que se creó la lista
    private ArrayList<ComponenteListaCompra> productos;//ArrayList para guardar los ítems de la lista de la compra


    /**
     * Constructor
     *
     * @param _fecha     fecha en la que se crea la lista
     * @param _productos ArrayList para guardar los productos que componen la lista de la compra
     */
//Otro constructor
    public ListaCompra(String _fecha, ArrayList<ComponenteListaCompra> _productos){
        this.fecha = _fecha;
        this.productos = _productos;
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
     * Gets fecha.
     *
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Sets fecha.
     *
     * @param fecha the fecha
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Gets productos.
     *
     * @return the productos
     */
    public ArrayList<ComponenteListaCompra> getProductos() {
        return productos;
    }

    /**
     * Sets productos.
     *
     * @param productos the productos
     */
    public void setProductos(ArrayList<ComponenteListaCompra> productos) {
        this.productos = productos;
    }
}
