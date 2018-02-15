package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Alberto on 15/02/2018.
 */

public class CustomRecyclerViewAdapterRevistaCategorias extends RecyclerView.Adapter<CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias> {
    private ArrayList<Ingrediente> ingredientes;
    private Activity activity;

    public CustomRecyclerViewAdapterRevistaCategorias(ArrayList<Ingrediente> ingredientes, Activity activity){
        this.ingredientes = ingredientes;
        this.activity = activity;
    }

    @Override
    public CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_recyclerview, parent,
                false);
        CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias viewHolder = new CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias holder, int position) {
        Bitmap bitmap = ingredientes.get(position).getImagenIngrediente();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        try {
            Glide.with(this.activity.getApplicationContext())
                    .load(bitmapdata)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvNombreAlimento.setText(ingredientes.get(position).getNombreIngrediente());
    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }

    public static class ViewHolderRevistaCategorias extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView tvNombreAlimento;
        private TextView tvUnidadesFila;
        private TextView tvDiasCaducidad;

        public ViewHolderRevistaCategorias(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.ivFotoAlimentoFila);
            this.tvNombreAlimento = (TextView) itemView.findViewById(R.id.tvNombreAlimentoFila);
            this.tvUnidadesFila = (TextView) itemView.findViewById(R.id.tvUnidadesFila);
            this.tvDiasCaducidad = (TextView) itemView.findViewById(R.id.tvDiasCaducidadFila);
            tvDiasCaducidad.setVisibility(View.INVISIBLE);
            tvUnidadesFila.setVisibility(View.INVISIBLE);
        }
    }
}
