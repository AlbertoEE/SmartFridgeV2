package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.TabTipo;

import java.io.ByteArrayOutputStream;

/**
 * Created by Alberto on 22/02/2018.
 */

public class CustomRecyclerViewAdapterFiltroTipos extends RecyclerView.Adapter<CustomRecyclerViewAdapterFiltroTipos.ViewHolderRevistaFiltroTipos> {
    private Activity activity;
    private TabTipo tabTipo;
    private Context contexto;
    public CustomRecyclerViewAdapterFiltroTipos(Activity activity, TabTipo tabTipo, Context cont){
        this.activity = activity;
        this.tabTipo = tabTipo;
        this.contexto = cont;
    }
    @Override
    public CustomRecyclerViewAdapterFiltroTipos.ViewHolderRevistaFiltroTipos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_tipo, parent,
                false);
        CustomRecyclerViewAdapterFiltroTipos.ViewHolderRevistaFiltroTipos viewHolder = new CustomRecyclerViewAdapterFiltroTipos.ViewHolderRevistaFiltroTipos(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomRecyclerViewAdapterFiltroTipos.ViewHolderRevistaFiltroTipos holder, final int position) {
        Drawable d = null;
        if (position == 0){
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_arroz);
        } else if (position == 1) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_carne);
        } else if (position == 2) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_ensalada);
        } else if (position == 3) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_entrantes);
        } else if (position == 4) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_guiso);
        } else if (position == 5) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_pan);
        } else if (position == 6) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_pasta);
        } else if (position == 7) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_pizza);
        }else if (position == 8) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_postre);
        } else if(position == 9) {
            d = this.activity.getApplicationContext().getResources().getDrawable(R.drawable.ic_pescado);
        }
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        try {
            Glide.with(this.activity.getApplicationContext())
                    .load(bitmapdata)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.iamgebutton);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.iamgebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Dialogos d = new Dialogos(contexto, tabTipo, activity);
                d.dialogoFiltroTipo(position + 1);*/
                tabTipo.iniciarAsync(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolderRevistaFiltroTipos extends RecyclerView.ViewHolder{
        public ImageButton iamgebutton;
        public ViewHolderRevistaFiltroTipos(View itemView) {
            super(itemView);
            iamgebutton = itemView.findViewById(R.id.ibTipo);
        }
    }
}
