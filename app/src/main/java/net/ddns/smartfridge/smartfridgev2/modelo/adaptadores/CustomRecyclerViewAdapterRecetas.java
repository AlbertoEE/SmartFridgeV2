package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;

import java.util.ArrayList;

/**
 * Created by Alberto on 19/02/2018.
 */

public class CustomRecyclerViewAdapterRecetas extends RecyclerView.Adapter<CustomRecyclerViewAdapterRecetas.ViewHolderRecetas>{
    private ArrayList<Receta> recetas;

    public CustomRecyclerViewAdapterRecetas(ArrayList<Receta> recetas){
        this.recetas = recetas;
    }

    @Override
    public ViewHolderRecetas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_receta, parent,
                false);
        CustomRecyclerViewAdapterRecetas.ViewHolderRecetas viewHolder = new CustomRecyclerViewAdapterRecetas.ViewHolderRecetas(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderRecetas holder, int position) {
        holder.tvNombreReceta.setText(recetas.get(position).getTituloReceta());
        holder.tvTiempo.setText(recetas.get(position).getTiempoReceta());

        holder.getBackgroundImage().reuse();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolderRecetas extends ParallaxViewHolder {
        public TextView tvNombreReceta;
        public TextView tvTiempo;
        public TextView tvMinutos;
        @Override
        public int getParallaxImageId() {
            return R.id.ivReceta;
        }

        public ViewHolderRecetas(View itemView) {
            super(itemView);
            tvNombreReceta = itemView.findViewById(R.id.tvNombreReceta);
            tvTiempo = itemView.findViewById(R.id.tvTiempo);
            tvMinutos = itemView.findViewById(R.id.tvMinutos);
        }
    }
}
