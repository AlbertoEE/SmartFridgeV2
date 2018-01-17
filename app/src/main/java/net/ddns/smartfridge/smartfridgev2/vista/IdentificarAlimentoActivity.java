package net.ddns.smartfridge.smartfridgev2.vista;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.Permiso;
import net.ddns.smartfridge.smartfridgev2.persistencia.GestorAlmacenamientoInterno;

import java.io.File;

public class IdentificarAlimentoActivity extends AppCompatActivity {
    public static final int PERMISOS = 5;//Cte que representa el valor que le daremos al parámetro onRequestPermissionsResult del grantResult, en el caso
    //de que el usuario no haya concedido los permisos necesarios
    private static final String KEY = "data";//Cte para el nombre de la clave "data"
    private static final String KEY_URI = "Uri";//Cte para el nombre de la clave "Uri"
    private Bitmap imagenCamara = null;//Para almacenar la imagen
    private static final String API_KEY= "AIzaSyDnjuzwlVTlgcubURXS3xhFwmuKBEvxGGQ";
    private GestorAlmacenamientoInterno gai;//Para almacenar la foto
    private static final String NOMBRE_FOTO_CAMARA = "imagenVision.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificar_alimento);
        gai = new GestorAlmacenamientoInterno(this);

    }

    public void escanear() {
        Intent intent = new Intent(this, EscanerActivity.class);
        //Para que no se guarde el histórico de códigos escaneados
        intent.putExtra("SAVE_HISTORY", false);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this, "ResultCode: " + requestCode, Toast.LENGTH_SHORT).show();
        //Si viene del escaner
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                //Log.d("scaner", "contents: " + contents);
            } else if (resultCode == RESULT_CANCELED) {
                //Log.d("scaner", "RESULT_CANCELED");
            }
            //Si viene del api vision
        } else {
            if(resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imagenCamara = (Bitmap) extras.get(KEY);
                //Comprobamos si se ha hecho una foto
                if (imagenCamara != null) {
                    gai.guardarImagen(imagenCamara);
                } else {//Si no se ha hecho, se lo indicamos al usuario
                    Toast.makeText(this, "Error al tomar la fotografía. Por favor, vuelva a intentarlo.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Llamaremos a este método para ver si están los permisos. Si están a true, llamaremos al método escanear()
    public void scaner(View v) {
        Permiso permiso = new Permiso();
        if (permiso.permisoCamara(this, this)) {
            escanear();
        } else {//Si no están los permisos, lo indicamos y no se podrá iniciar el scanner
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA},
                    PERMISOS);
        }
    }

    //Para comprobar el resultado del permiso de acceso a la cámara
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //Miramos el código de respuesta a qué permiso pertenece
            case PERMISOS: {
                if ((grantResults.length > 1) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    llamarHacerFoto();
                } else {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Llamaremos a este método para ver si están los permisos. Si están a true, llamaremos al método escanear()
    public void visionCloud(View v) {
        //Creamos la instancia del objeto Vision para usar Cloud Vision API.
        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(), new AndroidJsonFactory(),null);
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer(API_KEY));
        Vision vision = visionBuilder.build();
        //Solicitamos los permisos
        Permiso permiso = new Permiso();
        if (permiso.permisoCamara(this, this) && permiso.permisoEscritura(this, this) && (permiso.permisoLectura(this, this))) {
            llamarHacerFoto();
        } else {//Si no están los permisos, lo indicamos y no se podrá hacer la fotografía
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISOS);
        }
    }

    //Para llamar a la cámara para hacer la foto del Cloud
    public void llamarHacerFoto(){
        Intent iHacerFotografia = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Miramos si hay alguna aplicación que pueda hacer la foto
        if (iHacerFotografia.resolveActivity(this.getPackageManager()) != null) {
            //Cogemos la uri de la foto que hacemos
            Uri fotoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", cogerArchivoCamara());
            //Le pasamos la Uri al Intent
            iHacerFotografia.putExtra(KEY_URI, fotoUri);
            iHacerFotografia.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(iHacerFotografia, Permiso.PERM_FOTO);
        }
    }

    public void insertarManualmenteButton(View view){
        Intent intent = new Intent(this, InsertarManualmenteActivity.class);
        startActivity(intent);
    }

    //Metodo para coger el fichero con la imagen hecha con la cámara
    public File cogerArchivoCamara(){
        File archivo = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File (archivo, NOMBRE_FOTO_CAMARA);
    }
}
