package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.ddns.smartfridge.smartfridgev2.R;

/**
 * Created by Alberto on 22/02/2018.
 */

public class CustomBaseAdapter extends BaseAdapter {
    private Context context;

    public CustomBaseAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 4;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        ViewHolderBase fila;

        if (view == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.fila_spinner, viewGroup, false);
            fila = new ViewHolderBase();
            fila.imageView = (ImageView) itemView.findViewById(R.id.ivSpinner);
            itemView.setTag(fila);
        } else {
            fila = (ViewHolderBase) itemView.getTag();
        }

        fila.imageView.setImageBitmap();
        return null;
    }

    private static class ViewHolderBase{
        ImageView imageView;
    }
}
