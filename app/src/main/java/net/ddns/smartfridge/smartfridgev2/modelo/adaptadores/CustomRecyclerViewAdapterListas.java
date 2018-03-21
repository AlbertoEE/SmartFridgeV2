package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Fecha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase creada para cargar datos en el recycler view de las listas de la compra
 */
public class CustomRecyclerViewAdapterListas extends RecyclerView.Adapter<CustomRecyclerViewAdapterListas.ViewHolder2> {
    private ArrayList<ListaCompra> listas;
    private Fecha fechaF = new Fecha();//Para formatear la fecha de la bbdd
    private ListaCompra lista;//Para recoger la lista de la posición seleccionada

    /**
     * Constructor para crear un objeto CustomRecyclerViewAdapterListas.
     *
     * @param listaCompras las listas de la compra
     */
    public CustomRecyclerViewAdapterListas(ArrayList<ListaCompra> listaCompras){
        this.listas = listaCompras;
    }

    /**
     * Añadir una nueva lista de la compra.
     *
     * @param listaCompra the lista compra
     */
    public void addItem(ListaCompra listaCompra) {
        listas.add(listaCompra);
        notifyItemInserted(listas.size());
    }

    /**
     * Eliminar una nueva lista de la compra.
     *
     * @param position the position
     */
    public void removeItem(int position) {
        listas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listas.size());
    }

    @Override
    public CustomRecyclerViewAdapterListas.ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_listas_creadas, parent,
                false);
        CustomRecyclerViewAdapterListas.ViewHolder2 viewHolder = new CustomRecyclerViewAdapterListas.ViewHolder2(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapterListas.ViewHolder2 holder, int position) {
        String fecha = fechaF.fechaCorta(listas.get(position).getFecha());
        holder.tvFechaLista.setText(fecha);
        holder.tvNumeroProductos.setText(String.valueOf(listas.get(position).getProductos().size()));
    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    /**
     * The type View holder 2.
     */
    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        /**
         * The Fila.
         */
        public View fila;//La fila completa, para el listener
        /**
         * The Tv fecha lista.
         */
        public TextView tvFechaLista;
        /**
         * The Tv numero productos.
         */
        public TextView tvNumeroProductos;

        /**
         * Instantiates a new View holder 2.
         *
         * @param itemView the item view
         */
        public ViewHolder2(View itemView) {
            super(itemView);
            fila = itemView;
            tvFechaLista = itemView.findViewById(R.id.tvFechaListaCompra);
            tvNumeroProductos = itemView.findViewById(R.id.tvNumeroProductos);
        }
    }

    /**
     * Get lista array list.
     *
     * @param posicion the posicion
     * @return the array list
     */
    public ArrayList<ComponenteListaCompra> getLista(int posicion){
        return this.listas.get(posicion).getProductos();
    }

    /**
     * Get lista compra array list.
     *
     * @return the array list
     */
//Recogemos la lista de la posición seleccionada
    public ArrayList<ListaCompra> getListaCompra(){
        return this.listas;
    }

    /**
     * Método para ordenar el recycler view alfabeticamente
     *
     * @param az este int será un 1 o un -1 según el orden que queramos
     */
    public void sortRecyclerView(final int az){
        Collections.sort(listas, new Comparator<ListaCompra>() {
            @Override
            public int compare(ListaCompra v1, ListaCompra v2) {
                return v1.getFecha().compareToIgnoreCase(v2.getFecha()) * az;
            }
        });
        notifyDataSetChanged();
    }
}
