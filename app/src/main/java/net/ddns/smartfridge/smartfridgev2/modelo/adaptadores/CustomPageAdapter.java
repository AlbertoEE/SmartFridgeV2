package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.DetallesActivity;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.Fragment_detalles;

import java.util.ArrayList;

/**
 * Created by Alberto on 23/01/2018.
 */
public class CustomPageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Alimento> alimentos;
    private ArrayList<Bitmap> imagen;
    private int id;
    private static boolean cambio = false;

    /**
     * Instantiates a new Custom page adapter.
     *
     * @param fm             the fm
     * @param alimentos      the alimentos
     * @param imagenAlimento the imagen alimento
     */
    public CustomPageAdapter(FragmentManager fm, ArrayList<Alimento> alimentos, ArrayList<Bitmap> imagenAlimento) {
        super(fm);
        this.alimentos = alimentos;
        this.imagen = imagenAlimento;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment_detalles.newInstance(alimentos.get(position), imagen.get(position), position);
    }

    @Override
    public int getItemPosition(Object object) {
        /*if(cambio){
            Log.d("PETA", "getItemPosition: " + cambio);
            cambio = false;
            Log.d("PETA", "getItemPosition: " + cambio);
            return POSITION_NONE;
        }else {
            Log.d("PETA", "getItemPosition: " + cambio);
            return POSITION_UNCHANGED;
        }*/
        return POSITION_NONE;
    }

    /**
     * Set cambio.
     *
     * @param cambio1 the cambio 1
     */
    public static void setCambio(boolean cambio1){
        cambio = cambio1;
    }

    /**
     * Remove page.
     *
     * @param id the id
     */
    public void removePage(int id){
        alimentos.remove(id);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return alimentos.size();
    }
}

