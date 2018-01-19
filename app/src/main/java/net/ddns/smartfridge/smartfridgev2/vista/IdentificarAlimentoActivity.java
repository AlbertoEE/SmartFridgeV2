package net.ddns.smartfridge.smartfridgev2.vista;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.CustomDialogProgressBar;
import net.ddns.smartfridge.smartfridgev2.modelo.Firma;
import net.ddns.smartfridge.smartfridgev2.modelo.Permiso;
import net.ddns.smartfridge.smartfridgev2.persistencia.GestorAlmacenamientoInterno;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class IdentificarAlimentoActivity extends AppCompatActivity {
    public static final int PERMISOS = 5;//Cte que representa el valor que le daremos al parámetro onRequestPermissionsResult del grantResult, en el caso
    //de que el usuario no haya concedido los permisos necesarios
    private static final String KEY = "data";//Cte para el nombre de la clave "data"
    private Bitmap imagenCamara = null;//Para almacenar la imagen
    private static final String API_KEY = "AIzaSyDnjuzwlVTlgcubURXS3xhFwmuKBEvxGGQ";
    private GestorAlmacenamientoInterno gai;//Para almacenar la foto
    private static final String NOMBRE_FOTO_CAMARA = "imagenVision.png";//Nombre de la foto creada
    private Uri fotoUri;//Para almacenar la Uri de la foto para api Cloud Vision
    private static final int DIMENSION_BITMAP = 1000;//Para redimensionar el bitmap de la imagen
    private CustomDialogProgressBar customDialogProgressBar;//Para el progressbar personalizado
    private static final String HEADER = "X-Android-Package";//Para general el header de la solicitud http
    private static final String CERT = "X-Android-Cert";//Para el certificado en la validación
    private static final int CALIDAD = 90;//Calidad de la imagen para pasarla de bitmap a jpeg
    private Bitmap imagenEscalada;//Para escalar el bitmap de la foto hecha por la cámara
    private static final int NUM_RESULTADOS = 10;//Para el número máximo de resultados que nos da el API Vision
    private static final String[] FEATURES = {"LABEL_DETECTION", "CROP_HINTS", "DOCUMENT_TEXT_DETECTION", "FACE_DETECTION", "IMAGE_PROPERTIES", "LANDMARK_DETECTION",
            "LOGO_DETECTION", "SAFE_SEARCH_DETECTION", "TEXT_DETECTION", "TYPE_UNSPECIFIED", "WEB_ANNOTATION"};//Parámetro a detectar en la imagen
    private static Feature labelDetection;//Para darle las características del label_detection
    private int cloudOk;//Lo vamos a usar para saber si ha habido algún resultado o no en la consulta al Cloud Vision. En función de este parámetro, se cargará
    //una funcionalidad u otra en el activity que se inicia. Si es 0, será que no ha habido resultados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identificar_alimento);
        gai = new GestorAlmacenamientoInterno(this);
        customDialogProgressBar = new CustomDialogProgressBar(this);
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
        /*
        Vision.Builder visionBuilder = new Vision.Builder(new NetHttpTransport(), new AndroidJsonFactory(),null);
        visionBuilder.setVisionRequestInitializer(new VisionRequestInitializer(API_KEY));
        Vision vision = visionBuilder.build();*/
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
            /*fotoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() +
            ".provider", cogerArchivoCamara());*/
            startActivityForResult(iHacerFotografia, Permiso.PERM_FOTO);
        }
    }

    //Abre el intent de la inserción manual de alimentos
    public void insertarManualmenteButton(View view) {
        Intent intent = new Intent(this, InsertarManualmenteActivity.class);
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
        /*File archivo = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, NOMBRE_FOTO_CAMARA);*/
        return fichero;
    }

    //Metodo para cargar la imagen
    public void cargarImagen(Uri uri) {
        //Comprobamos que no esté vacía
        if (uri != null) {
            //Primero escalamos la imagen
            try {
                //Ojo que puede ar fallo y sea esclarImagen(imagenCamara)
                imagenEscalada = escalarImagen(MediaStore.Images.Media.getBitmap
                        (getContentResolver(), uri), DIMENSION_BITMAP);
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

    //Metodo para escalar la imagen y darle el tamaño de 1000x1000
    public Bitmap escalarImagen(Bitmap bitmap, int dimension) {
        int anchoOriginal = bitmap.getWidth();
        int altoOriginal = bitmap.getHeight();
        int anchoRed = DIMENSION_BITMAP;
        int altoRed = DIMENSION_BITMAP;
        //Creamos los if para que se ajuste el tamaño
        if (altoOriginal > anchoOriginal) {
            //Si el alto original es mayor que el ancho original
            altoRed = DIMENSION_BITMAP;
            anchoRed = (int) ((altoRed * (float) anchoOriginal) / (float) altoOriginal);
        } else if (anchoOriginal > altoOriginal) {
            //Si el ancho original es mayor que el alto original
            anchoRed = DIMENSION_BITMAP;
            altoOriginal = (int) ((anchoRed * (float) altoOriginal) / (float) anchoOriginal);
        } else {
            //En el caso de que ambos valores sean iguales
            anchoRed = DIMENSION_BITMAP;
            altoRed = DIMENSION_BITMAP;
        }
        return Bitmap.createScaledBitmap(bitmap, anchoRed, altoRed, false);
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
                Image imagen = convertirBitmap(imagenEscalada);
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
                return tratarRespuesta(response);
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
            i.putExtra("ClasePadre", "IdentificarAlimentoActivity");
            startActivity(i);
        }
    }

    //Metodo para crear un objeto Imagen a partir del Bitmap obtenido de la camara
    public static Image convertirBitmap(Bitmap b) {
        //Instanciamos el objeto Image
        Image base64 = new Image();
        //Creamos el stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Comprimimos el bitmap, lo pasamos a jpeg con calidad 90
        b.compress(Bitmap.CompressFormat.JPEG, CALIDAD, baos);
        //Almacenamos el nuevo archivo en un array de bytes
        byte[] imBytes = baos.toByteArray();
        //Lo pasamos a Base64(representación del array de bytes en base 64)
        return base64.encodeContent(imBytes);
    }

    //Método para tratar la respuesta obtenida por el API
    public String tratarRespuesta(BatchAnnotateImagesResponse respuesta) {
        /*Nos devuelve un EntityAnnotation. Almacenamos todos los datos en un List. Por cada "coincidencia" que encuentre va a tener un objeto de tipo Label
        Vamos a almacenar todas estas etiquetas en un List*/
        String [] clave = {"pen", "bottle", "lemon", "tomato", "egg"};
        String mensaje="";//Para almacenar el mensaje que se mostrará al usuario
        List<EntityAnnotation> etiquetas = respuesta.getResponses().get(0).getLabelAnnotations();
        //Comprobamos si hemos recibido respuesta
        if (etiquetas != null){
            //Recorremos la lista
            for (EntityAnnotation etiqueta : etiquetas){
                mensaje = String.format(Locale.US, etiqueta.getDescription());
                mensaje += "\n";
                //Hacemos un bucle con el array de objetos y miramos si hay alguna coincidencia
                for (int j=0; j<5;j++){
                    if(mensaje.contains(clave[j])){
                        Log.d("json", "El objeto es: " + clave[j]);
                    }
                }
                Log.d("json", mensaje);
            }
            cloudOk=1;
        } else {
            mensaje = "Lo sentimos, no se ha encontrado ninguna coincidencia.";
            cloudOk=0;
        }
        String mensaje2 = "Prueba";
        return mensaje2;
    }

    public int getCloudOk() {
        return cloudOk;
    }
}
