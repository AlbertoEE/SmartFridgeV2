package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import java.io.Serializable;

/**
 * Clase que representa un item de la lista de la compra
 */
public class ComponenteListaCompra implements Serializable{
    private int id;//El id que tiene el elemento
    private String nombreElemento;//El nombre del elemento
    private int tipo;//Nos va a decir de dónde proviene ese alimento
    /**
     * Constante que es un array de int donde se representa la procedencia del alimento que forma parte de la lista de la compra
     */
    public static final int[]TIPOS = {1,2,3};//Si es de tipo 1, proviene de la bbdd de MiNevera, si es de tipo 2 proviene de SF y si es de tipo 3 es manual

    /**
     * Constructor
     *
     * @param id             id del alimento
     * @param nombreElemento nombre del alimento
     * @param tipo           tipo del alimento para indicar de dónde procede
     */
    public ComponenteListaCompra(int id, String nombreElemento, int tipo) {
        this.id = id;
        this.nombreElemento = nombreElemento;
        this.tipo = tipo;
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
     * Gets nombre elemento.
     *
     * @return the nombre elemento
     */
    public String getNombreElemento() {
        return nombreElemento;
    }

    /**
     * Sets nombre elemento.
     *
     * @param nombreElemento the nombre elemento
     */
    public void setNombreElemento(String nombreElemento) {
        this.nombreElemento = nombreElemento;
    }

    /**
     * Gets tipo.
     *
     * @return the tipo
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * Sets tipo.
     *
     * @param tipo the tipo
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object object) {
        boolean iguales = false;

        if (object != null && object instanceof ComponenteListaCompra)
        {
            iguales = this.nombreElemento == ((ComponenteListaCompra) object).nombreElemento;
        }

        return iguales;
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result = 31 * result + (this.nombreElemento == null ? 0 : this.nombreElemento.hashCode());

        return result;
    }
}
