package net.ddns.smartfridge.smartfridgev2.modelo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import net.ddns.smartfridge.smartfridgev2.vista.CaducidadAlimento;
import net.ddns.smartfridge.smartfridgev2.vista.IdentificarAlimentoActivity;

/**
 * Clase para mostrar los diferentes tipos de dialogs en la app
 */

public class Dialogos {
    private Context contexto;//El contexto del dialog
    private Intent intent;//Para llamar a otras Activitys

    public Dialogos(Context context){
        this.contexto=context;
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
}
