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
     * Gets id codigo.
     *
     * @return the id codigo
     */
    public int getId_codigo() {
        return id_codigo;
    }

    /**
     * Sets id codigo.
     *
     * @param id_codigo the id codigo
     */
    public void setId_codigo(int id_codigo) {
        this.id_codigo = id_codigo;
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
     * Sets nom alimento.
     *
     * @param nomAlimento the nom alimento
     */
    public void setNomAlimento(String nomAlimento) {
        this.nomAlimento = nomAlimento;
    }

    /**
     * Gets codigo barras.
     *
     * @return the codigo barras
     */
    public String getCodigo_barras() {
        return codigo_barras;
    }

    /**
     * Sets codigo barras.
     *
     * @param codigo_barras the codigo barras
     */
    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
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
