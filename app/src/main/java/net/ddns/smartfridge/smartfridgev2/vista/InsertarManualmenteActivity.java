package net.ddns.smartfridge.smartfridgev2.vista;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.Permiso;

import static net.ddns.smartfridge.smartfridgev2.modelo.Permiso.PERM_FOTO2;

public class InsertarManualmenteActivity extends AppCompatActivity {
    private TextView explicacion;
    private Permiso permiso;
    private final static int COD_CAMARA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_manualmente);
        cargarMarquee();

    }

    private void cargarMarquee(){
        explicacion = (TextView) findViewById(R.id.tvExplicativo);
        explicacion.requestFocus();
        explicacion.setSelected(true);
    }

    public void hacerFotoButton(View view){
        hacerFoto();
    }

    private void hacerFoto(){
        permiso = new Permiso();
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d("PERMISOCAMARA2", String.valueOf(permiso.permisoCamara2(this, this)));
        if (permiso.permisoCamara2(this, this)
                && intentCamera.resolveActivity(this.getPackageManager()) != null){
               startActivityForResult(intentCamera, COD_CAMARA);
        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, permiso.PERM_FOTO2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode){
            case PERM_FOTO2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hacerFoto();
                } else{

                }
            }
        }
    }
}
