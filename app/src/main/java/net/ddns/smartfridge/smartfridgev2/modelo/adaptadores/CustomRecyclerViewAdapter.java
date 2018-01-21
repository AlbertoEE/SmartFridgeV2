package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.MiNeveraActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Alberto on 17/01/2018.
 */

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {
    private Cursor cursor;
    private MiNeveraActivity activity;
    private ArrayList<Alimento> alimentos;
    private ArrayList<Alimento> alimentosCopia;

    public CustomRecyclerViewAdapter(Cursor cursor, MiNeveraActivity activity) {
        this.cursor = cursor;
        this.activity = activity;
        alimentos = new ArrayList();
        cargarArray();
        alimentosCopia = new ArrayList<>();
        alimentosCopia.addAll(alimentos);
        Log.d("RAQUEL", "CustomRecyclerViewAdapter: " + Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public void cargarArray() {
        alimentos.clear();
        if (cursor.moveToFirst()) {
            byte[] byteArrayFoto;
            Bitmap bm = null;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            for (int i = 0; i < cursor.getCount(); i++) {
                byteArrayFoto = cursor.getBlob(6);
                if (byteArrayFoto != null) {
                    byteArrayFoto = cursor.getBlob(6);
                    bm = BitmapFactory.decodeByteArray(byteArrayFoto, 0, byteArrayFoto.length);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
                alimentos.add(new Alimento(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        bm));
                cursor.moveToNext();
            }
        }
    }

    private void llenarFila(CustomRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.tvNombre.setText(alimentos.get(position).getNombreAlimento());
        holder.tvUnidades.setText(String.valueOf(alimentos.get(position).getCantidad()));
        holder.tvDiasCaducidad.setText(String.valueOf(alimentos.get(position).getDias_caducidad()) + "\n días");

        if (alimentos.get(position).getImagen() != null) {
            try {
                Glide.with(this.activity.getApplicationContext())
                        .load(alimentos.get(position).getImagen())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.ivFotoAlimento);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String formatearFecha(String fecha) {
        String fechaFinal;

        /*fechaFinal = fecha.substring(0, 2) + "/";
        fechaFinal += fecha.substring(2, 4) + "/";
        fechaFinal += fecha.substring(4, 8);*/

        return fecha;
    }

    public void filter(String text) {
        alimentos.clear();
        if(text.isEmpty()){
            alimentos.addAll(alimentosCopia);
        } else{
            text = text.toLowerCase();
            for(Alimento item: alimentosCopia){
                if(item.getNombreAlimento().toLowerCase().contains(text)){
                    alimentos.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void sortRecyclerView(final int az){
        Collections.sort(alimentos, new Comparator<Alimento>() {
            public int compare(Alimento v1, Alimento v2) {
                return v1.getNombreAlimento().compareToIgnoreCase(v2.getNombreAlimento()) * az;
            }
        });
        notifyDataSetChanged();
    }

    public void setCursor(Cursor cursor){
        this.cursor = cursor;
    }

    @Override
    public CustomRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_recyclerview, parent,
                false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapter.ViewHolder holder, final int position) {
        llenarFila(holder, position);
        holder.fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.iniciardetalles(position, alimentos.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return alimentos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View fila;//La fila completa, para el listener
        public TextView tvNombre;
        public TextView tvUnidades;
        public TextView tvDiasCaducidad;
        public TextView tvFechaCaducidad;
        public ImageView ivFotoAlimento;

        public ViewHolder(View itemView) {
            super(itemView);
            fila = itemView;
            tvNombre = itemView.findViewById(R.id.tvNombreAlimentoFila);
            tvUnidades = itemView.findViewById(R.id.tvUnidadesFila);
            tvDiasCaducidad = itemView.findViewById(R.id.tvDiasCaducidadFila);
            ivFotoAlimento = itemView.findViewById(R.id.ivFotoAlimentoFila);
        }
    }
}
