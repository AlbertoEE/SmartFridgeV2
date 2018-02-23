package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.content.Intent;
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
import net.ddns.smartfridge.smartfridgev2.vista.actividades.sr.DetallesRecetaActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Alberto on 19/02/2018.
 */

public class CustomRecyclerViewAdapterRecetas extends RecyclerView.Adapter<CustomRecyclerViewAdapterRecetas.ViewHolderRecetas>{
    private ArrayList<Receta> recetas;
    private Activity activity;
    private Intent intent;//Para iniciar la nueva actividad

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
    public void onBindViewHolder(ViewHolderRecetas holder, final int position) {
        holder.tvNombreReceta.setText(String.valueOf(recetas.get(position).getTituloReceta()));
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
        holder.fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(activity.getApplicationContext(), DetallesRecetaActivity.class);
                intent.putExtra("id", recetas.get(position).getIdReceta());
                intent.putExtra("nombre", recetas.get(position).getTituloReceta());
                intent.putExtra("descripcion", recetas.get(position).getDescripcion());
                intent.putExtra("tipo", recetas.get(position).getTipoReceta());
                intent.putExtra("duracion", recetas.get(position).getTiempo());
                intent.putExtra("dificultad", recetas.get(position).getDificultad());
                intent.putExtra("imagen", recetas.get(position).getImagenReceta());
                activity.startActivity(intent);
            }
        });
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
        public View fila;
        public TextView tvNombreReceta;
        public ParallaxImageView pivFoto;
        @Override
        public int getParallaxImageId() {
            return R.id.ivReceta;
        }

        public ViewHolderRecetas(View itemView) {
            super(itemView);
            fila = itemView;
            tvNombreReceta = itemView.findViewById(R.id.tvNombreReceta);
            pivFoto = itemView.findViewById(R.id.ivReceta);
            pivFoto.setParallaxRatio(2.6f);
        }
    }
}
