package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import java.io.Serializable;

/**
 * Clase que representa los productos con los precios por supermercado
 */
public class Precio implements Serializable {
    private String nombreProducto;//El nombre del producto
    private double pvp;//El precio del producto
    private String supermercado;//El nombre del supermercado al que corresponde dicho precio

    /**
     * Instantiates a new Precio.
     *
     * @param nombreProducto the nombre producto
     * @param pvp            the pvp
     * @param supermercado   the supermercado
     */
    public Precio(String nombreProducto, double pvp, String supermercado) {
        this.nombreProducto = nombreProducto;
        this.pvp = pvp;
        this.supermercado = supermercado;
    }

    /**
     * Gets nombre producto.
     *
     * @return the nombre producto
     */
    public String getNombreProducto() {
        return nombreProducto;
    }

    /**
     * Sets nombre producto.
     *
     * @param nombreProducto the nombre producto
     */
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    /**
     * Gets pvp.
     *
     * @return the pvp
     */
    public double getPvp() {
        return pvp;
    }

    /**
     * Sets pvp.
     *
     * @param pvp the pvp
     */
    public void setPvp(double pvp) {
        this.pvp = pvp;
    }

    /**
     * Gets supermercado.
     *
     * @return the supermercado
     */
    public String getSupermercado() {
        return supermercado;
    }

    /**
     * Sets supermercado.
     *
     * @param supermercado the supermercado
     */
    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }
}
