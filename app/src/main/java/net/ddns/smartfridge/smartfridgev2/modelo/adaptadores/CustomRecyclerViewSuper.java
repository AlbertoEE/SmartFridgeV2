package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yayandroid.parallaxrecyclerview.ParallaxImageView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Precio;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Alberto on 17/02/2018.
 */

public class CustomRecyclerViewSuper extends RecyclerView.Adapter<CustomRecyclerViewSuper.ViewHolderSuper> {
    private Activity activity;
    private ArrayList<Precio> precios;

    public CustomRecyclerViewSuper(Activity activity, ArrayList<Precio> precios){
        this.activity = activity;
        this.precios = precios;
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
        Drawable d = null;
        switch (position){
            case 0:
                d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.super_alcampo);
                break;
            case 1:
                d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.super_carrefour);
                break;
            case 2:
                d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.super_hipercor);
                break;
            case 3 :
                d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.super_mercadona);
                break;

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
                    .into(holder.getBackgroundImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.getBackgroundImage().reuse();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class ViewHolderSuper extends ParallaxViewHolder {

        @Override
        public int getParallaxImageId() {
            return R.id.ivParallax;
        }

        public ViewHolderSuper(View itemView) {
            super(itemView);
        }
    }
}
