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

    public Alimento_Codigo(int id_codigo, String nomAlimento, String codigo_barras, Bitmap imagen) {
        this.id_codigo = id_codigo;
        this.nomAlimento = nomAlimento;
        this.codigo_barras = codigo_barras;
        this.imagen = imagen;
    }

    public int getId_codigo() {
        return id_codigo;
    }

    public void setId_codigo(int id_codigo) {
        this.id_codigo = id_codigo;
    }

    public String getNomAlimento() {
        return nomAlimento;
    }

    public void setNomAlimento(String nomAlimento) {
        this.nomAlimento = nomAlimento;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
