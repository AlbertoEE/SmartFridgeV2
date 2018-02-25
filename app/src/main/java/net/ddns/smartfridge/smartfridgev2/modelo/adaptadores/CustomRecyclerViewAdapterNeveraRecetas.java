package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.sr.MiNeveraFiltroActivity;

import java.io.ByteArrayOutputStream;
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
    private ArrayList<String> alimentosSeleccionados;
    private Cursor cursor;
    private MiNeveraFiltroActivity clase;

    public CustomRecyclerViewAdapterNeveraRecetas(Cursor cursor, Activity activity, MiNeveraFiltroActivity clase){
        this.alimentos = new ArrayList<>();
        this.alimentosCopia = new ArrayList<>();
        this.alimentosSeleccionados = new ArrayList<>();
        this.activity = activity;
        this.cursor = cursor;
        this.clase = clase;

        cargarArray();
    }

    public void cargarArray() {
        //Limpiamos el array
        alimentos.clear();

        //Comprobamos si hay datos en el cursor
        if (cursor.moveToFirst()) {
            //Variables necesarias para convertir el blob de la foto a bitmap
            byte[] byteArrayFoto;
            Bitmap bm;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            for (int i = 0; i < cursor.getCount(); i++) { //Recorremos el cursor (no sé por qué puse un for)
                byteArrayFoto = cursor.getBlob(6);
                if (byteArrayFoto != null) { //Comprobamos si el blob que nos da es null
                    //no lo es por lo tanto comprimimos la foto y añadimos un nuevo alimento a nuestro array
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
                    //el blob es null por lo tanto añadimos un alimento a nuestro array pero con la imagen null
                    alimentos.add(new Alimento(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            null));
                }
                //siguiente registro del cursor
                cursor.moveToNext();
            }
        }
        //Cuando ya tenemos nuestro array entero, hacemos una copia de el porque nos va a hacer falta
        //para el método de filtrar
        alimentosCopia.addAll(alimentos);
        cursor.close();
    }


    @Override
    public CustomRecyclerViewAdapterNeveraRecetas.ViewHolderNeveraRecetas onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_recyclerview, parent,
                false);
        CustomRecyclerViewAdapterNeveraRecetas.ViewHolderNeveraRecetas viewHolder = new CustomRecyclerViewAdapterNeveraRecetas.ViewHolderNeveraRecetas(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapterNeveraRecetas.ViewHolderNeveraRecetas holder, final int position) {
        holder.tvNombre.setText(alimentos.get(position).getNombreAlimento());
        holder.tvUnidades.setVisibility(View.INVISIBLE);
        holder.tvDiasCaducidad.setVisibility(View.INVISIBLE);
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
        holder.fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alimentosSeleccionados.size()<6){
                    if(!alimentosSeleccionados.contains(alimentos.get(position).getNombreAlimento())){
                        alimentosSeleccionados.add(alimentos.get(position).getNombreAlimento());
                        Toast.makeText(activity, "Alimento seleccionado", Toast.LENGTH_SHORT).show();
                        clase.addChip(alimentos.get(position).getNombreAlimento());
                    } else {
                        Toast.makeText(activity, "El alimento ya ha sido seleccionado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(activity, "Ya se han seleccionado 3 alimentos", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public void eliminarSeleccionado(String string){
        alimentosSeleccionados.remove(string);
    }

    public ArrayList<String> getAlimentosSeleccionados(){
        return this.alimentosSeleccionados;
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
