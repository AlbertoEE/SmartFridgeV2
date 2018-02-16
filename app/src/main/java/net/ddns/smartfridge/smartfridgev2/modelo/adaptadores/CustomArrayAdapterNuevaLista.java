package net.ddns.smartfridge.smartfridgev2.modelo.adaptadores;

import android.app.Activity;
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
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by Alberto on 10/02/2018.
 */

public class CustomArrayAdapterNuevaLista extends ArrayAdapter<ComponenteListaCompra> {
    private ArrayList<ComponenteListaCompra> productos;
    private ArrayList<ComponenteListaCompra> auxiliar;
    private ArrayList<SmoothCheckBox> checkBoxes;
    private Dialogos dialogos;
    private Activity activity;
    private String modificacion;

    public CustomArrayAdapterNuevaLista(@NonNull Context context, ArrayList<ComponenteListaCompra> productosSugeridos, Activity activity) {
        super(context, R.layout.fila_producto_nueva_lista, productosSugeridos);
        if (productosSugeridos != null) {
            this.productos = productosSugeridos;

        } else {
            this.productos = new ArrayList<>();
        }
        this.auxiliar = new ArrayList<>();
        this.checkBoxes = new ArrayList<>();
        dialogos = new Dialogos(context, activity);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String alimento = productos.get(position).getNombreElemento();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_producto_nueva_lista, parent, false);
        }

        TextView tvAlimentoSugerido = convertView.findViewById(R.id.tvNombreroductoNuevaLista);
        SmoothCheckBox scb = convertView.findViewById(R.id.smoothCheckBoxNuevaLista);
        checkBoxes.add(scb);
        scb.setVisibility(View.INVISIBLE);


        tvAlimentoSugerido.setText(alimento);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean b) {
                if (b) {
                    auxiliar.add(productos.get(position));
                } else {
                    auxiliar.remove(alimento);
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialogos.dialogoModificarBorrar(alimento, CustomArrayAdapterNuevaLista.this, position);
                return false;
            }
        });

        return convertView;
    }


    public void addProducto(ComponenteListaCompra producto) {
        productos.add(producto);
        Log.d("customer", productos.get(0).getNombreElemento());
        this.notifyDataSetChanged();
    }

    public void confirmarCambios() {
        this.productos = this.auxiliar;
        for (ComponenteListaCompra item: auxiliar) {
            Log.d("la que he liao", "confirmarCambios: " + item.getNombreElemento() );
        }
        auxiliar.clear();
        this.notifyDataSetChanged();
    }

    public ArrayList<ComponenteListaCompra> getListaFinal() {
        return this.productos;
    }

    public void mostrarCheckboxes() {
        auxiliar = productos;
        for (SmoothCheckBox item : this.checkBoxes) {
            item.setVisibility(View.VISIBLE);
            item.setChecked(true);
        }
    }

    public void ocultarrCheckboxes() {
        for (SmoothCheckBox item : this.checkBoxes) {
            item.setVisibility(View.INVISIBLE);
        }
    }

    public void modificar(int position, String modificacion){
        if (modificacion != null) {
            productos.get(position).setNombreElemento(modificacion);
            notifyDataSetChanged();
        } else {
            productos.remove(position);
            notifyDataSetChanged();
        }
    }
}
