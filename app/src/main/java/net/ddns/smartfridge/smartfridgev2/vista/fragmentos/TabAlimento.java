package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import net.ddns.smartfridge.smartfridgev2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabAlimento extends Fragment {


    public TabAlimento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_alimento, container, false);
    }
    //Método para realizar la consulta a la bbdd a partir de los datos recogidos
    public void comprobarRB(View view) {
        //Miramos si está seleccionado el radiobutton
        boolean checked = ((me.omidh.liquidradiobutton.LiquidRadioButton) view).isChecked();
        //Hacemos un case con las opciones de cada radiobutton
        switch(view.getId()) {
            case R.id.rbTenga:
                if (checked)
                    //
                    break;
            case R.id.rbNoTenga:
                if (checked)
                    //
                    break;

        }
    }
}
