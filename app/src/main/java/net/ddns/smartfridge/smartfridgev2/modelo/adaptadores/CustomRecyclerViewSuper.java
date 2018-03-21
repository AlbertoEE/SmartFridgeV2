package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yayandroid.parallaxrecyclerview.ParallaxImageView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Precio;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Clase creada para mostrar los precios de un supermercado.
 */
public class CustomRecyclerViewSuper extends RecyclerView.Adapter<CustomRecyclerViewSuper.ViewHolderSuper> {
    private Activity activity;
    private ArrayList<Precio> precios;
    private ArrayList<Precio> preciosAuxiliar;
    private DecimalFormat f = new DecimalFormat("##.00");

    /**
     * Constructor de la clase Custom recycler view super.
     *
     * @param activity     la actividad donde se encuentra el recycler view
     * @param precios      los precios
     * @param superMercado el supermercado del que provienen
     */
    public CustomRecyclerViewSuper(Activity activity, ArrayList<Precio> precios, String superMercado){
        this.activity = activity;
        this.precios = precios;

        preciosAuxiliar = new ArrayList<>();

        for (Precio item : precios) {
            if (item.getSupermercado().equalsIgnoreCase(superMercado)){
                preciosAuxiliar.add(item);
            }
        }

        this.precios.clear();
        this.precios = this.preciosAuxiliar;
    }
    @Override
    public CustomRecyclerViewSuper.ViewHolderSuper onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_super, parent,
                false);
        CustomRecyclerViewSuper.ViewHolderSuper viewHolder = new CustomRecyclerViewSuper.ViewHolderSuper(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewSuper.ViewHolderSuper holder, int position) {
        holder.tvProductoSuper.setText(precios.get(position).getNombreProducto());
        holder.tvSuperMercado.setText(precios.get(position).getSupermercado());
        holder.tvPrecioSuper.setText(f.format(precios.get(position).getPvp()) + "€");
    }

    @Override
    public int getItemCount() {
        return precios.size();
    }


    /**
     * The type View holder super.
     */
    public static class ViewHolderSuper extends RecyclerView.ViewHolder {
        private TextView tvSuperMercado;
        private TextView tvProductoSuper;
        private TextView tvPrecioSuper;

        /**
         * Instantiates a new View holder super.
         *
         * @param itemView the item view
         */
        public ViewHolderSuper(View itemView) {
            super(itemView);
            tvSuperMercado = itemView.findViewById(R.id.tvSuperMercado);
            tvProductoSuper = itemView.findViewById(R.id.tvProductoSuper);
            tvPrecioSuper = itemView.findViewById(R.id.tvPrecioSuper);
        }
    }
}
