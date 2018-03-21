package net.ddns.smartfridge.smartfridgev2.vista.actividades.clc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.BuildConfig;
import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapterListas;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Fecha;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.GestionFicheroLista;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.ListaCompraDB;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Activity para mostrar todas las listas de la compra que hay almacenadas
 */
public class TodasListasActivity extends AppCompatActivity {
    private CustomRecyclerViewAdapterListas adapter;
    private RecyclerView recyclerView;
    private ArrayList<ListaCompra> listas;//ArrayList con todas las listas del adapter
    private Paint p = new Paint();
    private Intent intent;
    private GestionFicheroLista gfl;//Para leer del fichero las listas
    private ListaCompra lista;//Para generar cada objeto de tipo ListaCompra
    private ArrayList<ListaCompra> todasLasListas = new ArrayList<>();//Array con todas las listas de la compra que hay en la bbdd
    private int sort = 1;
    private TodasListasActivity todasListasActivity;
    private Dialogos dialogos;

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
        gfl = new GestionFicheroLista(this);
        //todasLasListas = NuevaListaActivity.getTodasLasListas();
        todasLasListas = gfl.leerTodasListas();
        Log.d("listaTotalFinal", "tamaño: " + todasLasListas.size());
        for (int i=0; i<todasLasListas.size(); i++){
            lista = todasLasListas.get(i);
            Log.d("listaTotalFinal", "id de la lista: " + lista.getId());
        }
        if(todasLasListas.size()>0){
            TextView text = (TextView) findViewById(R.id.tvNoLista);
            text.setVisibility(View.INVISIBLE);
        }
        cargarRecyclerView(todasLasListas);
        dialogos = new Dialogos(this,this);
        todasListasActivity = this;
    }

    /**
     * Método para cargar el recyclerView para mostrar los datos
     * @param array Lista con todas las listas de la compra que se van a mostrar
     */
    private void cargarRecyclerView(ArrayList<ListaCompra>array){
        adapter = new CustomRecyclerViewAdapterListas(todasLasListas);
        recyclerView = (RecyclerView) findViewById(R.id.rvListas);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        cargarSwipe();
    }

    /**
     * Método para adjuntar el swipe para ver el detalle de una lista o borrarla
     */
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
                    dialogos.dialogoBorrarLista(todasListasActivity, position);
                    Log.d("ñññ", "onSwiped: 1");
                } else if(direction == ItemTouchHelper.RIGHT){
                    intent = new Intent(getApplicationContext(), MostradorProductosListaActivity.class);
                    intent.putExtra(getString(R.string.lista_p), adapter.getLista(position));
                    adapter.notifyDataSetChanged();
                    startActivity(intent);
                    Log.d("ñññ", "onSwiped: 2");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSortAll:
                sort *= -1;
                adapter.sortRecyclerView(sort);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Método para eliminar una lista
     *
     * @param position posicion de la lista en el listado con todas ellas
     */
    public void deleteOne(int position){
        adapter.removeItem(position);
        listas = adapter.getListaCompra();
        gfl.actualizarListas(listas);
    }

    /**
     * Método para cancelar la eliminación de una lista de la compra
     */
    public void cancel(){
        adapter.notifyDataSetChanged();
    }
}
