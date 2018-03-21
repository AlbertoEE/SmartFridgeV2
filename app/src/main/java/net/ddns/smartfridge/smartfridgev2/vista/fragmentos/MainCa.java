package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.IdentificadorAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.MiNeveraActivity;

/**
 * Fragment para la parte de control de alimentos
 */
public class MainCa extends Fragment {
    private View v;//Para el view del fragment

    /**
     * Constructor.
     */
    public MainCa() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_ca, container, false);
        // Inflate the layout for this fragment
        view.findViewById(R.id.ibIdentificar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), IdentificadorAlimentoActivity.class);
                startActivity(i);
            }
        });

        view.findViewById(R.id.ibNevera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MiNeveraActivity.class);
                startActivity(i);
            }
        });
        this.v = view;
        return view;
    }
    /**
     * Gets v.
     *
     * @return the v
     */
    public View getV() {
        return v;
    }
}
