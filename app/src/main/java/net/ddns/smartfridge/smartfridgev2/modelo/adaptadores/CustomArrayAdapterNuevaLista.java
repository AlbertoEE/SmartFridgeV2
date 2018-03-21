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
import java.util.Collections;
import java.util.Comparator;

import cn.refactor.library.SmoothCheckBox;

/**
 * Esta clase se encarga de mostrar de adaptar un listView a la hora de crear una lista de la compra
 * nueva
 */
public class CustomArrayAdapterNuevaLista extends ArrayAdapter<ComponenteListaCompra> {
    private ArrayList<ComponenteListaCompra> productos;
    private ArrayList<ComponenteListaCompra> auxiliar;
    private ArrayList<SmoothCheckBox> checkBoxes;
    private Dialogos dialogos;
    private ArrayList<Boolean> booleans;
    private int contador;//Para ver si hay elementos que están repetidos

    /**
     * Constructor para crear un CustomArrayAdapterNuevaLista
     *
     * @param context            el contexto
     * @param productosSugeridos los productos sugeridos por la apliación al usuario
     * @param activity           la actividad para poder crear diálogos
     */
    public CustomArrayAdapterNuevaLista(@NonNull Context context, ArrayList<ComponenteListaCompra> productosSugeridos, Activity activity) {
        super(context, R.layout.fila_producto_nueva_lista, productosSugeridos);
        //Si el array recibido es distinto de null lo asignamos al atributo de la clase para
        //trabajar con él. Este parámetro puede llegar null ya que de los alimentos sugeridos el
        //usuario puede no haber seleccionado ninguno.
        if (productosSugeridos != null) {
            this.productos = productosSugeridos;
        } else { // En caso de que sea null lo instanciamos como uno nuevo y lo asignamos
            productosSugeridos = new ArrayList<>();
            this.productos = productosSugeridos;
        }
        //Instanciamos los atributos de la clase
        this.auxiliar = new ArrayList<>();
        this.checkBoxes = new ArrayList<>();
        dialogos = new Dialogos(context, activity);
        //cargamos un array de booleans
        cargarBooleans();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Obtenemos el string del arrayList del constructor según la posición de la fila
        final String alimento = productos.get(position).getNombreElemento();
        //Aquí inflamos la "fila" con el fin de poder trabajar con ella
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_producto_nueva_lista, parent, false);
        }
        //En este bloque de código empezamos a cargar los elementos gráficos, además también
        //empieza el control de checkboxes y se le añade el listener correspondiente a cada checkbox
        //y se añade a un array de checkboxes.
        TextView tvAlimentoSugerido = convertView.findViewById(R.id.tvNombreroductoNuevaLista);
        SmoothCheckBox scb = convertView.findViewById(R.id.smoothCheckBoxNuevaLista);
        //Hacemos que los checkboxes sean invisibles desde el primer momento
        scb.setVisibility(View.INVISIBLE);
        //Añadimos el correspondiente checkbox al arrayList siempre y cuando sea único
        comprobarRepetidos(checkBoxes, scb);
        tvAlimentoSugerido.setText(alimento);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean b) {
                if (b) {
                    //Si un checkbox se ha puesto a true, en nuestro array de booleans ponemos el
                    //boolean correspondiente a la posición del checkbox a true.
                    booleans.set(position, true);
                } else {
                    booleans.set(position, false);
                }
            }
        });
        //Asignamos un listener a cada fila, al hacer un "long clic" iniciamos el diálogo de
        //modificar
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialogos.dialogoModificarBorrar(alimento, CustomArrayAdapterNuevaLista.this, position);
                return false;
            }
        });

        return convertView;
    }

    /**
     * Este método instancia un array de booleans con todos a true según el tamaño del array de los
     * alimentos sugeridos por la aplicación
     */
    public void cargarBooleans(){
        booleans = new ArrayList<>();
        booleans.clear();
        for (int i = 0; i < productos.size(); i++){
            booleans.add(true);
        }
    }

    /**
     * Este método añade un prodcuto al array de productos y además tamién añade un booleano true
     * al array de booleanos que tiene que estar sincronizado con el array de alimentos
     *
     * @param producto el producto que se quiere añadir
     */
    public void addProducto(ComponenteListaCompra producto) {
        if(this.productos.contains(producto)){
        } else {
            productos.add(producto);
            booleans.add(new Boolean(true));
            this.notifyDataSetChanged();
        }
    }

    /**
     * Método para confirmar los alimentos que ha seleccionado o no el usuario
     */
    public void confirmarCambios() {
        for (Boolean item : booleans) {
            Log.d("ConfirmarCambios", "confirmarCambios: " + item.booleanValue());
        }
        Log.d("ConfirmarCambios", "confirmarCambios: " + booleans.size());
        Log.d("ConfirmarCambios", "confirmarCambios: " + productos.size());

        for (int i = 0; i < booleans.size(); i++) {
            if(booleans.get(i).booleanValue()){
                auxiliar.add(productos.get(i));
            }
        }
        for (ComponenteListaCompra item : auxiliar) {
            Log.d("bucle", "confirmarCambios: " + item.getNombreElemento());
        }

        productos.clear();
        for (ComponenteListaCompra item : auxiliar) {
            productos.add(item);
        }
        for (ComponenteListaCompra item : productos) {
            Log.d("bucle2", "confirmarCambios: " + item.getNombreElemento());
        }
        auxiliar.clear();
        Log.d("check", "longitud de productos: " + productos.size());
        checkBoxes.clear();
        cargarBooleans();
        this.notifyDataSetChanged();
    }

    /**
     * Gets lista final.
     *
     * @return la lista final de alimetos filtrada según los criterios del usuario
     */
    public ArrayList<ComponenteListaCompra> getListaFinal() {
        return this.productos;
    }

    /**
     * Metodo para mostrar los checkboxes gracias al arrayList de checkboxes
     */
    public void mostrarCheckboxes() {
        for (SmoothCheckBox item : this.checkBoxes) {
            item.setVisibility(View.VISIBLE);
            item.setChecked(true);
        }
    }

    /**
     * Metodo para ocultar los checkboxes gracias al arrayList de checkboxes
     */
    public void ocultarrCheckboxes() {
        for (SmoothCheckBox item : this.checkBoxes) {
            item.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Metodo para modificar el nombre de un alimento ya existente en el array
     *
     * @param position     la posición del alimento
     * @param modificacion el nuevo nombre
     */
    public void modificar(int position, String modificacion){
        if (modificacion != null) {
            productos.get(position).setNombreElemento(modificacion);
            notifyDataSetChanged();
        } else {
            productos.remove(position);
            notifyDataSetChanged();
        }
    }

    /**
     * Getter del tamaño.
     *
     * @return tamaño
     */
    public int getSize(){
        Log.d("PR", "getSize: " + productos.size());
        return productos.size();
    }

    /**
     * Comprobar repetidos.
     *
     * @param checkbox the checkbox
     * @param scb      the scb
     */
//Método para comprobar si hay algún checkbox repetido en la lista y no ponerlo
    public void comprobarRepetidos(ArrayList<SmoothCheckBox> checkbox, SmoothCheckBox scb){
        contador = 0;//Lo inicializamos
        for (SmoothCheckBox c : checkbox){
            //Si coinciden los valores, se incrementa el contador
            if(c==scb){
                contador++;
            }
        }
        //Si no ha encontrado ninguna coincidencia, se añade al arrayList
        if(contador==0){
            checkbox.add(scb);
        }
    }

    /**
     * Comprobar repetidos alimentos.
     *
     * @param aux        the aux
     * @param componente the componente
     */
//Método para comprobar si hay algún alimento repetido en la lista y no ponerlo
    public void comprobarRepetidosAlimentos(ArrayList<ComponenteListaCompra> aux, ComponenteListaCompra componente){
        contador=0;//Iniciamos el contador
        for (ComponenteListaCompra c : aux){
            //Recorremos el array viendo si coinciden los valores
            if(c.getNombreElemento().equals(componente.getNombreElemento())){
                contador++;
            }
        }
        //Si no hay coincidencias, se añade al arrayList correspondiente
        if(contador==0){
            aux.add(componente);
        }
    }

    /**
     * Método para ordenar el recycler view alfabeticamente
     *
     * @param az este int será un 1 o un -1 según el orden que queramos
     */
    public void sortRecyclerView(final int az){
        Collections.sort(productos, new Comparator<ComponenteListaCompra>() {
            @Override
            public int compare(ComponenteListaCompra v1, ComponenteListaCompra v2) {
                return v1.getNombreElemento().compareToIgnoreCase(v2.getNombreElemento()) * az;
            }
        });
        notifyDataSetChanged();
    }

    /**
     * Add productos varios.
     *
     * @param productos los productos obtenidos desde el catálogo de productos
     */
    public void addProductosVarios(ArrayList<ComponenteListaCompra> productos){
        for (ComponenteListaCompra item: productos) {
            Log.d("atila", "addProductosVarios: " + item.getNombreElemento() + "\n" + item.getId() + "\n" + item.getTipo());
            if (!this.productos.contains(item)) {
                this.productos.add(item);
                Log.d("atila", "pene: " + item.getNombreElemento() + "\n" + item.getId() + "\n" + item.getTipo());

            }
        }
        for (ComponenteListaCompra item : this.productos) {
            Log.d("atilo", "addProductosVarios: " + item.getNombreElemento() + item.getId() + item.getTipo());
        }
        cargarBooleans();
        notifyDataSetChanged();
    }
}
