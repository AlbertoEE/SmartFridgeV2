package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;

import java.util.ArrayList;

/**
 * Created by Alberto on 23/01/2018.
 */

public class CustomPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Alimento> alimentos;

    public CustomPageAdapter(FragmentManager fm, ArrayList<Alimento> alimentos) {
        super(fm);
        this.alimentos = alimentos;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
            return alimentos.size();
    }
}

