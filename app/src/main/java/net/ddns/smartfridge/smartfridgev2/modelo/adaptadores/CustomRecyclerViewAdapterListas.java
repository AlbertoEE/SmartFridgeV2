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
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;

import java.util.ArrayList;

/**
 * Created by Alberto on 09/02/2018.
 */

public class CustomRecyclerViewAdapterListas extends RecyclerView.Adapter<CustomRecyclerViewAdapterListas.ViewHolder2> {
    private ArrayList<ListaCompra> listas;
    private Paint p = new Paint();

    public CustomRecyclerViewAdapterListas(ArrayList<ListaCompra> listaCompras){
        this.listas = listaCompras;
    }

    public void addItem(ListaCompra listaCompra) {
        listas.add(listaCompra);
        notifyItemInserted(listas.size());
    }

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
        holder.tvFechaLista.setText(listas.get(position).getFecha());
        holder.tvNumeroProductos.setText(String.valueOf(listas.get(position).getProductos().size()));
    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        public View fila;//La fila completa, para el listener
        public TextView tvFechaLista;
        public TextView tvNumeroProductos;

        public ViewHolder2(View itemView) {
            super(itemView);
            fila = itemView;
            tvFechaLista = itemView.findViewById(R.id.tvFechaListaCompra);
            tvNumeroProductos = itemView.findViewById(R.id.tvNumeroProductos);
        }
    }
}
