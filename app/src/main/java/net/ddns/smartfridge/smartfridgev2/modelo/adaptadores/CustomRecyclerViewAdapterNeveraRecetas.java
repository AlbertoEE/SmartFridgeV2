package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Alberto on 25/02/2018.
 */

public class CustomRecyclerViewAdapterNeveraRecetas extends RecyclerView.Adapter<CustomRecyclerViewAdapterNeveraRecetas.ViewHolderNeveraRecetas>{
    private ArrayList<Alimento> alimentos;
    private Activity activity;
    private ArrayList<Alimento> alimentosCopia;

    public CustomRecyclerViewAdapterNeveraRecetas(ArrayList<Alimento> alimentos, Activity activity){
        this.alimentos = alimentos;
        this.alimentosCopia = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    public CustomRecyclerViewAdapterNeveraRecetas.ViewHolderNeveraRecetas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_recyclerview, parent,
                false);
        CustomRecyclerViewAdapterNeveraRecetas.ViewHolderNeveraRecetas viewHolder = new CustomRecyclerViewAdapterNeveraRecetas.ViewHolderNeveraRecetas(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapterNeveraRecetas.ViewHolderNeveraRecetas holder, int position) {
        holder.tvNombre.setText(alimentos.get(position).getNombreAlimento());
        holder.tvUnidades.setVisibility(View.INVISIBLE);
        holder.tvDiasCaducidad.setVisibility(View.INVISIBLE);
        holder.tvFechaCaducidad.setVisibility(View.INVISIBLE);
        //Si el alimento que estamos usando tiene imagen usamos glide para cargar la imagen
        if (alimentos.get(position).getImagen() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            alimentos.get(position).getImagen().compress(Bitmap.CompressFormat.PNG, 100, stream);
            try {
                Glide.with(this.activity.getApplicationContext())
                        .load(stream.toByteArray())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.ivFotoAlimento);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para filtrar en el recycler view
     *
     * @param text
     */
    public void filter(String text) {
        //limpiamos todos los alimentos, por eso antes hicimos una copia
        alimentos.clear();
        //Si el string que pasamos está vacio añadimos toda la copia
        if(text.isEmpty()){
            alimentos.addAll(alimentosCopia);
        } else{ //Si tiene algo buscamos las coincidencias
            text = text.toLowerCase();
            for(Alimento item: alimentosCopia){
                if(item.getNombreAlimento().toLowerCase().contains(text)){
                    alimentos.add(item);
                }
            }
        }
        //notificamos que el recycler view ha cambiado
        notifyDataSetChanged();
    }

    /**
     * Método para ordenar el recycler view alfabeticamente
     * @param az este int será un 1 o un -1 según el orden que queramos
     */
    public void sortRecyclerView(final int az){
        Collections.sort(alimentos, new Comparator<Alimento>() {
            public int compare(Alimento v1, Alimento v2) {
                return v1.getNombreAlimento().compareToIgnoreCase(v2.getNombreAlimento()) * az;
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    public class ViewHolderNeveraRecetas extends RecyclerView.ViewHolder {
        public View fila;//La fila completa, para el listener
        public TextView tvNombre;
        public TextView tvUnidades;
        public TextView tvDiasCaducidad;
        public TextView tvFechaCaducidad;
        public ImageView ivFotoAlimento;
        public ViewHolderNeveraRecetas(View itemView) {
            super(itemView);
            fila = itemView;
            tvNombre = itemView.findViewById(R.id.tvNombreAlimentoFila);
            tvUnidades = itemView.findViewById(R.id.tvUnidadesFila);
            tvDiasCaducidad = itemView.findViewById(R.id.tvDiasCaducidadFila);
            ivFotoAlimento = itemView.findViewById(R.id.ivFotoAlimentoFila);
        }
    }
}
