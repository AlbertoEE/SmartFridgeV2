package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomPageAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Fecha;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.DetallesActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.MiNeveraActivity;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

/**
 * Fragment para ver el detalle de un ingrediente
 */
public class Fragment_detalles extends Fragment {
    private int unidadesWheel;
    private static int MAXUDS = 50;
    private WheelPicker wheelPicker;
    private TextView tvNombreAlimento;
    private TextView tvFechaCaducidad;
    private ImageView ivFondoBlur;
    private TextView tvDiasRestantes;
    private ImageView ivAlimento;
    private Dialogos dialogos;
    private View constraintLayout;
    private AlimentoDB adb;
    private Alimento alimento;
    private Bitmap imagen;
    private AlimentoDB alimentoDB;
    private CustomPageAdapter customPageAdapter;
    private int posicion;
    private Fecha fecha;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles, container, false);
        ViewPager vp=(ViewPager) getActivity().findViewById(R.id.viewpager);
        fecha = new Fecha();
        dialogos = new Dialogos(getContext(), getActivity());

        customPageAdapter = (CustomPageAdapter) vp.getAdapter();
        wheelPicker = view.findViewById(R.id.wheelUdsDetalles);
        alimentoDB = new AlimentoDB(getContext());
        readBundle(getArguments());
        Log.d("SWIPE", "onCreate: estoy aqui en el Fragment_detalles" );

        dialogos = new Dialogos(getActivity().getApplicationContext(), getActivity());
        adb = new AlimentoDB(this.getActivity().getApplicationContext());

        Intent intent = getActivity().getIntent();

        cargarDetallesAlimento(view);

        return view;
    }

    /**
     * Crea una nueva instancia del fragment
     *
     * @param alimento Alimento a mostrar
     * @param imagen   imagen del alimento
     * @param position posición del alimento
     * @return el Fragment correspondiente
     */
    public static Fragment_detalles newInstance(Alimento alimento, Bitmap imagen, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("alimento", alimento);
        bundle.putParcelable("imagen", imagen);
        bundle.putInt("posicion", position);

        Fragment_detalles fragment = new Fragment_detalles();
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * Método para leer los datos que se han recogido en newInstance
     * @param bundle Datos del newInstance
     */
    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            this.alimento = (Alimento) bundle.getSerializable("alimento");
            this.imagen = (Bitmap) bundle.getParcelable("imagen");
            this.posicion = (int) bundle.getInt("posicion");
            wheel(wheelPicker);
        }

    }

    /**
     * Método para crear y dar los parámetros al wheelpicker para elegir los días de caducidad
     *
     * @param wheelPicker Wheelpicker que vamos a utilizar
     */
    public void wheel(final WheelPicker wheelPicker){
        //final int itemSel;//Para el item seleccionado
        //Asignamos datos al WheelPicker
        List<Integer> unidades = new ArrayList<>();
        for (int k = 0; k <= MAXUDS; k++)
            unidades.add(k);
        wheelPicker.setData(unidades);
        //Asignamos los items que van a estar visibles
        wheelPicker.setVisibleItemCount(2);
        //Le ponemos las mismas dimensiones a todos los elementos
        wheelPicker.setSameWidth(true);
        //Indicamos que al inciarse esté apuntando a un elemento, en este caso, el primero


        wheelPicker.setSelectedItemPosition(alimento.getCantidad());
        //Iniciamos la variable a 1, ya que empezará en el primer elemento, que tendrá valor 1
        /*Para poner color de fondo
        wheelPicker.setBackgroundColor(getResources().getColor(R.color.viewfinder_laser));*/
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                int itemSel = picker.getCurrentItemPosition();
                //Las uds van a ser la posición del wheel picker + 1
                if(itemSel != 0){
                    unidadesWheel = itemSel;
                    alimento.setCantidad(unidadesWheel);
                    alimentoDB.actualizarUnidades(alimento.getId(), unidadesWheel);
                    //CustomPageAdapter.setCambio(true);
                } else {
                    dialogos.dialogCeroUnidades(
                            constraintLayout,
                            alimento.getId(),
                            getActivity().getApplicationContext(),
                            imagen,
                            alimento.getNombreAlimento(),
                            customPageAdapter,
                            posicion,
                            wheelPicker,
                            alimento.getCantidad());
                }
            }
        });
    }

    /**
     * Método para cargar los datos en el detalle  cuando sea necesario
     * @param view View sobre el que actúa el onClick
     */
    private void cargarDetallesAlimento(View view){

        tvNombreAlimento = (TextView)view.findViewById(R.id.tvNombreAlimentoDetalles);
        tvFechaCaducidad = (TextView)view.findViewById(R.id.tvFechaCaducidadDetalles);
        tvDiasRestantes = (TextView)view.findViewById(R.id.tvDiasRestantesDetalles);
        ivAlimento = (ImageView)view.findViewById(R.id.ivAlimentoDetalles);
        constraintLayout = (View) view.findViewById(R.id.constraintLayout);
        ivFondoBlur = (ImageView) view.findViewById(R.id.ivFondoBlur);

        tvNombreAlimento.setText(alimento.getNombreAlimento());
        tvFechaCaducidad.setText(alimento.getFecha_caducidad());
        try {
            tvDiasRestantes.setText(String.valueOf(fecha.fechaDias(alimento.getFecha_caducidad(), getContext())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Blurry.with(getContext()).from(BitmapFactory.decodeResource(getResources(), R.drawable.inside_fridge2)).into(ivFondoBlur);

        if(imagen != null){
            //ivAlimento.setImageBitmap(imagen);
            comprimirImagen(imagen, ivAlimento);
        } else {
            //ivAlimento.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.no_image_found));
            comprimirImagen(BitmapFactory.decodeResource(getResources(), R.drawable.no_image_found), ivAlimento);
        }

    }

    /**
     * Método para comprimir la imágen a mostrar
     *
     * @param bitmap    Imagen a comprimir
     * @param imageView ImageView donde se muestra dicha imagen
     */
    public void comprimirImagen(Bitmap bitmap, ImageView imageView){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        try {
            Glide.with(this)
                    .load(stream.toByteArray())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
