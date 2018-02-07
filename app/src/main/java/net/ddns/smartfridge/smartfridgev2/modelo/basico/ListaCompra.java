package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import java.util.ArrayList;

/**
 * Clase que representa una lista de la compra
 */

public class ListaCompra {
    private int id;//Representa el identificador único de la lista
    private String fecha;//Para almacenar la fecha en la que se creó la lista
    private ArrayList<String> productos;//ArrayList para guardar los ítems de la lista de la compra

    public ListaCompra() {
    }

    //Constructor
    public ListaCompra(int _id, String _fecha, ArrayList<String> _productos){
        this.id = _id;
        this.fecha = _fecha;
        this.productos = _productos;
    }

    //Otro constructor
    public ListaCompra(String _fecha, ArrayList<String> _productos){
        this.fecha = _fecha;
        this.productos = _productos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<String> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<String> productos) {
        this.productos = productos;
    }
}
