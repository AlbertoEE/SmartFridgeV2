package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.util.concurrent.AtomicDoubleArray;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomArrayAdapterNuevaLista;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Fecha;
import net.ddns.smartfridge.smartfridgev2.persistencia.GestorSharedP;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.ListaCompraDB;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.DialogActivity;

import java.util.ArrayList;
import java.util.Map;

public class NuevaListaActivity extends AppCompatActivity {
    private Intent intent;//Para trabajar con los intents para lanzar nuevos activitys
    private String alimentoNuevo;//Para recoger el dato introducido por el usuario en el dialog
    private Context context;//Para indicar el contexto del activity
    private ArrayList<String> listaAlimentosCompra;//ArrayList que lleva el nombre de los alimentos que se muestran en la lista
    private ListaCompra listaNueva;//Para trabajar con el objeto ListaCompra
    private Fecha fecha;//Para usar los métodos para obtener la fecha de hoy
    private ArrayList<ComponenteListaCompra> alimentosLeidosSP;//Para leer los aliemntos que hay en el SP almacenados
    private int elementos;//Para contar el número de elementos que hay en el SP
    private GestorSharedP gsp;//Instancia de la clase para trabajar con el SharedPreferences
    private ListaCompraDB listaCompraDB;//Para utilizar los métodos de persistencia del módulo de lista de la compra
    private ComponenteListaCompra componente;//Para crear los items que van a ir en la lista
    private int id_alimento_manual;//Para guardar el id que hay en la bbdd asociado al alimento que metemos de manera manual
    ////////////////////////////
    private ArrayList<ComponenteListaCompra> a = new ArrayList<ComponenteListaCompra>();
    /////////////////////////////
    private CustomArrayAdapterNuevaLista adapter;
    private ListView listView;
    private boolean editando = false;
    private static final int REQUEST_CODE_ALIMENTOS_SUGERIDOS = 357;
    private ArrayList<ComponenteListaCompra> componenteListaCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_lista);
        gsp = new GestorSharedP();
        listaCompraDB = new ListaCompraDB(this);
        cargarAdapter();
        //SharedPreferences mysp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Fijamos el contexto del activity
        context = this;
        //Instanciamos el arraylist
        listaAlimentosCompra = new ArrayList<String>();
        alimentosLeidosSP = new ArrayList<ComponenteListaCompra>();
        //Comprobamos si hay algún alimento almacenado en el SP para notificárselo al usuario
        elementos = gsp.productosAlmacenados();
        if(gsp.isHayElemento()){
            //Mostramos la lista indicándo que hay elementos y cuáles quiere añadir a la lista
            alimentosLeidosSP = gsp.recogerValores();
            intent = new Intent(this, SugerenciaDeAlimentoActivity.class);
            //intent.putStringArrayListExtra("AlimentosSugeridos", alimentosLeidosSP);
            intent.putExtra("AlimentosSugeridos", alimentosLeidosSP);
            startActivityForResult(intent, REQUEST_CODE_ALIMENTOS_SUGERIDOS);
            //borrarSP();
        }
        //Cogemos la referencia a los floating action buttons
        com.getbase.floatingactionbutton.FloatingActionButton botonManual = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.manual);
        com.getbase.floatingactionbutton.FloatingActionButton botonAlimentos = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.anadirAlimentos);
        com.getbase.floatingactionbutton.FloatingActionButton botonEditar = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.editar);
        com.getbase.floatingactionbutton.FloatingActionButton botonAceptar = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.aceptar);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        //Le asignamos el listener a cada botón
        botonManual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Cuando pulsemos el botón, nos da la opción de añadir alimentos de manera manual
                    //Mensaje del Alert
                    builder.setMessage("Introduzca el elemento que quiere añadir a la lista:");
                    //Título
                    builder.setTitle("Añadir manualmente");
                    //Añadimos el layout que hemos creado
                    //builder.setView(inflater.inflate(R.layout.dialognewfood, null));
                    final EditText input = new EditText(context);
                    builder.setView(input);
                    //Añadimos los botones
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Asignamos el valor introducido a la variable
                            alimentoNuevo =  input.getText().toString();

                            //Lo añadimos a la bbdd
                            listaCompraDB.insertarAlimentoManual(alimentoNuevo);
                            //Leemos el id de ese objeto
                            id_alimento_manual = listaCompraDB.getIdAlimento(alimentoNuevo);
                            //Creamos el objeto que va a ser añadido a la vista de la lista
                            componente = new ComponenteListaCompra(id_alimento_manual, alimentoNuevo,3);
                            //Lo añadimos al adapter
                            adapter.addProducto(componente);
                            Log.d("alimento", "alimento: " + alimentoNuevo + ", id: " + id_alimento_manual);
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //No hacemos nada
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        botonAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cuando pulsemos el botón, se va a abrir el activity con todos los alimentos
                /*intent = new Intent(this, ListadoAlimentos.class);
                startActivity(intent);
                 */
            }
        });
        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editando = !editando;
                if(editando){
                    adapter.mostrarCheckboxes();
                }else{
                    adapter.ocultarrCheckboxes();
                }
            }
        });
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cuando pulsemos el botón, se va a guardar la lista
                a=new ArrayList<>();
                a = crearArray();
                //Creamos el nuevo objeto Lista
                fecha = new Fecha();
                listaNueva = new ListaCompra(fecha.fechaActualCompleta(), a);
                //Guardamos los datos de la lista en la bbdd
                listaCompraDB.insertarListaCompra(listaNueva);
                Log.d("guardarLista", "Lista guardada");
                int id = listaCompraDB.getIdLista(fecha.fechaActualCompleta());
                Log.d("guardarLista", "id: " + id);
                insertarComponentesLista(a, id);
                Toast.makeText(context, "Se ha guardado una nueva lista con fecha " + fecha.fechaActual(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Método para crear el objeto lista
    public void crearListaNueva(View v){
        /*Recogemos los elementos de la lista y los almacenamos en un array
        listaAlimentosCompra = customAdaptador.getArray();
        listaNueva.setProductos(listaAlimentosCompra);
         */
    }

    //En el onDestroy cerramos la bbdd

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listaCompraDB.cerrarConexion();
    }

    //Método para hacer los insert en las tablas correspondientes
    public void insertarComponentesLista(ArrayList<ComponenteListaCompra> a, int idLista){
        int tipo;//Para guardar el tipo de cada objeto del arrayList
        //Recorremos el ArrayList
        for (ComponenteListaCompra c : a){
            tipo = c.getTipo();
            Log.d("tipo", "Tipo: " + tipo);
            switch (tipo){
                case 1:
                    listaCompraDB.insertComponenteInterno(c, idLista);
                    Log.d("tipo", "Entra por el 1: " + c.getNombreElemento() + " con tipo " + c.getTipo());
                    break;
                case 2:
                    listaCompraDB.insertComponenteExterno(c, idLista);
                    Log.d("tipo", "Entra por el 2: " + c.getNombreElemento() + " con tipo " + c.getTipo());
                    break;
                case 3:
                    listaCompraDB.insertComponenteManual(c, idLista);
                    Log.d("tipo", "Entra por el 3: " + c.getNombreElemento() + " con tipo " + c.getTipo());
            }
        }
    }

    //Método para crear un arrayList ficticio con datos, luego BORRAR!!!!!!!!!!!!
    public ArrayList<ComponenteListaCompra> crearArray(){
        ComponenteListaCompra c = new ComponenteListaCompra(1, "patata", 1);
        ComponenteListaCompra c1 = new ComponenteListaCompra(2, "tomate", 1);
        ComponenteListaCompra c2 = new ComponenteListaCompra(3, "lechuga", 1);
        ComponenteListaCompra c3 = new ComponenteListaCompra(1, "nata", 2);
        ComponenteListaCompra c4 = new ComponenteListaCompra(2, "mantequilla", 2);
        ComponenteListaCompra c5 = new ComponenteListaCompra(3, "azúcar", 2);
        ComponenteListaCompra c6 = new ComponenteListaCompra(1, "chorizo", 3);
        ComponenteListaCompra c7 = new ComponenteListaCompra(2, "salchichón", 3);
        ComponenteListaCompra c8 = new ComponenteListaCompra(3, "jamón serrano", 3);
        a.add(c);
        a.add(c1);
        a.add(c2);
        a.add(c3);
        a.add(c4);
        a.add(c5);
        a.add(c6);
        a.add(c7);
        a.add(c8);
        return a;
    }

    private void cargarAdapter(){
        if(alimentosLeidosSP != null){
            adapter = new CustomArrayAdapterNuevaLista(this, alimentosLeidosSP);
        } else {
            adapter = new CustomArrayAdapterNuevaLista(this, new ArrayList<ComponenteListaCompra>());
        }

        Log.d("elputo", "cargarAdapter: " + crearArray());
        Log.d("elputo", "cargarAdapter: " + adapter);
        listView = (ListView)findViewById(R.id.lvNuevaLista);
        Log.d("elputo", "cargarAdapter: " + listView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ALIMENTOS_SUGERIDOS){
            componenteListaCompras = new ArrayList<>();
            componenteListaCompras = (ArrayList<ComponenteListaCompra>) data.getExtras().getSerializable("AlimentosSeleccionados");
            cargarAdapter();
        }
    }


}

