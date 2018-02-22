package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yayandroid.parallaxrecyclerview.ParallaxImageView;
import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Alberto on 19/02/2018.
 */

public class CustomRecyclerViewAdapterRecetas extends RecyclerView.Adapter<CustomRecyclerViewAdapterRecetas.ViewHolderRecetas>{
    private ArrayList<Receta> recetas;
    private Activity activity;
    public CustomRecyclerViewAdapterRecetas(ArrayList<Receta> recetas, Activity activity){
        this.recetas = recetas;
        this.activity = activity;
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
        holder.tvNombreReceta.setText(String.valueOf(recetas.get(position).getTituloReceta()));
        holder.tvTiempo.setText(String.valueOf(recetas.get(position).getTiempoReceta()));
        Bitmap bitmap = (recetas.get(position).getImagenReceta());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        try {
            Glide.with(this.activity.getApplicationContext())
                    .load(bitmapdata)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.getBackgroundImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.getBackgroundImage().reuse();
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public void filtrarArray(ArrayList<Receta> recetasFiltradas){
        Log.d("lll", "filtrarArray: " + recetasFiltradas);
        this.recetas = recetasFiltradas;
        notifyDataSetChanged();
    }

    public static class ViewHolderRecetas extends ParallaxViewHolder {
        public TextView tvNombreReceta;
        public TextView tvTiempo;
        public TextView tvMinutos;
        public ParallaxImageView pivFoto;
        @Override
        public int getParallaxImageId() {
            return R.id.ivReceta;
        }

        public ViewHolderRecetas(View itemView) {
            super(itemView);
            tvNombreReceta = itemView.findViewById(R.id.tvNombreReceta);
            tvTiempo = itemView.findViewById(R.id.tvTiempo);
            tvMinutos = itemView.findViewById(R.id.tvMinutos);
            pivFoto = itemView.findViewById(R.id.ivReceta);
            pivFoto.setParallaxRatio(2.6f);
        }
    }
}
