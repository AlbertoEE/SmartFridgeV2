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
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Fecha;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.MiNeveraActivity;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Alberto on 17/01/2018.
 * Esta clase servirá para adaptar el recycler view que se encuentra la clase de "MiNeveraActivity"
 * Este adaptador funciona con un cursor pero su principal fuente de datos es un arrayList cargado
 * con la información del cursor.
 */

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {
    private Cursor cursor;
    private MiNeveraActivity activity;
    private ArrayList<Alimento> alimentos; //Este array es el principal
    private ArrayList<Alimento> alimentosCopia; //Este otro tan solo se usa para el método de filtrar
    private Fecha fecha;

    public CustomRecyclerViewAdapter(Cursor cursor, MiNeveraActivity activity) {
        this.cursor = cursor;
        this.activity = activity;

        alimentos = new ArrayList();
        alimentosCopia = new ArrayList<>();
        fecha = new Fecha();

        //Después de obtener el cursor lo pasamos a ArrayList para que el manejo de datos sea más fácil
        cargarArray();

    }

    /**
     * Este método sirve para refrescar el arrayList en el que se basa el adaptador, se debe usar
     * justo después de usar el métoedo "setCursor" por lo tanto para refrescar el recyclerView
     * necesitariamos seguir este orden. setCursor --> cargarArray --> notify...(el notify que necesitemos)
     */
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
    }

    /**
     * Este método sirve para rellenar la fila con los datos del arrayList, posteriormente será
     * llamado en el "onBindViewHolder"
     *
     * @param holder ViewHolder donde vamos a asignar los datos a la UI de usuario
     * @param position posición de la lista
     */
    private void llenarFila(CustomRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.tvNombre.setText(alimentos.get(position).getNombreAlimento());
        holder.tvUnidades.setText(String.valueOf(alimentos.get(position).getCantidad()));
        try {
            //En el mismo set text hacemos el calculo de los dias que quedan de caducidad
            holder.tvDiasCaducidad.setText(fecha.fechaDias(alimentos.get(position).getFecha_caducidad(), activity.getApplicationContext()) + "\n días");
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    /**
     * Setter del cursor
     * @param cursor
     */
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
                activity.iniciardetalles(alimentos.get(position));
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
