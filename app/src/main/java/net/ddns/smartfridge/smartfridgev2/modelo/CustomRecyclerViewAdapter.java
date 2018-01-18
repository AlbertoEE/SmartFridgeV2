package net.ddns.smartfridge.smartfridgev2.modelo;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.MiNeveraActivity;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;

/**
 * Created by Alberto on 17/01/2018.
 */

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder>{
    private Cursor cursor;
    private MiNeveraActivity activity;
    ArrayList<Alimento> alimentos;

    public CustomRecyclerViewAdapter(Cursor cursor, MiNeveraActivity activity){
        this.cursor = cursor;
        this.activity = activity;
        alimentos = new ArrayList();
        //_id", "nombre", "cantidad", "dias_caducidad", "fecha_registro", "fecha_caducidad", "imagen_alimento", "id_alimento_creado
        //int id, String nombreAlimento, int cantidad, int dias_caducidad, String fecha_registro, String fecha_caducidad, Bitmap imagen
        cargarArray();
        Log.d("RAQUEL", "CustomRecyclerViewAdapter: " + Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    private void cargarArray(){
        if(cursor.moveToFirst()){
            byte[] byteArrayFoto;
            Bitmap bm;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            for (int i = 0; i < cursor.getCount(); i++) {
                byteArrayFoto = cursor.getBlob(6);
                if(byteArrayFoto != null) {
                    byteArrayFoto = cursor.getBlob(6);
                    bm = BitmapFactory.decodeByteArray(byteArrayFoto, 0, byteArrayFoto.length);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    alimentos.add(new Alimento(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            bm));
                }else{
                    alimentos.add(new Alimento(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            null));
                }
            }
        }
    }

    private void llenarFila(CustomRecyclerViewAdapter.ViewHolder holder, int position){
        holder.tvNombre.setText(alimentos.get(position).getNombreAlimento());
        holder.tvUnidades.setText(String.valueOf(alimentos.get(position).getCantidad()));
        holder.tvDiasCaducidad.setText(alimentos.get(position).getDias_caducidad());
        holder.tvFechaCaducidad.setText(formatearFecha(alimentos.get(position).getFecha_caducidad()));

        if(alimentos.get(position).getImagen() != null){
            try {
                Glide.with(this.activity.getApplicationContext())
                        .load(alimentos.get(position).getImagen())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.ivFotoAlimento);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        /*cursor.moveToPosition(position);

        holder.tvNombre.setText(cursor.getString(1));
        holder.tvUnidades.setText(String.valueOf(cursor.getInt(2)));
        holder.tvDiasCaducidad.setText(cursor.getString(3));
        holder.tvFechaCaducidad.setText(formatearFecha(cursor.getString(5)));
        byte[] byteArrayFoto = cursor.getBlob(6);
        if(byteArrayFoto != null){
            Bitmap bm = BitmapFactory.decodeByteArray(byteArrayFoto, 0 ,byteArrayFoto.length);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            try {
                Glide.with(this.activity.getApplicationContext())
                        .load(stream.toByteArray())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.ivFotoAlimento);
            } catch (Exception e){
                e.printStackTrace();
            }
        }*/
    }

    private String formatearFecha(String fecha){
        String fechaFinal;

        fechaFinal = fecha.substring(0,2) + "/";
        fechaFinal += fecha.substring(2,4) + "/";
        fechaFinal += fecha.substring(4,8);

        return fechaFinal;
    }

    @Override
    public CustomRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_recyclerview, parent,
                false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapter.ViewHolder holder, int position) {
        llenarFila(holder, position);
    }

    @Override
    public int getItemCount() {
        return  cursor.getCount();
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
            tvFechaCaducidad = itemView.findViewById(R.id.tvFechaCaducidadFila);
            ivFotoAlimento = itemView.findViewById(R.id.ivFotoAlimentoFila);
        }
    }
}
