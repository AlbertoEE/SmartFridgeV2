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
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ListaCompra;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by Alberto on 10/02/2018.
 */

public class CustomArrayAdapterNuevaLista extends ArrayAdapter<ComponenteListaCompra> {
    private ArrayList<ComponenteListaCompra> productos;
    private ArrayList<ComponenteListaCompra> auxiliar;
    private ArrayList<SmoothCheckBox> checkBoxes;

    public CustomArrayAdapterNuevaLista(@NonNull Context context, ArrayList<ComponenteListaCompra> productosSugeridos) {
        super(context, R.layout.fila_producto_nueva_lista);
        if(productosSugeridos != null){
            this.productos = productosSugeridos;
        } else {
            this.productos = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String alimento = productos.get(position).getNombreElemento();

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_producto_nueva_lista, parent, false);
        }

        TextView tvAlimentoSugerido = convertView.findViewById(R.id.tvNombreroductoNuevaLista);
        SmoothCheckBox scb = convertView.findViewById(R.id.smoothCheckBoxNuevaLista);
        scb.setVisibility(View.INVISIBLE);
        checkBoxes.add(scb);

        tvAlimentoSugerido.setText(alimento);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean b) {
                if(b){
                    auxiliar.add(productos.get(position));
                } else {
                    auxiliar.remove(alimento);
                }
            }
        });

        return convertView;
    }


    public void addProducto(ComponenteListaCompra producto) {
        productos.add(producto);
        this.notifyDataSetChanged();
    }

    public void confirmarCambios(){
        this.productos = this.auxiliar;
    }

    public void mostrarCheckboxes(){
        for (SmoothCheckBox item: this.checkBoxes) {
            item.setVisibility(View.VISIBLE);
        }
    }
    public void ocultarrCheckboxes(){
        for (SmoothCheckBox item: this.checkBoxes) {
            item.setVisibility(View.INVISIBLE);
        }
    }
}
