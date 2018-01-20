package net.ddns.smartfridge.smartfridgev2.modelo.servicios;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

/**
 * Clase que representa la lógica para acceder al API de Google Cloud Vision
 */

public class CloudVision {
    public static final int DIMENSION_BITMAP = 1000;//Para redimensionar el bitmap de la imagen
    private static final int CALIDAD = 90;//Calidad de la imagen para pasarla de bitmap a jpeg
    private static final String [] clave = {"pen", "bottle", "lemon", "tomato", "egg"};//Array con palabras clave
    private static final String [] claveEsp = {"bolígrafo", "botella", "limón", "tomate", "huevo"};//Array con palabras clave en español
    private int cloudOk;//Lo vamos a usar para saber si ha habido algún resultado o no en la consulta al Cloud Vision. En función de este parámetro, se cargará
    //una funcionalidad u otra en el activity que se inicia. Si es 0, será que no ha habido resultados

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
        String label="";//Para almacenar la etiqueta leida
        String mostrar=null;//Para almacenar el mensaje que se mostrará al usuario
        List<EntityAnnotation> etiquetas = respuesta.getResponses().get(0).getLabelAnnotations();
        //Comprobamos si hemos recibido respuesta
        if (etiquetas != null){
            //Recorremos la lista
            for (EntityAnnotation etiqueta : etiquetas){
                label = String.format(Locale.US, etiqueta.getDescription());
                label += "\n";
                //Hacemos un bucle con el array de objetos y miramos si hay alguna coincidencia
                for (int j=0; j<5;j++){
                    if(label.contains(clave[j])){
                        Log.d("json", "El objeto es: " + claveEsp[j]);
                        mostrar = claveEsp[j];
                        break;
                    }
                }
                Log.d("json", label);
            }
            cloudOk=1;//Si se ha ejecutado la consulta y hemos obtenido respuesta, tomará el valor de 1
        } else {
            mostrar = "Lo sentimos, no se ha encontrado ninguna coincidencia.";
            cloudOk=0;//Tomará el valor de 0 si no hemos obtenido respuesta por parte del API
        }
        return mostrar;
    }

    public int getCloudOk() {
        return cloudOk;
    }

    public void setCloudOk(int cloudOk) {
        this.cloudOk = cloudOk;
    }
}
