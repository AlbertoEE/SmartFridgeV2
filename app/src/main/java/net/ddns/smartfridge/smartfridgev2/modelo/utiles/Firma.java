package net.ddns.smartfridge.smartfridgev2.modelo.utiles;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.google.common.io.BaseEncoding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Contiene los métodos para obtener la firma digital SHA1
 */
public class Firma {

    /**
     * Método para coger la firma
     *
     * @param pm            el PckageManer necesario para obtenerla
     * @param nombrePackage el nombre del Package para obtener la firma
     * @return String con la firma
     */
//Método para coger la firma
    public static String getFirma(PackageManager pm, String nombrePackage){

        //Recogemos la información del package. Recoge toda la info del Manifest
        PackageInfo informacionPackage = null;
        try {
            //Cogemos todas las firmas del package
            informacionPackage = pm.getPackageInfo(nombrePackage, PackageManager.GET_SIGNATURES);
            //Si no hay ninguna firma, devuelve null
            if (informacionPackage == null || informacionPackage.signatures == null || informacionPackage.signatures.length == 0
                    || informacionPackage.signatures[0] == null) {
                return null;
            }
            //Lo normal es que haya al menos 1 firma que esté en la primera posición del array de firmas
            return codificarFirma(informacionPackage.signatures[0]);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    //Metodo para conseguir la firma codificada en formato hex
    private static String codificarFirma(Signature firma) {
        //Pasamos el String a array de bytes
        byte[] firmaByte = firma.toByteArray();
        try {
            //Cogemos la instancia del MessageDigest indicándole el algoritmo SHA1
            MessageDigest md = MessageDigest.getInstance("SHA1");
            //Le pasamos el array de bytes que hemos creado antes con la firma
            byte[] digest = md.digest(firmaByte);
            //Devolvemos el resultado codificado en minúscula
            return BaseEncoding.base16().lowerCase().encode(digest);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
