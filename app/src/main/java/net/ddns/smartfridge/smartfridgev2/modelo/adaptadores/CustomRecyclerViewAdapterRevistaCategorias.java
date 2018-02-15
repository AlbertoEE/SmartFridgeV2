package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;

import java.util.ArrayList;

/**
 * Created by Alberto on 15/02/2018.
 */

public class CustomRecyclerViewAdapterRevistaCategorias extends RecyclerView.Adapter<CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias> {
    private ArrayList<Ingrediente> ingredientes;

    public CustomRecyclerViewAdapterRevistaCategorias(ArrayList<Ingrediente> ingredientes){
        this.ingredientes = ingredientes;
    }

    @Override
    public CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_revista_main, parent,
                false);
        CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias viewHolder = new CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias holder, int position) {

    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }

    public static class ViewHolderRevistaCategorias extends RecyclerView.ViewHolder {

        public ViewHolderRevistaCategorias(View itemView) {
            super(itemView);
        }
    }
}
