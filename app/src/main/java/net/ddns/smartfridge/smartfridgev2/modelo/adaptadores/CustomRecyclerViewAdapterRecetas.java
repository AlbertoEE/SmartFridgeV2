package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.smartfridge.smartfridgev2.R;

/**
 * Created by Alberto on 19/02/2018.
 */

public class CustomRecyclerViewAdapterRecetas extends RecyclerView.Adapter<CustomRecyclerViewAdapterRecetas.ViewHolderRecetas>{

    @Override
    public ViewHolderRecetas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_recyclerview, parent,
                false);
        CustomRecyclerViewAdapterRecetas.ViewHolderRecetas viewHolder = new CustomRecyclerViewAdapterRecetas.ViewHolderRecetas(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderRecetas holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolderRecetas extends RecyclerView.ViewHolder {
        public ViewHolderRecetas(View itemView) {
            super(itemView);
        }
    }
}
