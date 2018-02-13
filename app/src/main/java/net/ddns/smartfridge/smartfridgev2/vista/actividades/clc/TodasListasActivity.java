package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import net.ddns.smartfridge.smartfridgev2.BuildConfig;
import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapterListas;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Fecha;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.ListaCompraDB;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class TodasListasActivity extends AppCompatActivity {
    private CustomRecyclerViewAdapterListas adapter;
    private RecyclerView recyclerView;
    private ArrayList<ListaCompra> listas;
    private Paint p = new Paint();
    private Intent intent;
    private ListaCompraDB listaCompraDB;//Para trabajar con la bbdd de datos y la tabla de las listas
    private ArrayList<Integer> ids;//Para almacenar los ids de la tabla listas
    private ArrayList<Integer> idsCopia;//Para almacenar los ids de la tabla listas
    //private ArrayList<Lista> productosInterna;//Para almacenar los componentes de cada compra almacenada en la bbdd
    private CopyOnWriteArrayList<ComponenteListaCompra> productosExterna;//Para almacenar los componentes de cada compra almacenada en la bbdd
    private CopyOnWriteArrayList<ComponenteListaCompra> productosManual;//Para almacenar los componentes de cada compra almacenada en la bbdd
    private ListaCompra lista;//Para generar cada objeto de tipo ListaCompra
    private String fechaLista;//Para saber la fecha de una lista
    private Fecha fecha;//Para cambiar el formato de la fecha que recibimos de la bbdd
    private ArrayList<ListaCompra> todasLasListas = new ArrayList<ListaCompra>();//Array con todas las listas de la compra que hay en la bbdd
    private static final int MAX_AVAILABLE = 1;//Para la construcción del semáforo, nº de hilos
    //private CopyOnWriteArrayList<CopyOnWriteArrayList>todosLosProductos = new CopyOnWriteArrayList<>();//Para crear la lista con todos los productos
    //private ArrayList<Lista>todosLosProductos = new ArrayList<>();//Para crear la lista con todos los productos


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todas_listas);
        todasLasListas = NuevaListaActivity.getTodasLasListas();
       /* listaCompraDB = new ListaCompraDB(this);
        fecha = new Fecha();
        //Recogemos todas las listas que hay en la bbdd
        ids = listaCompraDB.recuperarIdListas();
        Log.d("fecha2", "longitud array: " + ids.size());
        productosInterna = new ArrayList<Lista>();
        productosExterna = new CopyOnWriteArrayList<ComponenteListaCompra>();
        productosManual = new CopyOnWriteArrayList<>();
        productosInterna = listaCompraDB.recuperarComponentesListaInterna();

        todosLosProductos = listaCompraDB.recuperarComponentesListaExterna(productosInterna);

        for(Lista l : todosLosProductos){
            CopyOnWriteArrayList<ComponenteListaCompra> a = l.getProductos();
            for (ComponenteListaCompra c : a){
                int contador =1;
                Log.d("lista2", "id: " + l.getId() + " componentes: " + c.getNombreElemento() + "contador: " + contador);
                contador++;
            }
        }*/

        //productosManual = listaCompraDB.recuperarComponentesListaManual();
        /*
        for(int i=0;i<productosInterna.size();i++){
            todosLosProductos.add(productosInterna.get(i));
        }
        for(int i=0;i<productosExterna.size();i++){
            todosLosProductos.add(productosExterna.get(i));
        }
        for(int i=0;i<productosManual.size();i++){
            todosLosProductos.add(productosManual.get(i));
        }
        Log.d("lista", "Total productos: " + todosLosProductos.size());
        for(int i=0;i<todosLosProductos.size();i++){
            Log.d("lista", "Elementos de la lista: "+todosLosProductos.get(i).getNombreElemento());
        }*/
        //int n = ids.get(0);
  /*      try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //Recuperamos los componentes de cada id, es decir, de cada lista y los guardamos en un objeto de tipo lista
        //for (int n : idsCopia) {
            //Cogemos el ArrayList con todos los productos que componen la lista
            //productos = listaCompraDB.recuperarComponentesLista(n);
            //productos = listaCompraDB.recuperarComponentesListaInterna();
            //Log.d("fecha2", "producto: " + productos.get(2).getNombreElemento());
            /*Cogemos también la fecha de creación de la lista y la pasamos a su formato sin horas
            fechaLista = fecha.fechaCorta(listaCompraDB.obtenerFechaLista(n));
            lista = new ListaCompra(n, fechaLista, productos);
            //Guardamos todas las listas en un ArrayList que será el que usemos para mostrarlo
            todasLasListas.add(lista);
            Log.d("fecha2", "total listas: " + todasLasListas.size());
        /*    ListIterator<Integer> li = ids.listIterator();
            while( li.hasNext() ){
                productos = listaCompraDB.recuperarComponentesLista(n);
            }*/
            //Log.d("fecha2", "valor de n: " + n);
        //}
        //semaphore.release();
        cargarRecyclerView();
    }

    private void cargarRecyclerView(){
        listas = new ArrayList<>();

        adapter = new CustomRecyclerViewAdapterListas(todasLasListas);
        recyclerView = (RecyclerView) findViewById(R.id.rvListas);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        cargarSwipe();
    }

    private void cargarSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                   adapter.removeItem(position);
                } else {
                    intent = new Intent(getApplicationContext(), MostrarProductosListaActivity.class);
                    intent.putExtra("ListaProductos", adapter.getLista(position));
                    startActivity(intent);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_details_white_18dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white_18dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
