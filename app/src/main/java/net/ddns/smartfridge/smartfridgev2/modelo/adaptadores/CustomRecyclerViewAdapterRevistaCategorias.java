package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Ingrediente;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.clc.DetalleListaExternaActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Alberto on 15/02/2018.
 */

public class CustomRecyclerViewAdapterRevistaCategorias extends RecyclerView.Adapter<CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias> {
    private ArrayList<Ingrediente> ingredientes;
    private Activity activity;
    private String nombreAlimentoSeleccionado;//Para almacenar el alimento seleccionado por el usuario
    private int idAlimSele;//Para guardar el id del alimento seleccionado
    private ArrayList<ComponenteListaCompra> componentes;//Para almacenar los elementos seleccionados y pasarlos al activity
    private ComponenteListaCompra c;//Para crear el objeto que añadiremos al array de los elementos seleccionados

    public CustomRecyclerViewAdapterRevistaCategorias(ArrayList<Ingrediente> ingredientes, Activity activity, ArrayList<ComponenteListaCompra> array){
        this.ingredientes = ingredientes;
        this.activity = activity;
        this.componentes = array;
    }

    @Override
    public CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_recyclerview, parent,
                false);
        CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias viewHolder = new CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapterRevistaCategorias.ViewHolderRevistaCategorias holder, final int position) {
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
        holder.fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreAlimentoSeleccionado = ingredientes.get(position).getNombreIngrediente();
                idAlimSele = ingredientes.get(position).getIdIngrediente();
                c = new ComponenteListaCompra(idAlimSele, nombreAlimentoSeleccionado, 2);
                componentes.add(c);
                Toast.makeText(activity, "Preparando para añadir: " + c.getNombreElemento(), Toast.LENGTH_SHORT).show();
                for(int i=0; i<componentes.size();i++) {
                    Log.d("componente", "nombre: " + componentes.get(i).getNombreElemento());
                }
            }
        });
    }
    /**
     * Método para ordenar el recycler view alfabeticamente
     * @param az este int será un 1 o un -1 según el orden que queramos
     */
    public void sortRecyclerView(final int az){
        Collections.sort(ingredientes, new Comparator<Ingrediente>() {
            @Override
            public int compare(Ingrediente v1, Ingrediente v2) {
                return v1.getNombreIngrediente().compareToIgnoreCase(v2.getNombreIngrediente()) * az;
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }

    public static class ViewHolderRevistaCategorias extends RecyclerView.ViewHolder {
        private View fila;
        private ImageView imageView;
        private TextView tvNombreAlimento;
        private TextView tvUnidadesFila;
        private TextView tvDiasCaducidad;

        public ViewHolderRevistaCategorias(View itemView) {
            super(itemView);
            this.fila = itemView;
            this.imageView = (ImageView) itemView.findViewById(R.id.ivFotoAlimentoFila);
            this.tvNombreAlimento = (TextView) itemView.findViewById(R.id.tvNombreAlimentoFila);
            this.tvUnidadesFila = (TextView) itemView.findViewById(R.id.tvUnidadesFila);
            this.tvDiasCaducidad = (TextView) itemView.findViewById(R.id.tvDiasCaducidadFila);
            tvDiasCaducidad.setVisibility(View.INVISIBLE);
            tvUnidadesFila.setVisibility(View.INVISIBLE);
        }
    }

    public ArrayList<ComponenteListaCompra> getComponentes() {
        return componentes;
    }
}
