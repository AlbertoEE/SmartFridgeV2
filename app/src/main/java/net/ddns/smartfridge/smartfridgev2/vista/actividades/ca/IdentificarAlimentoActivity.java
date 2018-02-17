package net.ddns.smartfridge.smartfridgev2.vista.actividades.ca;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.api.client.extensions.android.http.AndroidHttp;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.servicios.CloudVision;
import net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Dialogos;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Firma;
import net.ddns.smartfridge.smartfridgev2.modelo.utiles.Permiso;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.GestorAlmacenamientoInterno;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class IdentificarAlimentoActivity extends AppCompatActivity {
    public static final int PERMISOS = 5;//Cte que representa el valor que le daremos al parámetro onRequestPermissionsResult del grantResult, en el caso
    //de que el usuario no haya concedido los permisos necesarios
    private static final String KEY = "data";//Cte para el nombre de la clave "data"
    private Bitmap imagenCamara = null;//Para almacenar la imagen
    private static final String API_KEY = "AIzaSyDnjuzwlVTlgcubURXS3xhFwmuKBEvxGGQ";
    private GestorAlmacenamientoInterno gai;//Para almacenar la foto
    private Uri fotoUri;//Para almacenar la Uri de la foto para api Cloud Vision
    private CustomDialogProgressBar customDialogProgressBar;//Para el progressbar personalizado
    private static final String HEADER = "X-Android-Package";//Para general el header de la solicitud http
    private static final String CERT = "X-Android-Cert";//Para el certificado en la validación
    private Bitmap imagenEscalada;//Para escalar el bitmap de la foto hecha por la cámara
    private static final int NUM_RESULTADOS = 10;//Para el número máximo de resultados que nos da el API Vision
    private static final String[] FEATURES = {"LABEL_DETECTION", "CROP_HINTS", "DOCUMENT_TEXT_DETECTION", "FACE_DETECTION", "IMAGE_PROPERTIES", "LANDMARK_DETECTION",
            "LOGO_DETECTION", "SAFE_SEARCH_DETECTION", "TEXT_DETECTION", "TYPE_UNSPECIFIED", "WEB_ANNOTATION"};//Parámetro a detectar en la imagen
    private static Feature labelDetection;//Para darle las características del label_detection
    private Intent i;//Para recoger el intent de otra activity
    private Dialogos dialogos;//Para utilizar la clase con los diálogos
    private String codigo_barras;//Para recoger el código de barras cuando venga de un código no encontrado
    private CloudVision cvision;//Para crear una instancia y usar los métodos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificar_alimento);
        AlimentoDB alimentoDB = new AlimentoDB(this);
        dialogos = new Dialogos(this, this);
        gai = new GestorAlmacenamientoInterno(this);
        customDialogProgressBar = new CustomDialogProgressBar(this);
        cvision = new CloudVision();
        try {
            i = getIntent();
            if (i.getStringExtra("ClasePadre").equals("ConfirmarAlmientoActivity")) {
                dialogos.dialogNoCodBarras();
                codigo_barras = i.getStringExtra("CODIGO_BARRAS");
                Log.d("cod", "codigo 2: " + codigo_barras);
            }
        } catch (NullPointerException e){
            //No hacemos nada
        }
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
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imagenCamara = (Bitmap) extras.get(KEY);
                //Comprobamos si se ha hecho una foto
                if (imagenCamara != null) {
                    //Guardamos la imagen
                    gai.guardarImagen(imagenCamara);
                    //Cogemos la uri de la foto que hacemos
                    //Log.d("seguimiento", "uri: " + FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", cogerArchivoCamara()));
                    //fotoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", cogerArchivoCamara());
                    File f = cogerArchivoCamara();
                    fotoUri = Uri.parse(gai.guardarImagen(imagenCamara));
                    //Llamamos al metodo cargarImagen y le pasamos la uri
                    cargarImagen(fotoUri);
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
                    320);
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
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 320: {
                if ((grantResults.length > 1) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    escanear();
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    //Llamaremos a este método para ver si están los permisos. Si están a true, llamaremos al método escanear()
    public void visionCloud(View v) {
        //Creamos la instancia del objeto Vision para usar Cloud Vision API.

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
    public void llamarHacerFoto() {
        Intent iHacerFotografia = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Miramos si hay alguna aplicación que pueda hacer la foto
        if (iHacerFotografia.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(iHacerFotografia, Permiso.PERM_FOTO);
        }
    }

    //Abre el intent de la inserción manual de alimentos
    public void insertarManualmenteButton(View view) {
        Log.d("cod", "codigo 3_A: " + codigo_barras);
        Intent intent = new Intent(this, InsertarManualmenteActivity.class);
       // if(codigo_barras!=null){
            intent.putExtra("CODIGO_BARRAS", codigo_barras);
            Log.d("cod", "codigo 3: " + codigo_barras);
        //}
        startActivity(intent);
    }

    //Metodo para coger el fichero con la imagen hecha con la cámara
    public File cogerArchivoCamara() {
        File archivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String directorioAlmcto;//Para darle el nombre a la imagen
        File fichero;
        GestorAlmacenamientoInterno gi = new GestorAlmacenamientoInterno(this);
        directorioAlmcto = gi.cogerDirectorio();
        fichero = new File(directorioAlmcto + "/imagenVision.png");
        return fichero;
    }

    //Metodo para cargar la imagen y ejecutar el AsyncTask
    public void cargarImagen(Uri uri) {
        //Comprobamos que no esté vacía
        if (uri != null) {
            //Primero escalamos la imagen
            try {
                //Ojo que puede ar fallo y sea esclarImagen(imagenCamara)
                imagenEscalada = cvision.escalarImagen(MediaStore.Images.Media.getBitmap
                        (getContentResolver(), uri), CloudVision.DIMENSION_BITMAP);
                //LLAMADA AL ASYNC TASK PASANDO COMO PARÁMETRO ESTE BITMAP
                if (conexion()) {
                    new CloudVisionTask().execute(imagenEscalada);
                } else {
                    Toast.makeText(this, "No hay conexión", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.d("seguimiento", "Error al ejecutar la consulta: " + e.getMessage());
                Toast.makeText(this, "Error al ejecutar la consulta. Por favor, vuelva a " +
                        "intentarlo.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error al cargar la imagen. Vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            Log.d("seguimiento", "Image picker da imagen a null");
        }
    }

    //Comrpobamos si hay conexión
    public boolean conexion() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    //Creamos el AsyncTask para hacer la consulta a la web
    public class CloudVisionTask extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Mostramos el progressBar personalizado
            customDialogProgressBar.showDialogCuadrado();
        }

        @Override
        protected String doInBackground(Object... objects) {
            try {
                //Construimos la instancia de Vision API
                HttpTransport ht = AndroidHttp.newCompatibleTransport();
                //Para trabajar con el Json que nos devuelve la consulta
                JsonFactory jsf = GsonFactory.getDefaultInstance();
                //Metemos la clave y creamos el objeto que va a hacer las consultas, pasándole lo necesario para inicializarla
                //Le asignamos la clave del producto
                VisionRequestInitializer ri = new VisionRequestInitializer(API_KEY) {
                    //Hacemos la peticion para inicializar el servicio Cloud Api Vision
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> request) throws IOException {
                        super.initializeVisionRequest(request);
                        //Cogemos el nombre del package
                        String nombrePackage = getPackageName();
                        //Para el header de HTTP usado para las solicitudes a las apis de Google
                        request.getRequestHeaders().set(HEADER, nombrePackage);
                        //Recogemos en un string la firma del proyecto
                        String firma = Firma.getFirma(getPackageManager(), nombrePackage);
                        //Le pasamos a los headers la cte con el valor del certificado para la app  y la firma que hemos obtenido antes
                        request.getRequestHeaders().set(CERT, firma);
                    }
                };
            /*Una vez que ya tenemos inicializado el objeto sobre el que haremos las consultas, creamos el Vision.Builder
            para trabajar con el json que nos devuelve la consulta*/
                Vision.Builder builder = new Vision.Builder(ht, jsf, null);
                //Le pasamos como argumento el objeto VisionRequestInitializer inicializado al builder
                builder.setVisionRequestInitializer(ri);
            /*Creamos el objeto Vision con el builder. Este objeto se va a encargar de manejar internamente el transporte HTTP
            y la lógica de análisis JSON para cada solicitud y cada respuesta que hagamos*/
                Vision vision = builder.build();

            /*Ahora vamos a codificar los datos de la imagen, ya que el API no puede usar Bitmap directamente, y vamos a crear un objeto Imagen. Se lo pasaremos como argumento a
            AnnotateImageRequest y activaremos la función LABEL_DETECTION
            BatchAnnotateImagesRequest bair = new BatchAnnotateImagesRequest();
            bair.setRequests(new ArrayList<AnnotateImageRequest>(){{
                AnnotateImageRequest air = new AnnotateImageRequest();
                //Convertimos la imagen escalada
                convertirBitmap(imagenEscalada);
                //Le damos las características al AnnotateImageRequest. Le vamos a dar la detección de etiquetas
                air.setFeatures(new ArrayList<Feature>(){{
                    add(caracteristicasVision(FEATURES, NUM_RESULTADOS));
                }});
                add(air);
            }});
            //Ahora tratamos la respuesta que nos envíe el API Vision

                Vision.Images.Annotate aRespuesta = vision.images().annotate(bair);
                //Si el tamaño de la imagen es muy grande, puede darnos error cuando intentemos comprimirlo a GZIP, lo inhabilitamos
                aRespuesta.setDisableGZipContent(true);
                Log.d("seguimiento", "creada Cloud Vision request objetc, enviando consulta");
                //Almacenamos la respuesta en un BatchAnnotateImagesResponse
                BatchAnnotateImagesResponse respuesta = aRespuesta.execute();*/

            /*Ahora vamos a codificar los datos de la imagen, ya que el API no puede usar Bitmap directamente, y vamos a crear un objeto Imagen. Se lo pasaremos como argumento a
            AnnotateImageRequest y activaremos la función LABEL_DETECTION*/
                //Creamos la solicitud con la imagen escalada
                AnnotateImageRequest air = new AnnotateImageRequest();
                //Escalamos la imagen
                Image imagen = cvision.convertirBitmap(imagenEscalada);
                //Le asignamos la imagen escalada al AnnotateImageRequest
                air.setImage(imagen);
                // Añadimos las características de la petición
                labelDetection = new Feature();
                //Añadimos qué queremos identificar en la fotografía
                labelDetection.setType(FEATURES[0]);
                //Añadimos el número máximo de resultados que queremos que nos devuelva la respuesta
                labelDetection.setMaxResults(NUM_RESULTADOS);
                air.setFeatures(Collections.singletonList(labelDetection));

                //Ejecutamos la petición al servicio.
                BatchAnnotateImagesRequest bair = new BatchAnnotateImagesRequest();
                //Le pasamos el AnnotateImageRequest con las caraceterísticas que le hemos dado anteriormente
                bair.setRequests(Collections.singletonList(air));
            /*Ahora tratamos la respuesta que nos envíe el API Vision.Si el tamaño de la imagen es muy grande, puede darnos error
            cuando intentemos comprimirlo a GZIP, lo inhabilitamos. Almacenamos la respuesta en un BatchAnnotateImagesResponse*/
                BatchAnnotateImagesResponse response = vision.images().annotate(bair).setDisableGZipContent(true).execute();
                Log.d("seguimiento", "creada Cloud Vision request objetc, enviando consulta");
                //El metodo de tratarRespuesta nos devuelve un String
                return cvision.tratarRespuesta(response);
            } catch (IOException e) {
                Log.d("seguimiento", "Error al ejecutar API Vision: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            customDialogProgressBar.endDialog();
            Log.d("json", "mensaje: " + s);
            Toast.makeText(IdentificarAlimentoActivity.this, "" + s, Toast.LENGTH_SHORT).show();
            //Vamos a crear el Intent para abrir el activity de ConfirmarAlimentoActivity
            Intent i = new Intent(getApplicationContext(), ConfirmarAlimentoActivity.class);
            //Le pasamos el nombre de la clase padre
            i.putExtra("ClasePadre", "IdentificarAlimentoActivity");
            if (s!=null){
                //Le pasamos el nombre del elemento
                i.putExtra("nombreCloud", s);
                //Si hemos detectado algún nombre, cambiaremos la variable a true
                i.putExtra("elementoEncontrado", true);
            } else {
                //Controlamos que haya algún dato
                i.putExtra("nombreCloud", "Elemento desconido");
                //Si no hemos detectado el nombre, cambiaremos la variable a false
                i.putExtra("elementoEncontrado", false);
            }
            //Le pasamos la imagen
            i.putExtra("imagenCloud", imagenCamara);
            startActivity(i);
        }
    }
}
