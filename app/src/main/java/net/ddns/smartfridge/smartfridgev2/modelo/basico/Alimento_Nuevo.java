package net.ddns.smartfridge.smartfridgev2.modelo.basico;

/**
 * Clase que representa un alimento nuevo, creado a partir de insertar manualmente
 */

public class Alimento_Nuevo {
    private int id;//Representa el id del alimento que proviene de la tabla Alimentos de MiNevera(Foreign Key)
    private String nombre_ali_nuevo;//Representa el nombre del alimento
    private String fecha_alta;//Fecha en la que se insertó el alimento en la bbdd

    //Constructor 1
    public Alimento_Nuevo(String nombre_ali_nuevo, String fecha_alta, int id) {

        this.nombre_ali_nuevo = nombre_ali_nuevo;
        this.fecha_alta = fecha_alta;
        this.id = id;
    }

    //Constructor 2
    public Alimento_Nuevo(String nombre_ali_nuevo, String fecha_alta) {
        this.nombre_ali_nuevo = nombre_ali_nuevo;
        this.fecha_alta = fecha_alta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre_ali_nuevo() {
        return nombre_ali_nuevo;
    }

    public void setNombre_ali_nuevo(String nombre_ali_nuevo) {
        this.nombre_ali_nuevo = nombre_ali_nuevo;
    }

    public String getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }
}
