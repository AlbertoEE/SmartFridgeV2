package net.ddns.smartfridge.smartfridgev2.persistencia.gestores;

import android.content.Context;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Clase para gestionar los ficheros donde vamos a escribir y leer los objetos que representan
 * nuevas listas de la compra
 */
public class GestionFicheroLista {
    private ArrayList<ListaCompra>listas;//ArrayList para guardar todas las listas leidas del fichero
    private Context contexto;//Para almacenar el contexto del activity donde se va a instanciar la clase
    private String nombreFichero="Listas.obj";//Nombre del fichero

    /**
     * Constructor
     *
     * @param context el Contexto de la Activity
     */
    public GestionFicheroLista(Context context){
        this.contexto = context;
    }

    /**
     * Método para añadir los objetos de tipo ListaCompra al fichero
     *
     * @param l Objeto ListaCompra que contiene los datos que componen una lista de la compra
     * @return ArrayList con todas las listas de la compra que hay
     */
    public ArrayList<ListaCompra> escribirLista(ListaCompra l)  {
        listas = leerTodasListas();
        if(listas==null){
            listas = new ArrayList<ListaCompra>();
        }
        listas.add(l);
        FileOutputStream fos=null;
        ObjectOutputStream oos = null;
        try {
            fos = contexto.getApplicationContext().openFileOutput(nombreFichero, contexto.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            //Escribimos el objeto en el fichero
            oos.writeObject(listas);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Error al guardar la lista, vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                //Cerramos los streams y el fichero
                fos.close();
                oos.close();
            } catch (IOException e) {
                Toast.makeText(contexto, "Error al guardar la lista, vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
            }
        }
        return listas;
    }

    /**
     * Método para leer el arraylist con las listas almacenado en el fichero
     *
     * @return ArrayList con todas las listas de la compra que hay en el fichero
     */
    public ArrayList<ListaCompra> leerTodasListas(){
        listas = new ArrayList<ListaCompra>();
        ObjectInputStream ois=null;
        FileInputStream fis = null;
        try {
            fis = contexto.getApplicationContext().openFileInput(nombreFichero);
            ois = new ObjectInputStream(fis);
            //Leemos cada objeto y lo almacenamos en la variable
            //lista = (ListaCompra)ois.readObject();
            while (true) {
                listas = (ArrayList<ListaCompra>) ois.readObject();
            }
        } catch (NullPointerException e){
            //Si el fichero no existe, directamente escribimos en él
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fis.close();
                ois.close();
            } catch (NullPointerException ex){
                //No hacemos nada, puesto que el fichero no está creado
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listas;
    }

    /**
     * Método para actualizar el fichero con las listas desde TodasListasActivity
     *
     * @param alc ArrayList con todas las listas de la compra que hay en el fichero antes de actualizar
     */
    public void actualizarListas(ArrayList<ListaCompra> alc){
        FileOutputStream fos=null;
        ObjectOutputStream oos = null;
        try {
            fos = contexto.getApplicationContext().openFileOutput(nombreFichero, contexto.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            //Escribimos el objeto en el fichero
            oos.writeObject(alc);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Error al guardar la lista, vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                //Cerramos los streams y el fichero
                fos.close();
                oos.close();
            } catch (IOException e) {
                Toast.makeText(contexto, "Error al guardar la lista, vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
