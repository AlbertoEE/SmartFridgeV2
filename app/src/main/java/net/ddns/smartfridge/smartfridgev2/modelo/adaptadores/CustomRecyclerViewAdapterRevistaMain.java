package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.clc.CompraExternaActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.sr.SugerirRecetaActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by Alberto on 15/02/2018.
 */

public class CustomRecyclerViewAdapterRevistaMain extends RecyclerView.Adapter<CustomRecyclerViewAdapterRevistaMain.ViewHolderRevistaMain> {
    private Activity activity;
    private CompraExternaActivity compraExternaActivity;

    public CustomRecyclerViewAdapterRevistaMain(Activity activity, CompraExternaActivity compraExternaActivity){
        this.activity = activity;
        this.compraExternaActivity = compraExternaActivity;
    }

    @Override
    public CustomRecyclerViewAdapterRevistaMain.ViewHolderRevistaMain onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_revista_main, parent,
                false);
        CustomRecyclerViewAdapterRevistaMain.ViewHolderRevistaMain viewHolder = new CustomRecyclerViewAdapterRevistaMain.ViewHolderRevistaMain(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapterRevistaMain.ViewHolderRevistaMain holder, final int position) {
        Drawable d = null;
        if (position == 0){
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.verduras);
        } else if (position == 1) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.carne);
        } else if (position == 2) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.fruta);
        } else if (position == 3) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.pescado);
        } else if (position == 4) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.bebida);
        } else if (position == 5) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.embutido);
        } else if (position == 6) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.frutos_secos);
        } else if (position == 7) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.desayuno);
        }
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        try {
            Glide.with(this.activity.getApplicationContext())
                    .load(bitmapdata)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageButton);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compraExternaActivity.abrirCategoria(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public static class ViewHolderRevistaMain extends RecyclerView.ViewHolder {
        public View fila;//La fila completa, para el listener
        private ImageButton imageButton;

        public ViewHolderRevistaMain(View itemView) {
            super(itemView);
            fila = itemView;
            imageButton = itemView.findViewById(R.id.imageButton);
        }
    }
}
