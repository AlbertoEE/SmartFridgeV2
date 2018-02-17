package net.ddns.smartfridge.smartfridgev2.modelo.basico;

/**
 * Clase que representa los productos con los precios por supermercado
 */

public class Precio {
    private String nombreProducto;//El nombre del producto
    private double pvp;//El precio del producto
    private String supermercado;//El nombre del supermercado al que corresponde dicho precio

    public Precio(String nombreProducto, double pvp, String supermercado) {
        this.nombreProducto = nombreProducto;
        this.pvp = pvp;
        this.supermercado = supermercado;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPvp() {
        return pvp;
    }

    public void setPvp(double pvp) {
        this.pvp = pvp;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }
}
