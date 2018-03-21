package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.refactor.library.SmoothCheckBox;

/**
 * Esta clase es usada para el filtrado de alimentos sugeridos.
 */
public class CustomArrayAdapter extends ArrayAdapter<ComponenteListaCompra> {
    private ArrayList<ComponenteListaCompra> alimentos;
    private ArrayList<ComponenteListaCompra> alimentosSeleccionados;
    private ArrayList<SmoothCheckBox> smoothCheckBoxes;
    private boolean cambio = false;

    /**
     * Constructor de la clase CustomArrayAdapter
     *
     * @param context   el contexto
     * @param alimentos arraylist que contiene los los alimentos que se le van a sugerir al usuario
     */
    public CustomArrayAdapter(Context context, ArrayList<ComponenteListaCompra> alimentos){
        super(context, R.layout.fila_alimentos_sugeridos, alimentos);
        this.alimentos = alimentos;
        alimentosSeleccionados = new ArrayList<>();
        smoothCheckBoxes = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Obtenemos el string del arrayList del constructor según la posición de la fila
        final String alimento = alimentos.get(position).getNombreElemento();
        //Aquí inflamos la "fila" con el fin de poder trabajar con ella
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_alimentos_sugeridos, parent, false);
        }
        //En este bloque de código empezamos a cargar los elementos gráficos, además también
        //empieza el control de checkboxes y se le añade el listener correspondiente a cada checkbox
        //y se añade a un array de checkboxes.
        TextView tvAlimentoSugerido = convertView.findViewById(R.id.textViewAlimentoSugerido);
        SmoothCheckBox scb = convertView.findViewById(R.id.smoothCheckBox);
        smoothCheckBoxes.add(scb);
        tvAlimentoSugerido.setText(alimento);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean b) {
                if(b){
                    alimentosSeleccionados.add(alimentos.get(position));
                    Log.d("RETRASO", "onCheckedChanged: TRUE " + position);
                } else {
                    alimentosSeleccionados.remove(alimento);
                    Log.d("RETRASO", "onCheckedChanged: FALSO " + position);
                }
            }
        });

        return convertView;
    }

    /**
     * Este método sirve para cambiar todos los checkboxes a true o a false de manera alternativa
     */
    public void cambiarCheckBoxes(){
        cambio = !cambio;
        if(cambio){
            for (SmoothCheckBox item: smoothCheckBoxes) {
                item.setChecked(true, true);
            }
        } else {
            for (SmoothCheckBox item: smoothCheckBoxes) {
                item.setChecked(false, true);
            }
            alimentosSeleccionados.clear();
        }
    }

    /**
     * Get nueva lista array list.
     *
     * @return el array list que contiene los alimentos seleccionados por el usuario
     */
    public ArrayList<ComponenteListaCompra> getNuevaLista(){
        return alimentosSeleccionados;
    }

    /**
     * Método para ordenar el recycler view alfabeticamente
     *
     * @param az este int será un 1 o un -1 según el orden que queramos
     */
    public void sortRecyclerView(final int az){
        Collections.sort(alimentos, new Comparator<ComponenteListaCompra>() {
            @Override
            public int compare(ComponenteListaCompra v1, ComponenteListaCompra v2) {
                return v1.getNombreElemento().compareToIgnoreCase(v2.getNombreElemento()) * az;
            }
        });
        notifyDataSetChanged();
    }
}
