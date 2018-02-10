package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import java.io.Serializable;

/**
 * Clase que representa un item de la lista de la compra
 */

public class ComponenteListaCompra implements Serializable{
    private int id;//El id que tiene el elemento
    private String nombreElemento;//El nombre del elemento
    private int tipo;//Nos va a decir de dónde proviene ese alimento
    public static final int[]TIPOS = {1,2,3};//Si es de tipo 1, proviene de la bbdd de MiNevera, si es de tipo 2 proviene de SF y si es de tipo 3 es manual

    //Constructor
    public ComponenteListaCompra(int id, String nombreElemento, int tipo) {
        this.id = id;
        this.nombreElemento = nombreElemento;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreElemento() {
        return nombreElemento;
    }

    public void setNombreElemento(String nombreElemento) {
        this.nombreElemento = nombreElemento;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
