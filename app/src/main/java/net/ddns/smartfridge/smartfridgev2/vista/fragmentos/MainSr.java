package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yayandroid.parallaxrecyclerview.ParallaxRecyclerView;
import com.yayandroid.parallaxrecyclerview.ParallaxViewHolder;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomRecyclerViewAdapterRecetas;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Receta;
import net.ddns.smartfridge.smartfridgev2.modelo.servicios.RecetasIntentService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainSr extends Fragment {
    private ParallaxRecyclerView recyclerView;
    private CustomRecyclerViewAdapterRecetas adapter;
    private RecetasIntentService service;
    private ArrayList<Receta> recetas;

    public MainSr() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_sr, container, false);
        service = new RecetasIntentService();
        service.setMainSr(this);
        getContext().startService(new Intent(getContext(), RecetasIntentService.class));
        recyclerView = (ParallaxRecyclerView) view.findViewById(R.id.rvRecetas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        /*view.findViewById(R.id.bSeleccionarRectea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abrirmos el intent
                Intent i = new Intent(getActivity(), SeleccionarRecetaActivity.class);
                startActivity(i);
            }
        });

        view.findViewById(R.id.bSugerirReceta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        return view;
    }

    public void crearAdapter(ArrayList<Receta> recetas){
        this.recetas = recetas;
        adapter = new CustomRecyclerViewAdapterRecetas(recetas, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
