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
 * Clase creada para controlar y cargar de datos el viewPager que se encuentra en la parte de mi
 * nevera cuando iniciamos los detalles de un alimento
 */
public class CustomPageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Alimento> alimentos;
    private static ArrayList<Bitmap> imagen;

    /**
     * Constructor de la clase CustomPageAdapter.
     *
     * @param fm             el fragment manager
     * @param alimentos      los alimentos con imagenes null que pueden visualizarse en la lista de detalles
     * @param imagenAlimento las imagenes de los alimentos se pasan a aparte de los propios alimentos
     */
    public CustomPageAdapter(FragmentManager fm, ArrayList<Alimento> alimentos, ArrayList<Bitmap> imagenAlimento) {
        super(fm);
        this.alimentos = alimentos;
        this.imagen = imagenAlimento;
        Log.d("BUGASO", "CustomPageAdapter: " + alimentos.size() + "     " + imagen.size());
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment_detalles.newInstance(alimentos.get(position), imagen.get(position), position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * Método que sirve para eliminar una página si un alimento es eliminado.
     *
     * @param posicion la POSICIÓN del alimento en la lista
     */
    public void removePage(int posicion){
        alimentos.remove(posicion);
        imagen.remove(posicion);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return alimentos.size();
    }
}

