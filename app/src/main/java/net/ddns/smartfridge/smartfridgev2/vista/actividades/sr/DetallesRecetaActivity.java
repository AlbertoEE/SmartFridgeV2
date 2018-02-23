package net.ddns.smartfridge.smartfridgev2.vista.actividades.sr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;

public class DetallesRecetaActivity extends AppCompatActivity {
    private Intent intent;//Para recoger el intent del otro Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_receta);
        intent = getIntent();
        ImageView iv = (ImageView)findViewById(R.id.ivDetalleReceta);
        iv.setImageBitmap((Bitmap)intent.getParcelableExtra("imagen"));
        TextView tvTiempo = (TextView) findViewById(R.id.tvNumeroProductos);
        tvTiempo.setText(intent.getStringExtra("duracion"));
        TextView tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);
        tvDescripcion.setText(intent.getStringExtra("descripcion"));
    }
}
