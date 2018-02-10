package net.ddns.smartfridge.smartfridgev2.modelo.basico;

/**
 * Clase que representa un item de la lista de la compra
 */

public class ComponenteListaCompra {
    private int id;//El id que tiene el elemento
    private String nombreElemento;//El nombre del elemento
    private int tipo;//Nos va a decir de dónde proviene ese alimento
    public static final int[]TIPOS = {1,2,3};//Si es de tipo 1, proviene de la bbdd de MiNevera, si es de tipo 2 proviene de SF y si es de tipo 3 es manual
}
