package net.ddns.smartfridge.smartfridgev2.modelo.basico;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Clase que representa un alimento de la bbdd de sqlite
 */

public class Alimento implements Serializable {
    private int id;//Id del alimento
    private String nombreAlimento;//Nombre del alimento
    private int cantidad;//Número de unidades del alimento almacenadas
    private String fecha_registro;//Fecha en la que se registró el alimento
    private String fecha_caducidad;//Fecha de caducidad del aliemnto
    private int dias_caducidad;//Dias que faltan para que el alimento caduque
    private Bitmap imagen;//Imagen del alimento

    //Constructor 1
    public Alimento(int id, String nombreAlimento, int cantidad, int dias_caducidad, String fecha_registro, String fecha_caducidad, Bitmap imagen) {
        this.id = id;
        this.nombreAlimento = nombreAlimento;
        this.cantidad = cantidad;
        this.dias_caducidad = dias_caducidad;
        this.fecha_registro = fecha_registro;
        this.fecha_caducidad = fecha_caducidad;
        this.imagen = imagen;
    }

    //Constructor 2
    public Alimento(String nombreAlimento, int cantidad, int dias_caducidad, String fecha_registro, String fecha_caducidad, Bitmap imagen) {
        this.nombreAlimento = nombreAlimento;
        this.cantidad = cantidad;
        this.fecha_registro = fecha_registro;
        this.fecha_caducidad = fecha_caducidad;
        this.dias_caducidad = dias_caducidad;
        this.imagen = imagen;
    }

    //Constructor que recibe como parámetros el nombre y la imagen
    public Alimento (String nombreA, Bitmap bm){
        this.nombreAlimento = nombreA;
        this.imagen = bm;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreAlimento() {
        return nombreAlimento;
    }

    public void setNombreAlimento(String nombreAlimento) {
        this.nombreAlimento = nombreAlimento;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getFecha_caducidad() {
        return fecha_caducidad;
    }

    public void setFecha_caducidad(String fecha_caducidad) {
        this.fecha_caducidad = fecha_caducidad;
    }

    public int getDias_caducidad() {
        return dias_caducidad;
    }

    public void setDias_caducidad(int dias_caducidad) {
        this.dias_caducidad = dias_caducidad;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
