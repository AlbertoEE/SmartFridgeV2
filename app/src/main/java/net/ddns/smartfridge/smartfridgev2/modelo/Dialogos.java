package net.ddns.smartfridge.smartfridgev2.modelo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import net.ddns.smartfridge.smartfridgev2.vista.CaducidadAlimento;
import net.ddns.smartfridge.smartfridgev2.vista.ConfirmarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.ControlAlimentosActivity;
import net.ddns.smartfridge.smartfridgev2.vista.IdentificarAlimentoActivity;

import java.io.IOException;

/**
 * Clase para mostrar los diferentes tipos de dialogs en la app
 */

public class Dialogos {
    private Context contexto;//El contexto del dialog
    private Intent intent;//Para llamar a otras Activitys
    private Alimento acod;//Para crear un objeto que sea un alimento
    private AlertDialog.Builder builder;//El builder para crear los dialogs

    public Dialogos(Context context){
        this.contexto=context;
        builder = new AlertDialog.Builder(contexto);
    }
    //Se mostrará el dialog cuando el alimento encontrado en la bbdd no sea el que tiene el cliente
    public void dialogAlimentoNoEncontrado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        //Mensaje del Alert
        builder.setMessage("¡Vaya! El producto no es correcto.\nPor favor, vuelve a intentar localizar tu producto.");
        //Título
        builder.setTitle("Ups...");
        //Añadimos los botones
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(contexto, IdentificarAlimentoActivity.class);
                contexto.startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No hacemos nada
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //Se mostrará el dialog cuando el alimento encontrado en la bbdd sí sea el que tiene el cliente
    public void dialogAlimentoEncontrado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        //Mensaje del Alert
        builder.setMessage("A cotinuación pasarás a la pantalla para añadir la caducidad del producto");
        //Título
        builder.setTitle("¡¡¡¡Bieeeeeeeeeeen, somos los mejores!!!!");
        //Añadimos los botones
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(contexto, CaducidadAlimento.class);
                contexto.startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No hacemos nada
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Se mostrará el dialog cuando haya seleccionado la caducidad y las uds para confirmar los datos
    public void dialogCaducidad(int udsSeleccionadas){
        //Título
        builder.setTitle("CONFIRMAR CADUCIDAD Y CANTIDAD");
        //Mensaje del Alert
        builder.setMessage("Las unidades seleccionadas son: " + udsSeleccionadas + ", ¿Es correcto?");
        //Añadimos los botones
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Metodo para almacenar los datos en la nevera local
                Toast.makeText(contexto, "Elemento guardado correctamente en Tu Nevera", Toast.LENGTH_SHORT).show();
                intent = new Intent(contexto, ControlAlimentosActivity.class);
                contexto.startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No hacemos nada
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
