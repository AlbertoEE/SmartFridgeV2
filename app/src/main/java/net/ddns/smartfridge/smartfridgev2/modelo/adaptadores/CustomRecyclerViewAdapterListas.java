package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;

import java.util.ArrayList;

/**
 * Created by Alberto on 09/02/2018.
 */

public class CustomRecyclerViewAdapterListas extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ListaCompra> listas;

    @Override
    public CustomRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View fila;//La fila completa, para el listener
        public TextView tvFechaLista;
        public TextView tvNumeroProductos;

        public ViewHolder(View itemView) {
            super(itemView);
            fila = itemView;
            tvFechaLista = itemView.findViewById(R.id.tvFechaListaCompra);
            tvNumeroProductos = itemView.findViewById(R.id.tvNumeroProductos);
        }
    }
}
