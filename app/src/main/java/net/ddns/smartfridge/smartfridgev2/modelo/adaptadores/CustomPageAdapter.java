package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.Fragment_detalles;

import java.util.ArrayList;

/**
 * Created by Alberto on 23/01/2018.
 */

public class CustomPageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Alimento> alimentos;
    private ArrayList<Bitmap> imagen;

    public CustomPageAdapter(FragmentManager fm, ArrayList<Alimento> alimentos, ArrayList<Bitmap> imagenAlimento) {
        super(fm);
        Log.d("SWIPE", "onCreate: estoy aqui en el CustomPageAdapter" );
        this.alimentos = alimentos;
        Log.d("SWIPE", "CustomPageAdapter: " + alimentos.size());
        this.imagen = imagenAlimento;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("SWIPE", "onCreate: estoy aqui en el CustomPageAdapter GETITME" );
        return Fragment_detalles.newInstance(alimentos.get(position), imagen.get(position));
    }

    @Override
    public int getCount() {
            return alimentos.size();
    }
}

