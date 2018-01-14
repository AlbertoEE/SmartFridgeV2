package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.CaducidadAlimento;
import net.ddns.smartfridge.smartfridgev2.vista.IdentificarAlimentoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCa extends Fragment {
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
                Intent i = new Intent(getActivity(), IdentificarAlimentoActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}
