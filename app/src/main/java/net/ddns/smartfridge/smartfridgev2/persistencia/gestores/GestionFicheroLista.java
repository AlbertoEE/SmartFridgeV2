package net.ddns.smartfridge.smartfridgev2.persistencia.gestores;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Clase para gestionar los ficheros donde vamos a escribir y leer los objetos que representan
 * nuevas listas de la compra
 */
public class GestionFicheroLista {
    private ArrayList<ListaCompra>listas;//ArrayList para guardar todas las listas leidas del fichero
    private Context contexto;//Para almacenar el contexto del activity donde se va a instanciar la clase
    private ListaCompra lista;//Para leer cada objeto del fichero y trabajar con él

    /**
     * Instantiates a new Gestor fichero lista.
     *
     * @param context the context
     */
//Constructor
    public GestionFicheroLista(Context context){
        this.contexto = context;
    }

    /**
     * Escribir lista array list.
     *
     * @param l the l
     * @return the array list
     */
//Método para añadir los objetos de tipo ListaCompra al fichero
    public ArrayList<ListaCompra> escribirLista(ListaCompra l)  {
        listas = leerTodasListas();
        if(listas==null){
            listas = new ArrayList<ListaCompra>();
        }
        listas.add(l);
        FileOutputStream fos=null;
        ObjectOutputStream oos = null;
        try {
            fos = contexto.getApplicationContext().openFileOutput("Listas.obj", contexto.MODE_PRIVATE);
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
     * Leer todas listas array list.
     *
     * @return the array list
     */
//Método para leer el arraylist con las listas almacenado en el fichero
    public ArrayList<ListaCompra> leerTodasListas(){
        listas = new ArrayList<ListaCompra>();
        //BufferedReader br=null;
        ObjectInputStream ois=null;
        FileInputStream fis = null;
        try {
            fis = contexto.getApplicationContext().openFileInput("Listas.obj");
            ois = new ObjectInputStream(fis);
            //Leemos cada objeto y lo almacenamos en la variable
            //lista = (ListaCompra)ois.readObject();
            while (true) {
                listas = (ArrayList<ListaCompra>) ois.readObject();
            }
            //Log.d("listaTotalFinal", "longitud de array leido en fichero: " + listas.size());
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
     * Actualizar listas.
     *
     * @param alc the alc
     */
//Método para actualizar el fichero con las listas desde TodasListasActivity
    public void actualizarListas(ArrayList<ListaCompra> alc){
        FileOutputStream fos=null;
        ObjectOutputStream oos = null;
        try {
            fos = contexto.getApplicationContext().openFileOutput("Listas.obj", contexto.MODE_PRIVATE);
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
