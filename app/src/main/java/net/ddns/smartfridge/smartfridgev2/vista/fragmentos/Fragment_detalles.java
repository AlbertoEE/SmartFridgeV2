package net.ddns.smartfridge.smartfridgev2.vista.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.servicios.ComprobarCaducidadIntentService;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.DetallesActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.MiNeveraActivity;

import java.util.ArrayList;
import java.util.List;

public class Fragment_detalles extends Fragment {
    private int unidadesWheel;
    private static int MAXUDS = 50;
    private WheelPicker wheelPicker;
    private TextView tvNombreAlimento;
    private TextView tvFechaCaducidad;
    private TextView tvDiasRestantes;
    private ImageView ivAlimento;
    private Dialogos dialogos;
    private View constraintLayout;
    private MiNeveraActivity miNeveraActivity;
    private AlimentoDB adb;
    private Bitmap bitmapService;//Para almacenar el bitmap del alimento que recibimos en el intent
    private boolean notificacion;//Para ver de donde viene el intent
    private Alimento alimento;
    private Bitmap imagen;
    private DetallesActivity detallesActivity;
    private AlimentoDB alimentoDB;

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles, container, false);
        wheelPicker = view.findViewById(R.id.wheelUdsDetalles);
        alimentoDB = new AlimentoDB(getContext());
        readBundle(getArguments());
        Log.d("SWIPE", "onCreate: estoy aqui en el Fragment_detalles" );

        dialogos = new Dialogos(getActivity().getApplicationContext(), getActivity());
        adb = new AlimentoDB(this.getActivity().getApplicationContext());

        Intent intent = getActivity().getIntent();
        /*//alimento = (Alimento) intent.getSerializableExtra("Alimento");
        //Log.d("servicio", "clasepadre " + intent.getStringExtra("ClasePadre").equals("Dialogos"));
        if(intent.getStringExtra("ClasePadre").equals("Dialogos")){
            notificacion = true;
            bitmapService = ComprobarCaducidadIntentService.getBm();
            alimento.setImagen(bitmapService);
          /*  int id = intentRecyclerView.getIntExtra("id", 0);
            String nombreAlimento = intentRecyclerView.getStringExtra("nombre");
            int cantidad = intentRecyclerView.getIntExtra("cantidad", 8);
            String fecha_registro = intentRecyclerView.getStringExtra("fecha_registro");
            String fecha_caducidad = intentRecyclerView.getStringExtra("fecha_caducidad");
            int dias_caducidad = intentRecyclerView.getIntExtra("dias_caducidad", 88);
            Bitmap imagen = (Bitmap)intentRecyclerView.getExtras().get("imagen");*/
        /*} else {
            notificacion = false;
        }*/

        cargarDetallesAlimento(view);

        return view;
    }

    public static Fragment_detalles newInstance(Alimento alimento, Bitmap imagen) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("alimento", alimento);
        bundle.putParcelable("imagen", imagen);

        Fragment_detalles fragment = new Fragment_detalles();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            this.alimento = (Alimento) bundle.getSerializable("alimento");
            this.imagen = (Bitmap) bundle.getParcelable("imagen");

            wheel(wheelPicker);
        }

    }

    public void wheel(WheelPicker wheelPicker){
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
        unidadesWheel = 1;
        /*Para poner color de fondo
        wheelPicker.setBackgroundColor(getResources().getColor(R.color.viewfinder_laser));*/
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                int itemSel = picker.getCurrentItemPosition();
                //Las uds van a ser la posición del wheel picker + 1
                unidadesWheel = itemSel;
                alimentoDB.actualizarUnidades(alimento.getId(), unidadesWheel);

            }
        });
    }

    private void cargarDetallesAlimento(View view){

        tvNombreAlimento = (TextView)view.findViewById(R.id.tvNombreAlimentoDetalles);
        tvFechaCaducidad = (TextView)view.findViewById(R.id.tvFechaCaducidadDetalles);
        tvDiasRestantes = (TextView)view.findViewById(R.id.tvDiasRestantesDetalles);
        ivAlimento = (ImageView)view.findViewById(R.id.ivAlimentoDetalles);
        constraintLayout = (View) view.findViewById(R.id.constraintLayout);
        tvNombreAlimento.setText(alimento.getNombreAlimento());
        tvFechaCaducidad.setText(alimento.getFecha_caducidad());
        tvDiasRestantes.setText(String.valueOf(alimento.getDias_caducidad()));


        if(imagen != null){
            ivAlimento.setImageBitmap(imagen);
        }

        //Controlamos la imagen que hay que pon
    }

    public void okButton(View view){
        if(unidadesWheel > 0){
            adb.actualizarUnidades(alimento.getId(), unidadesWheel);
            this.getActivity().finish();
        }else if (unidadesWheel == 0){
            dialogos.dialogCeroUnidades(
                    constraintLayout,
                    alimento.getId(),
                    this.getActivity().getApplicationContext(),
                    imagen,
                    alimento.getNombreAlimento());
        }

    }
}
