package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.servicios.ComprobarCaducidadIntentService;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;

import java.util.ArrayList;
import java.util.List;

public class DetallesActivity extends AppCompatActivity {
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
    private Alimento alimento;
    private AlimentoDB adb;
    private Bitmap bitmapService;//Para almacenar el bitmap del alimento que recibimos en el intent
    private boolean notificacion;//Para ver de donde viene el intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        dialogos = new Dialogos(this, this);
        adb = new AlimentoDB(this);
        wheelPicker = findViewById(R.id.wheelUdsDetalles);
        Intent intent = getIntent();
        alimento = (Alimento) intent.getSerializableExtra("Alimento");
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
        } else {
            notificacion = false;
        }

        wheel(wheelPicker);
        cargarDetallesAlimento();
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
            }
        });
    }

    private void cargarDetallesAlimento(){

        tvNombreAlimento = (TextView)findViewById(R.id.tvNombreAlimentoDetalles);
        tvFechaCaducidad = (TextView)findViewById(R.id.tvFechaCaducidadDetalles);
        tvDiasRestantes = (TextView)findViewById(R.id.tvDiasRestantesDetalles);
        ivAlimento = (ImageView)findViewById(R.id.ivAlimentoDetalles);
        constraintLayout = (View) findViewById(R.id.constraintLayout);
        tvNombreAlimento.setText(alimento.getNombreAlimento());
        tvFechaCaducidad.setText(alimento.getFecha_caducidad());
        tvDiasRestantes.setText(String.valueOf(alimento.getDias_caducidad()));

        //Controlamos la imagen que hay que poner
        if (notificacion){
            //Si viene de la notificación, cogemos la imagen del service
            ivAlimento.setImageBitmap(bitmapService);
            notificacion = false;
        } else {
            //Si no, lo cogemos de MiNeveraActivity
            ivAlimento.setImageBitmap(MiNeveraActivity.getImagenDetalles());
            MiNeveraActivity.setImagenDetalles(null);
        }
    }

    public void okButton(View view){
        if(unidadesWheel > 0){
            adb.actualizarUnidades(alimento.getId(), unidadesWheel);
            finish();
        }else if (unidadesWheel == 0){
            dialogos.dialogCeroUnidades(
                    constraintLayout,
                    alimento.getId(),
                    this,
                    MiNeveraActivity.getImagenDetalles(),
                    alimento.getNombreAlimento());
        }

    }
}
