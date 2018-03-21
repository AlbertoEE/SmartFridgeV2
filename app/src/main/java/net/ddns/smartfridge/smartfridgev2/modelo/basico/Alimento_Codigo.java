package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Clase que representa un alimento de la bbdd de mysql
 */
public class Alimento_Codigo implements Serializable{
    private int id_codigo;//Id del alimento
    private String nomAlimento;//Nombre del alimento
    private String codigo_barras;//Representa el código de barras del alimento
    private Bitmap imagen;//Imagen del alimento

    /**
     * Instantiates a new Alimento codigo.
     *
     * @param id_codigo     id del alimento
     * @param nomAlimento   nombre del alimento
     * @param codigo_barras codigo de barras del alimento
     * @param imagen        imagen del alimento
     */
    public Alimento_Codigo(int id_codigo, String nomAlimento, String codigo_barras, Bitmap imagen) {
        this.id_codigo = id_codigo;
        this.nomAlimento = nomAlimento;
        this.codigo_barras = codigo_barras;
        this.imagen = imagen;
    }

    /**
     * Gets nom alimento.
     *
     * @return the nom alimento
     */
    public String getNomAlimento() {
        return nomAlimento;
    }

    /**
     * Gets imagen.
     *
     * @return the imagen
     */
    public Bitmap getImagen() {
        return imagen;
    }

    /**
     * Sets imagen.
     *
     * @param imagen the imagen
     */
    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
